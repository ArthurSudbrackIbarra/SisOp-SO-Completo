// -------------------------------------------------------------------------------------------------------
// --------------------- C P U  -  definicoes da CPU ----------------------------------------------------- 

public class CPU {

	private MySystem system;

	// característica do processador: contexto da CPU ...
	private int pc; 			// ... composto de program counter,
	private Word ir; 			// instruction register,
	public int[] reg;       	// registradores da CPU
	
	// Checa se algumas interrupcoes irao ocorrer.
	private InterruptChecker ic;

	// Trata as diferentes interrupcoes.
	private InterruptHandler ih;
	
	public Word[] m;   // CPU acessa MEMORIA, guarda referencia 'm' a ela. memoria nao muda. ee sempre a mesma.
	private MemoryManager memoryManager;
	
	private Auxiliary aux;
	
	public CPU(Word[] m, MySystem system, MemoryManager memoryManager) {     // ref a MEMORIA e interrupt handler passada na criacao da CPU
		this.system = system;
		this.m = m;		
		this.memoryManager = memoryManager; // usa o atributo 'm' para acessar a memoria.
		this.aux = new Auxiliary(memoryManager);		
		reg = new int[9]; 		// aloca o espaço dos registradores
		ic = new InterruptChecker(system);
		ih = new InterruptHandler(system);
	}
	
	public void setContext(int _pc) {  // no futuro esta funcao vai ter que ser 
		pc = _pc;                                              // limite e pc (deve ser zero nesta versao)
	}
	
	public void showState(){
		System.out.println("       "+ pc); 
		System.out.print("           ");
		for (int i=0; i<9; i++) { System.out.print("r"+i);   System.out.print(": "+reg[i]+"     "); };  
		System.out.println("");
		System.out.print("           ");  aux.dump(ir);
	}
	
	public void run() { 
		// execucao da CPU supoe que o contexto da CPU, vide acima, esta devidamente setado
		while (true) { 			// ciclo de instrucoes. acaba cfe instrucao, veja cada caso.
			InterruptTypes interruptFlag = InterruptTypes.NO_INTERRUPT;
			// FETCH
			ir = m[pc]; 	// busca posicao da memoria apontada por pc, guarda em ir
			//if debug
			showState();
			// EXECUTA INSTRUCAO NO ir
			switch (ir.opc) { // para cada opcode, sua execução

				case LDI: // Rd ← k
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else {
					reg[ir.r1] = ir.p;
				}					
				pc++;
				break;

				case LDD: // Rd ← [A]
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidAddress(ir.p)){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else {
					reg[ir.r1] = m[ir.p].p;
				}			
				pc++;
				break;

				case LDX: // Rd ← [Rs]
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if(ic.isInvalidAddress(reg[ir.r2])){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else {
					reg[ir.r1] = m[reg[ir.r2]].p;
				}	
				pc++;
				break;

				case STD: // [A] ← Rs
				if (ic.isInvalidAddress(ir.p)){ 
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else {
					m[ir.p].opc = Opcode.DATA;
					m[ir.p].p = reg[ir.r1];
				}
				pc++;
				break;

				case ADD: // Rd ← Rd + Rs
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.causesMathematicalOverflow(reg[ir.r1], reg[ir.r2], 1)){
					interruptFlag = InterruptTypes.MATHEMATICAL_OVERFLOW;
				} else {
					reg[ir.r1] = reg[ir.r1] + reg[ir.r2];
				}
				pc++;
				break;

				case MULT: // Rd ← Rd * Rs
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER ;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.causesMathematicalOverflow(reg[ir.r1], reg[ir.r2], 3)){
					interruptFlag = InterruptTypes.MATHEMATICAL_OVERFLOW;
				} else {
					reg[ir.r1] = reg[ir.r1] * reg[ir.r2];
				}
				pc++;
				break;

				case ADDI: // Rd ← Rd + k
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.causesMathematicalOverflow(reg[ir.r1], ir.p, 3)){
					interruptFlag = InterruptTypes.MATHEMATICAL_OVERFLOW;
				} else {
					reg[ir.r1] = reg[ir.r1] + ir.p;
				}
				pc++;
				break;

				case STX: // [Rd] ←Rs
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidAddress(reg[ir.r1])){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else {
					m[reg[ir.r1]].opc = Opcode.DATA;      
					m[reg[ir.r1]].p = reg[ir.r2];          
				}
				pc++;
				break;

				case SUB: // Rd ← Rd - Rs
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.causesMathematicalOverflow(reg[ir.r1], reg[ir.r2], 1)){
					interruptFlag = InterruptTypes.MATHEMATICAL_OVERFLOW;
				} else{							
					reg[ir.r1] = reg[ir.r1] - reg[ir.r2];
				}
				pc++;
				break;

				case SUBI: // Rd ← Rd – k
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.causesMathematicalOverflow(reg[ir.r1], ir.p, 3)){
					interruptFlag = InterruptTypes.MATHEMATICAL_OVERFLOW;
				} else {
					reg[ir.r1] = reg[ir.r1] - ir.p;
				}
				pc++;
				break;

				case JMP: //  PC ← k
				pc = ir.p;
				break;	
				case JMPI:
				if(ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else {
					pc = reg[ir.r1];
				}
				break;

				case JMPIG: // If Rc > 0 Then PC ← Rs Else PC ← PC +1
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (reg[ir.r2] > 0) {
					pc = reg[ir.r1];
				} else {
					pc++;
				}	
				break;

				case JMPIL: // if Rc < 0 then PC ← Rs Else PC ← PC +1
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if(reg[ir.r2] < 0) {
					pc = reg[ir.r1];
				} else {
					pc++;
				}
				break;

				case JMPIE: // If Rc = 0 Then PC ← Rs Else PC ← PC +1
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (reg[ir.r2] == 0) {
					pc = reg[ir.r1];
				} else {
					pc++;
				}
				break;

				case JMPIM: // PC ← [A]
				if(ic.isInvalidAddress(ir.p)){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else {
					pc = m[ir.p].p;
				}
				break;

				case JMPILM: // if Rc < 0 then PC ← [A] Else PC ← PC +1
				if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidAddress(ir.p)){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else if(reg[ir.r2] < 0) {
					pc = m[ir.p].p;
				} else {
					pc++;
				}
				break;

				case JMPIGM: // if Rc > 0 then PC ← [A] Else PC ← PC +1 
				if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidAddress(ir.p)){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else if(reg[ir.r2] > 0) {
					pc = m[ir.p].p;
				} else {
					pc++;
				}
				break;

				case JMPIEM: // if Rc = 0 then PC ← [A] Else PC ← PC +1
				if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidAddress(ir.p)){
					interruptFlag = InterruptTypes.INVALID_ADDRESS;
				} else if(reg[ir.r2] == 0) {
					pc = m[ir.p].p;
				} else {
					pc++;
				}							
				break;

				case SWAP: // T ← Ra | Ra ← Rb | Rb ←T
				if (ic.isInvalidRegister(ir.r1)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else if (ic.isInvalidRegister(ir.r2)){
					interruptFlag = InterruptTypes.INVALID_REGISTER;
				} else {
					int t = reg[ir.r1];
					reg[ir.r1] = reg[ir.r2];
					reg[ir.r2] = t;
				}
				pc++;
				break;

				case TRAP:
				if(ic.isInvalidAddress(reg[8])){
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
			boolean programShouldEnd = ih.handle(interruptFlag, pc);		
			if(programShouldEnd){
				break;
			}	
		}
	}
}
// ------------------ C P U - fim ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------