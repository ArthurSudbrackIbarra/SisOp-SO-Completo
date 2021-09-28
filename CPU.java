import java.util.LinkedList;

// -------------------------------------------------------------------------------------------------------
// --------------------- C P U  -  definicoes da CPU ----------------------------------------------------- 

public class CPU {

	// característica do processador: contexto da CPU ...
	private int pc; 			// ... composto de program counter,
	private Word ir; 			// instruction register,
	private int[] reg;       	// registradores da CPU
	
	private Word[] m;   // CPU acessa MEMORIA, guarda referencia 'm' a ela. memoria nao muda. ee sempre a mesma.

	private int currentProcessId;
	private LinkedList<Integer> pageTable;

	public CPU(Word[] m) {  
		this.pc = 0; 
		this.ir = null;
		this.reg = new int[9]; 		// aloca o espaço dos registradores
		this.m = m;		
		this.currentProcessId = -1;
		this.pageTable = null;
	}
	
	public void setContext(int pc) {  
		this.pc = pc;                                             
	}
	
	public void showState(){
		System.out.println("       "+ pc); 
		System.out.print("           ");
		for (int i=0; i<9; i++) { System.out.print("r"+i);   System.out.print(": "+reg[i]+"     "); };  
		System.out.println("");
		System.out.print("           ");  Auxiliary.dump(ir);
	}

	public void loadPCB(PCB pcb){
		this.currentProcessId = pcb.getId();
		this.pc = pcb.getPc();
		this.reg = pcb.getReg().clone();
		this.pageTable = new LinkedList<Integer>(pcb.getTablePage());
	}

	public PCB unloadPCB(){
		return new PCB(currentProcessId, pc, reg, pageTable);
	}
	
	// Este metodo ira retornar false para ProcessManager enquanto ainda houver comandos para rodar.
	// Este metodo ira retornar true para ProcessManager quando nao houver mais comandos para rodar.
	public boolean run() { 

		int instructionsCounter = 0;
		InterruptTypes interruptFlag = InterruptTypes.NO_INTERRUPT;

		// execucao da CPU supoe que o contexto da CPU, vide acima, esta devidamente setado
		while (instructionsCounter < MySystem.MAX_CPU_CYCLES) { 

			// ciclo de instrucoes. acaba cfe instrucao, veja cada caso.

			instructionsCounter++;

			// FETCH
			ir = m[MemoryManager.translate(pc, pageTable)]; 	// busca posicao da memoria apontada por pc, guarda em ir
			int physicalAddress;

			// if debug
			showState();
			
			// EXECUTA INSTRUCAO NO ir
			switch (ir.opc) { // para cada opcode, sua execução

				case LDI: // Rd ← k
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else {
						reg[ir.r1] = ir.p;
					}					
					pc++;
				break;

				case LDD: // Rd ← [A]
					physicalAddress = MemoryManager.translate(ir.p, pageTable);
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else {
						reg[ir.r1] = m[physicalAddress].p;
					}			
					pc++;
				break;

				case LDX: // Rd ← [Rs]
					physicalAddress = MemoryManager.translate(reg[ir.r2], pageTable);
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if(InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else {
						reg[ir.r1] = m[physicalAddress].p;
					}	
					pc++;
				break;

				case STD: // [A] ← Rs
					physicalAddress = MemoryManager.translate(ir.p, pageTable);
					if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){ 
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else {
						m[physicalAddress].opc = Opcode.DATA;
						m[physicalAddress].p = reg[ir.r1];
					}
					pc++;
				break;

				case ADD: // Rd ← Rd + Rs
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.causesOverflow(reg[ir.r1], reg[ir.r2], InterruptChecker.SUM)){
						interruptFlag = InterruptTypes.OVERFLOW;
					} else {
						reg[ir.r1] = reg[ir.r1] + reg[ir.r2];
					}
					pc++;
				break;

				case MULT: // Rd ← Rd * Rs
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER ;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.causesOverflow(reg[ir.r1], reg[ir.r2], InterruptChecker.MULTIPLICATION)){
						interruptFlag = InterruptTypes.OVERFLOW;
					} else {
						reg[ir.r1] = reg[ir.r1] * reg[ir.r2];
					}
					pc++;
				break;

				case ADDI: // Rd ← Rd + k
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.causesOverflow(reg[ir.r1], ir.p, InterruptChecker.SUM)){
						interruptFlag = InterruptTypes.OVERFLOW;
					} else {
						reg[ir.r1] = reg[ir.r1] + ir.p;
					}
					pc++;
				break;

				case STX: // [Rd] ←Rs
					physicalAddress = MemoryManager.translate(reg[ir.r1], pageTable);
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else {
						m[physicalAddress].opc = Opcode.DATA;      
						m[physicalAddress].p = reg[ir.r2];          
					}
					pc++;
				break;

				case SUB: // Rd ← Rd - Rs
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.causesOverflow(reg[ir.r1], reg[ir.r2], InterruptChecker.SUBTRACTION)){
						interruptFlag = InterruptTypes.OVERFLOW;
					} else{							
						reg[ir.r1] = reg[ir.r1] - reg[ir.r2];
					}
					pc++;
				break;

