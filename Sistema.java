// PUCRS - Escola Politécnica - Sistemas Operacionais
// Prof. Fernando Dotti
// Código fornecido como parte da solução do projeto de Sistemas Operacionais
//
// Fase 1 - máquina virtual (vide enunciado correspondente)
//
public class Sistema {
	
	// -------------------------------------------------------------------------------------------------------
	// --------------------- H A R D W A R E - definicoes de HW ---------------------------------------------- 

	// -------------------------------------------------------------------------------------------------------
	// --------------------- M E M O R I A -  definicoes de opcode e palavra de memoria ---------------------- 
	
	public class Word { 	// cada posicao da memoria tem uma instrucao (ou um dado)
		public Opcode opc; 	//
		public int r1; 		// indice do primeiro registrador da operacao (Rs ou Rd cfe opcode na tabela)
		public int r2; 		// indice do segundo registrador da operacao (Rc ou Rs cfe operacao)
		public int p; 		// parametro para instrucao (k ou A cfe operacao), ou o dado, se opcode = DADO

		public Word(Opcode _opc, int _r1, int _r2, int _p) {  
			opc = _opc;   r1 = _r1;    r2 = _r2;	p = _p;
		}
	}
    // -------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------
    // --------------------- C P U  -  definicoes da CPU ----------------------------------------------------- 

	public enum Opcode {
		DATA, ___,		    // se memoria nesta posicao tem um dado, usa DATA, se nao usada ee NULO ___
		JMP, JMPI, JMPIG, JMPIL, JMPIE,  JMPIM, JMPIGM, JMPILM, JMPIEM, STOP,   // desvios e parada
		ADDI, SUBI,  ADD, SUB, MULT,         // matematicos
		LDI, LDD, STD,LDX, STX, SWAP;        // movimentacao
	}

	public class CPU {
							// característica do processador: contexto da CPU ...
		private int pc; 			// ... composto de program counter,
		private Word ir; 			// instruction register,
		private int[] reg;       	// registradores da CPU
		private int interruptor; // interruptor da CPU

		private Word[] m;   // CPU acessa MEMORIA, guarda referencia 'm' a ela. memoria nao muda. ee sempre a mesma.
			
		private Aux aux = new Aux();

		public CPU(Word[] _m) {     // ref a MEMORIA e interrupt handler passada na criacao da CPU
			m = _m; 				// usa o atributo 'm' para acessar a memoria.
			reg = new int[8]; 		// aloca o espaço dos registradores
		}

		public void setContext(int _pc) {  // no futuro esta funcao vai ter que ser 
			pc = _pc;                                              // limite e pc (deve ser zero nesta versao)
		}
	
        public void showState(){
			System.out.println("       "+ pc); 
			System.out.print("           ");
			for (int i=0; i<8; i++) { System.out.print("r"+i);   System.out.print(": "+reg[i]+"     "); };  
			System.out.println("");
			System.out.print("           ");  aux.dump(ir);
		}

		public void run() { 		// execucao da CPU supoe que o contexto da CPU, vide acima, esta devidamente setado
			while (true) { 			// ciclo de instrucoes. acaba cfe instrucao, veja cada caso.
				// FETCH
					ir = m[pc]; 	// busca posicao da memoria apontada por pc, guarda em ir
					//if debug
					showState();
				// EXECUTA INSTRUCAO NO ir
					switch (ir.opc) { // para cada opcode, sua execução
						case LDI: // Rd ← k
							reg[ir.r1] = ir.p;
							pc++;
						break;

						case LDD:
							reg[ir.r1] = m[ir.p].p;
							pc++;
						break;

						case LDX:
							reg[ir.r1] = m[ir.r2].p;
							pc++;
						break;

						case STD: // [A] ← Rs
							m[ir.p].opc = Opcode.DATA;
							m[ir.p].p = reg[ir.r1];
							pc++;
						break;

						case ADD: // Rd ← Rd + Rs
							reg[ir.r1] = reg[ir.r1] + reg[ir.r2];
							pc++;
						break;

						case MULT: // Rd ← Rd * Rs
							reg[ir.r1] = reg[ir.r1] * reg[ir.r2];
							pc++;
						break;

						case ADDI: // Rd ← Rd + k
							reg[ir.r1] = reg[ir.r1] + ir.p;
							pc++;
						break;

						case STX: // [Rd] ←Rs
							m[reg[ir.r1]].opc = Opcode.DATA;      
							m[reg[ir.r1]].p = reg[ir.r2];          
							pc++;
						break;

						case SUB: // Rd ← Rd - Rs
							reg[ir.r1] = reg[ir.r1] - reg[ir.r2];
							pc++;
						break;

						case SUBI:
							reg[ir.r1] = reg[ir.r1] - ir.p;
							pc++;
						break;

						case JMP: //  PC ← k
							pc = ir.p;
						break;	

						case JMPI:
							pc = reg[ir.r1];
						break;

						case JMPIG: // If Rc > 0 Then PC ← Rs Else PC ← PC +1
							if (reg[ir.r2] > 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}	
						break;

						case JMPIL:
							if(reg[ir.r2] < 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}
						break;

						case JMPIE: // If Rc = 0 Then PC ← Rs Else PC ← PC +1
							if (reg[ir.r2] == 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}
						break;

						case JMPIM:
							pc = m[ir.p].p;
						break;

						case JMPILM:
							if(reg[ir.r2] < 0) {
								pc = m[ir.p].p;
							} else {
								pc++;
							}
						break;

						case JMPIGM:
							if(reg[ir.r2] > 0) {
								pc = m[ir.p].p;
							} else {
								pc++;
							}
						break;

						case JMPIEM:
							if(reg[ir.r2] == 0) {
								pc = m[ir.p].p;
							} else {
								pc++;
							}							
						break;

						case SWAP:
							int t = reg[ir.r1];
							reg[ir.r1] = reg[ir.r2];
							reg[ir.r2] = t;
							pc++;
						break;

						case STOP: // por enquanto, para execucao
						break;

						default:
						break;
					}
				
				// VERIFICA INTERRUPÇÃO !!! - TERCEIRA FASE DO CICLO DE INSTRUÇÕES
				if (ir.opc==Opcode.STOP) {   
					break; // break sai do loop da cpu
				}
			}
		}
	}
    // ------------------ C P U - fim ------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------

