import java.util.LinkedList;
import java.util.concurrent.Semaphore;

// -------------------------------------------------------------------------------------------------------
// --------------------- C P U  -  definicoes da CPU ----------------------------------------------------- 

public class CPU extends Thread {

	public Semaphore SEMA_CPU = new Semaphore(0);

	// característica do processador: contexto da CPU ...
	private int pc; // ... composto de program counter,
	private Word ir; // instruction register,
	private int[] reg; // registradores da CPU

	public Word[] m; // CPU acessa MEMORIA, guarda referencia 'm' a ela. memoria nao muda. e sempre a
						// mesma.

	private int currentProcessId;
	private LinkedList<Integer> pageTable;

	private InterruptTypes interruptFlag;
	private InterruptHandler interruptHandler;

	public CPU(Word[] m) {
		super("CPU");
		this.pc = 0;
		this.ir = null;
		this.reg = new int[9];
		this.m = m;
		this.currentProcessId = -1;
		this.pageTable = null;
		this.interruptFlag = InterruptTypes.NO_INTERRUPT;
		this.interruptHandler = new InterruptHandler(this);
	}

	public int getPC() {
		return pc;
	}

	public Word getIr() {
		return ir;
	}

	public int[] getReg() {
		return reg;
	}

	public int getCurrentProcessId() {
		return currentProcessId;
	}

	public LinkedList<Integer> getPageTable() {
		return pageTable;
	}

	public InterruptTypes getInterruptFlag() {
		return interruptFlag;
	}

	public InterruptHandler getInterruptHandler() {
		return interruptHandler;
	}

	public void setInterruptFlag(InterruptTypes interruptFlag) {
		this.interruptFlag = interruptFlag;
	}

	public void setContext(int pc) {
		this.pc = pc;
	}

	public void showState() {
		System.out.println("       " + pc);
		System.out.print("           ");
		for (int i = 0; i < 9; i++) {
			System.out.print("r" + i);
			System.out.print(": " + reg[i] + "     ");
		}
		System.out.println("");
		System.out.print("           ");
		System.out.println(Auxiliary.dump(ir));
	}

	public void loadPCB(PCB pcb) {
		this.currentProcessId = pcb.getId();
		this.pc = pcb.getPc();
		this.reg = pcb.getReg().clone();
		this.pageTable = new LinkedList<Integer>(pcb.getPageTable());
	}

	public PCB unloadPCB() {
		return new PCB(currentProcessId, pc, reg.clone(), new LinkedList<Integer>(pageTable));
	}

	@Override
	public void run() {
		while (true) {
			try {
				// Espera semaforo.
				SEMA_CPU.acquire();

				int instructionsCounter = 0;

				// execucao da CPU supoe que o contexto da CPU, vide acima, esta devidamente
				// setado
				while (true) {

					instructionsCounter++;

					// FETCH
					ir = m[MemoryManager.translate(pc, pageTable)]; // busca posicao da memoria apontada por pc, guarda
																	// em ir
					int physicalAddress;

					// if debug
					showState();

					// EXECUTA INSTRUCAO NO ir
					switch (ir.opc) { // para cada opcode, sua execução

						case LDI: // Rd ← k
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else {
								reg[ir.r1] = ir.p;
							}
							pc++;
							break;

						case LDD: // Rd ← [A]
							physicalAddress = MemoryManager.translate(ir.p, pageTable);
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else {
								reg[ir.r1] = m[physicalAddress].p;
							}
							pc++;
							break;

						case LDX: // Rd ← [Rs]
							physicalAddress = MemoryManager.translate(reg[ir.r2], pageTable);
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else {
								reg[ir.r1] = m[physicalAddress].p;
							}
							pc++;
							break;

						case STD: // [A] ← Rs
							physicalAddress = MemoryManager.translate(ir.p, pageTable);
							if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else {
								m[physicalAddress].opc = Opcode.DATA;
								m[physicalAddress].p = reg[ir.r1];
							}
							pc++;
							break;

						case ADD: // Rd ← Rd + Rs
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.causesOverflow(reg[ir.r1], reg[ir.r2], InterruptChecker.SUM)) {
								interruptFlag = InterruptTypes.OVERFLOW;
							} else {
								reg[ir.r1] = reg[ir.r1] + reg[ir.r2];
							}
							pc++;
							break;

						case MULT: // Rd ← Rd * Rs
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.causesOverflow(reg[ir.r1], reg[ir.r2],
									InterruptChecker.MULTIPLICATION)) {
								interruptFlag = InterruptTypes.OVERFLOW;
							} else {
								reg[ir.r1] = reg[ir.r1] * reg[ir.r2];
							}
							pc++;
							break;

						case ADDI: // Rd ← Rd + k
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.causesOverflow(reg[ir.r1], ir.p, InterruptChecker.SUM)) {
								interruptFlag = InterruptTypes.OVERFLOW;
							} else {
								reg[ir.r1] = reg[ir.r1] + ir.p;
							}
							pc++;
							break;

						case STX: // [Rd] ←Rs
							physicalAddress = MemoryManager.translate(reg[ir.r1], pageTable);
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else {
								m[physicalAddress].opc = Opcode.DATA;
								m[physicalAddress].p = reg[ir.r2];
							}
							pc++;
							break;

						case SUB: // Rd ← Rd - Rs
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.causesOverflow(reg[ir.r1], reg[ir.r2],
									InterruptChecker.SUBTRACTION)) {
								interruptFlag = InterruptTypes.OVERFLOW;
							} else {
								reg[ir.r1] = reg[ir.r1] - reg[ir.r2];
							}
							pc++;
							break;

						case SUBI: // Rd ← Rd – k
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.causesOverflow(reg[ir.r1], ir.p,
									InterruptChecker.SUBTRACTION)) {
								interruptFlag = InterruptTypes.OVERFLOW;
							} else {
								reg[ir.r1] = reg[ir.r1] - ir.p;
							}
							pc++;
							break;