				case SUBI: // Rd ← Rd – k
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.causesOverflow(reg[ir.r1], ir.p, InterruptChecker.SUBTRACTION)){
						interruptFlag = InterruptTypes.OVERFLOW;
					} else {
						reg[ir.r1] = reg[ir.r1] - ir.p;
					}
					pc++;
				break;

				case JMP: //  PC ← k
					pc = ir.p;
					break;	
					case JMPI:
					if(InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else {
						pc = reg[ir.r1];
					}
				break;

				case JMPIG: // If Rc > 0 Then PC ← Rs Else PC ← PC +1
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (reg[ir.r2] > 0) {
						pc = reg[ir.r1];
					} else {
						pc++;
					}	
				break;

				case JMPIL: // if Rc < 0 then PC ← Rs Else PC ← PC +1
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if(reg[ir.r2] < 0) {
						pc = reg[ir.r1];
					} else {
						pc++;
					}
				break;

				case JMPIE: // If Rc = 0 Then PC ← Rs Else PC ← PC +1
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (reg[ir.r2] == 0) {
						pc = reg[ir.r1];
					} else {
						pc++;
					}
				break;

				case JMPIM: // PC ← [A]
					physicalAddress = MemoryManager.translate(ir.p, pageTable);
					if(InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else {
						pc = m[physicalAddress].p;
					}
				break;

				case JMPILM: // if Rc < 0 then PC ← [A] Else PC ← PC +1
					physicalAddress = MemoryManager.translate(ir.p, pageTable);
					if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else if(reg[ir.r2] < 0) {
						pc = m[physicalAddress].p;
					} else {
						pc++;
					}
				break;

				case JMPIGM: // if Rc > 0 then PC ← [A] Else PC ← PC +1 
					physicalAddress = MemoryManager.translate(ir.p, pageTable);
					if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else if(reg[ir.r2] > 0) {
						pc = m[physicalAddress].p;
					} else {
						pc++;
					}
				break;

				case JMPIEM: // if Rc = 0 then PC ← [A] Else PC ← PC +1
					physicalAddress = MemoryManager.translate(ir.p, pageTable);
					if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
						interruptFlag = InterruptTypes.INVALID_ADDRESS;
					} else if(reg[ir.r2] == 0) {
						pc = m[physicalAddress].p;
					} else {
						pc++;
					}							
				break;

				case SWAP: // T ← Ra | Ra ← Rb | Rb ←T
					if (InterruptChecker.isInvalidRegister(ir.r1, reg.length)){
						interruptFlag = InterruptTypes.INVALID_REGISTER;
					} else if (InterruptChecker.isInvalidRegister(ir.r2, reg.length)){
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
					if(InterruptChecker.isInvalidAddress(physicalAddress, pageTable)){
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
			
			// VERIFICA INTERRUPÇÃO !!! - TERCEIRA FASE DO CICLO DE INSTRUÇÕES
			boolean programShouldEnd = InterruptHandler.handle(interruptFlag, m, reg, pageTable);		
			if(programShouldEnd){
				return true;
			}	
		}
		// INTERRUPÇÃO DE CLOCK
		interruptFlag = InterruptTypes.CLOCK_INTERRUPT;
		return InterruptHandler.handle(interruptFlag, m, reg, pageTable);	
	}
}
// ------------------ C P U - fim ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------