	public class InterruptionChecker {

		public InterruptionChecker(){}

		public boolean isInvalidAddress(Word[] m, int index){
			try {
				Word instruction = m[index];
				return false;
			} catch (IndexOutOfBoundsException error) {
				return true;
			}
		}

		public boolean isInvalidAdressRegister(int[] reg, int index){
			try {
				int register = reg[index];
				return false;
			} catch (IndexOutOfBoundsException error) {
				return true;
			}
		}

		public boolean causesMathematicalOverflow(int a, int b){
			long sum = (long) a + (long) b;
			if(sum > Integer.MAX_VALUE) return true;
			return false;
		}

	}
	
    // ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
    // -------------------------- atributos e construcao da VM -----------------------------------------------
	public class VM {
		public int tamMem;    
        public Word[] m;     
        public CPU cpu;    

        public VM(){   // vm deve ser configurada com endereço de tratamento de interrupcoes
	     // memória
  		 	 tamMem = 1024;
			 m = new Word[tamMem]; // m ee a memoria
			 for (int i=0; i<tamMem; i++) { m[i] = new Word(Opcode.___,-1,-1,-1); };
	  	 // cpu
			 cpu = new CPU(m);
	    }	
	}
    // ------------------- V M  - fim ------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------

    // --------------------H A R D W A R E - fim -------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------
	// ------------------- S O F T W A R E - inicio ----------------------------------------------------------

	// ------------------- VAZIO
	

	// -------------------------------------------------------------------------------------------------------
    // -------------------  S I S T E M A --------------------------------------------------------------------

	public VM vm;

    public Sistema(){   // a VM com tratamento de interrupções
		 vm = new VM();
	}

    // -------------------  S I S T E M A - fim --------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------

	
    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {
		Sistema s = new Sistema();
		//s.test2();
		s.test1();
		//s.test3();
	}
    // -------------------------------------------------------------------------------------------------------
    // --------------- TUDO ABAIXO DE MAIN É AUXILIAR PARA FUNCIONAMENTO DO SISTEMA - nao faz parte 

