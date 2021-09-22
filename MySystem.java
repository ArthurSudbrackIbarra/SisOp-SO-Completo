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
		MySystem system = new MySystem();		
		system.testProgram(Programs.pa);
	}
    // -------------------------------------------------------------------------------------------------------
    // --------------- TUDO ABAIXO DE MAIN É AUXILIAR PARA FUNCIONAMENTO DO SISTEMA - nao faz parte 

	// -------------------------------------------- teste do sistema ,  veja classe de programas

	public void testProgram(Word[] program){

		boolean createdProcess = processManager.createProcess(vm.m, program);
		if(!createdProcess){
			System.out.println("O processo não pôde ser criado!");
			return;
		}

		PCB nextProcess = processManager.nextProcess();
		vm.cpu.loadPCB(nextProcess);

		vm.cpu.run();

		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, vm.m.length);
	}

}