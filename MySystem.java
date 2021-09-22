/**
 * @author Arthur Sudbrack Ibarra, Luiz Eduardo Mello dos Reis, Willian Magnum Albeche
 */

// PUCRS - Escola Politécnica - Sistemas Operacionais
// Prof. Fernando Dotti
// Código fornecido como parte da solução do projeto de Sistemas Operacionais
// Nomes: Arthur Sudbrack Ibarra, Luiz Eduardo Mello dos Reis e Willian Magnum Albeche
// Fase 1 - máquina virtual (vide enunciado correspondente)
//
public class MySystem {

	public VM vm;
	private Auxiliary aux;

	private ProcessManager processManager;

    public MySystem(){   // a VM com tratamento de interrupções

		int MEMORY_SIZE = 45;
		int PAGE_SIZE = 5;
		vm = new VM(MEMORY_SIZE, PAGE_SIZE);

		this.aux = new Auxiliary(vm.memoryManager);
		this.processManager = new ProcessManager(vm.memoryManager);

	}
	
    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {
		MySystem s = new MySystem();		
		//s.test1(); // Descomente para testar progMinimo
		//s.test2(); // Descomente para testar fibonacci10
		//s.test3(); // Descomente para testar fatorial
		s.testPa(); // Descomente para testar fibonacci modificado (PA)
		//s.testPb(); // Descomente para testar fatorial modificado (PB)
		//s.testPc(); // Descomente para testar bubblesort (PC)
		//s.testIn(); // Descomente para testar a instrucao TRAP com entrada
		//s.testOut(); // Descomente para testar a instrucao TRAP com saida.
	}
    // -------------------------------------------------------------------------------------------------------
    // --------------- TUDO ABAIXO DE MAIN É AUXILIAR PARA FUNCIONAMENTO DO SISTEMA - nao faz parte 

	// -------------------------------------------- teste do sistema ,  veja classe de programas
	public void test1(){
		Word[] program = new Programs().fibonacci10;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 33);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 33);
	}

	public void test2(){
		Word[] program = new Programs().progMinimo;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		System.out.println("---------------------------------- após execucao ");
		vm.cpu.run();
		aux.dump(vm.m, 0, 15);
	}

	public void test3(){
		Word[] program = new Programs().fatorial;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 15);
	}

	public void testPa(){
		Word[] program = new Programs().pa;

		boolean createdProcess = processManager.createProcess(vm.m, program);
		if(!createdProcess){
			System.out.println("O processo não pôde ser criado!");
		}


		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, program.length);
		// vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, program.length);
	}

	public void testPb(){
		Word[] program = new Programs().pb;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, program.length);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, program.length);
	}

	public void testPc(){
		Word[] program = new Programs().pc;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, program.length);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, program.length);
	}

	public void testIn(){
		Word[] program = new Programs().testIn;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, program.length);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, program.length);
	}

	public void testOut(){
		Word[] program = new Programs().testOut;
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, program.length);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, program.length);
	}

}