	// -------------------------------------------- teste do sistema ,  veja classe de programas
	public void test1(){
		Aux aux = new Aux();
		Word[] p = new Programas().fibonacci10;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 33);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 33);
	}

	public void test2(){
		Aux aux = new Aux();
		Word[] p = new Programas().progMinimo;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		System.out.println("---------------------------------- após execucao ");
		vm.cpu.run();
		aux.dump(vm.m, 0, 15);
	}

	public void test3(){
		Aux aux = new Aux();
		Word[] p = new Programas().fatorial;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 15);
	}

	// -------------------------------------------  classes e funcoes auxiliares
    public class Aux {
		public void dump(Word w) {
			System.out.print("[ "); 
			System.out.print(w.opc); System.out.print(", ");
			System.out.print(w.r1);  System.out.print(", ");
			System.out.print(w.r2);  System.out.print(", ");
			System.out.print(w.p);  System.out.println("  ] ");
		}
		public void dump(Word[] m, int ini, int fim) {
			for (int i = ini; i < fim; i++) {
				System.out.print(i); System.out.print(":  ");  dump(m[i]);
			}
		}
		public void carga(Word[] p, Word[] m) {
			for (int i = 0; i < p.length; i++) {
				m[i].opc = p[i].opc;     m[i].r1 = p[i].r1;     m[i].r2 = p[i].r2;     m[i].p = p[i].p;
			}
		}
   }
   // -------------------------------------------  fim classes e funcoes auxiliares
	
   //  -------------------------------------------- programas aa disposicao para copiar na memoria (vide aux.carga)
   public class Programas {
	   public Word[] progMinimo = new Word[] {
		    //       OPCODE      R1  R2  P         :: VEJA AS COLUNAS VERMELHAS DA TABELA DE DEFINICAO DE OPERACOES
			//                                     :: -1 SIGNIFICA QUE O PARAMETRO NAO EXISTE PARA A OPERACAO DEFINIDA
		    new Word(Opcode.LDI, 0, -1, 999), 		
			new Word(Opcode.STD, 0, -1, 10), 
			new Word(Opcode.STD, 0, -1, 11), 
			new Word(Opcode.STD, 0, -1, 12), 
			new Word(Opcode.STD, 0, -1, 13), 
			new Word(Opcode.STD, 0, -1, 14), 
			new Word(Opcode.STOP, -1, -1, -1) };

	   public Word[] fibonacci10 = new Word[] { // mesmo que prog exemplo, so que usa r0 no lugar de r8
			new Word(Opcode.LDI, 1, -1, 0), 
			new Word(Opcode.STD, 1, -1, 20),    // 20 posicao de memoria onde inicia a serie de fibonacci gerada  
			new Word(Opcode.LDI, 2, -1, 1),
			new Word(Opcode.STD, 2, -1, 21),      
			new Word(Opcode.LDI, 0, -1, 22),       
			new Word(Opcode.LDI, 6, -1, 6),
			new Word(Opcode.LDI, 7, -1, 30),       
			new Word(Opcode.LDI, 3, -1, 0), 
			new Word(Opcode.ADD, 3, 1, -1),
			new Word(Opcode.LDI, 1, -1, 0), 
			new Word(Opcode.ADD, 1, 2, -1), 
			new Word(Opcode.ADD, 2, 3, -1),
			new Word(Opcode.STX, 0, 2, -1), 
			new Word(Opcode.ADDI, 0, -1, 1), 
			new Word(Opcode.SUB, 7, 0, -1),
			new Word(Opcode.JMPIG, 6, 7, -1), 
			new Word(Opcode.STOP, -1, -1, -1),   // POS 16
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),   // POS 20
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1),
			new Word(Opcode.DATA, -1, -1, -1)  // ate aqui - serie de fibonacci ficara armazenada
			   };   

	   public Word[] fatorial = new Word[] { 	 // este fatorial so aceita valores positivos.   nao pode ser zero
												 // linha   coment
			new Word(Opcode.LDI, 0, -1, 6),      // 0   	r0 é valor a calcular fatorial
			new Word(Opcode.LDI, 1, -1, 1),      // 1   	r1 é 1 para multiplicar (por r0)
			new Word(Opcode.LDI, 6, -1, 1),      // 2   	r6 é 1 para ser o decremento
			new Word(Opcode.LDI, 7, -1, 8),      // 3   	r7 tem posicao de stop do programa = 8
			new Word(Opcode.JMPIE, 7, 0, 0),     // 4   	se r0=0 pula para r7(=8)
			new Word(Opcode.MULT, 1, 0, -1),     // 5   	r1 = r1 * r0
			new Word(Opcode.SUB, 0, 6, -1),      // 6   	decrementa r0 1 
			new Word(Opcode.JMP, -1, -1, 4),     // 7   	vai p posicao 4
			new Word(Opcode.STD, 1, -1, 10),     // 8   	coloca valor de r1 na posição 10
			new Word(Opcode.STOP, -1, -1, -1),    // 9   	stop
			new Word(Opcode.DATA, -1, -1, -1) };  // 10   ao final o valor do fatorial estará na posição 10 da memória   
			

		// PA, um programa que le um valor de uma determinada posição (carregada no inicio),
		// se o número for menor que zero coloca -1 no início da posição de memória para saída;
		// se for maior que zero este é o número de valores da sequencia de fibonacci a
		// serem escritos em sequencia a partir de uma posição de memória;
		public Word[] pa = new Word[] {};

		// PB: dado um inteiro em alguma posição de memória,
 		// se for negativo armazena -1 na saída; se for positivo responde o fatorial do número na saída
		public Word[] pb = new Word[] {};

		// PC: para um N definido (10 por exemplo)
		// o programa ordena um vetor de N números em alguma posição de memória;
		// ordena usando bubble sort
		// loop ate que não swap nada
		// passando pelos N valores
		// faz swap de vizinhos se da esquerda maior que da direita
		public Word[] bubblesort = new Word[] {};
    }
}