						case JMP: // PC ← k
							pc = ir.p;
							break;
						case JMPI:
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else {
								pc = reg[ir.r1];
							}
							break;

						case JMPIG: // If Rc > 0 Then PC ← Rs Else PC ← PC +1
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (reg[ir.r2] > 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}
							break;

						case JMPIL: // if Rc < 0 then PC ← Rs Else PC ← PC +1
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (reg[ir.r2] < 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}
							break;

						case JMPIE: // If Rc = 0 Then PC ← Rs Else PC ← PC +1
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (reg[ir.r2] == 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}
							break;

						case JMPIM: // PC ← [A]
							physicalAddress = MemoryManager.translate(ir.p, pageTable);
							if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else {
								pc = m[physicalAddress].p;
							}
							break;

						case JMPILM: // if Rc < 0 then PC ← [A] Else PC ← PC +1
							physicalAddress = MemoryManager.translate(ir.p, pageTable);
							if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else if (reg[ir.r2] < 0) {
								pc = m[physicalAddress].p;
							} else {
								pc++;
							}
							break;

						case JMPIGM: // if Rc > 0 then PC ← [A] Else PC ← PC +1
							physicalAddress = MemoryManager.translate(ir.p, pageTable);
							if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else if (reg[ir.r2] > 0) {
								pc = m[physicalAddress].p;
							} else {
								pc++;
							}
							break;

						case JMPIEM: // if Rc = 0 then PC ← [A] Else PC ← PC +1
							physicalAddress = MemoryManager.translate(ir.p, pageTable);
							if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else if (reg[ir.r2] == 0) {
								pc = m[physicalAddress].p;
							} else {
								pc++;
							}
							break;

						case SWAP: // T ← Ra | Ra ← Rb | Rb ←T
							if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)) {
								interruptFlag = InterruptTypes.INVALID_REGISTER;
							} else {
								int t = reg[ir.r1];
								reg[ir.r1] = reg[ir.r2];
								reg[ir.r2] = t;
							}
							pc++;
							break;

						case TRAP:
							physicalAddress = MemoryManager.translate(reg[8], pageTable);
							if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)) {
								interruptFlag = InterruptTypes.INVALID_ADDRESS;
							} else {
								interruptFlag = InterruptTypes.TRAP_INTERRUPT;
							}
							pc++;
							break;

						case STOP: // Para a execucao
							interruptFlag = InterruptTypes.END_OF_PROGRAM;
							break;

						case DATA:
						case ___:
							break;

						// Instrucao invalida
						default:
							interruptFlag = InterruptTypes.INVALID_INSTRUCTION;
							break;
					}

					// Testa interrupcao de clock.
					if (interruptFlag == InterruptTypes.NO_INTERRUPT
							&& instructionsCounter == MySystem.MAX_CPU_CYCLES) {
						interruptFlag = InterruptTypes.CLOCK_INTERRUPT;
						break;
					}

					// Segue o loop se nao houve interrupcao.
					if (interruptFlag == InterruptTypes.NO_INTERRUPT) {
						// Checa se algum IO terminou.
						if (Console.FINISHED_IO_PROCESS_IDS.size() > 0) {
							interruptFlag = InterruptTypes.IO_FINISHED;
							break;
						}
						continue;
					}

					// Houve interrupcao, deve ser tratada (fora do loop).
					break;
				}
				// Trata interrupção.
				interruptHandler.handle();
			} catch (InterruptedException error) {
				error.printStackTrace();
			}
		}
	}
}
// ------------------ C P U - fim
// ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------