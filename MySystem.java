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

		int MEMORY_SIZE = 1024;
		int PAGE_SIZE = 16;
		vm = new VM(MEMORY_SIZE, PAGE_SIZE);

		this.aux = new Auxiliary(vm.memoryManager);
		this.processManager = new ProcessManager(vm.memoryManager);

	}
	
    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {

		MySystem system = new MySystem();	

		system.testProgram("PA", Programs.pa);
		system.testProgram("PB", Programs.pb);
		system.testProgram("PC", Programs.pc);

	}
    // -------------------------------------------------------------------------------------------------------
    // --------------- TUDO ABAIXO DE MAIN É AUXILIAR PARA FUNCIONAMENTO DO SISTEMA - nao faz parte 

	// -------------------------------------------- teste do sistema ,  veja classe de programas

	public void testProgram(String programName, Program program){

		boolean createdProcess = processManager.createProcess(vm.m, program);

		if(createdProcess){
			System.out.println("O processo para o programa " + program.getName() + " foi adicionado a fila com sucesso!");
		} else {
			System.out.println("O processo para o programa " + program.getName() + " não pôde ser criado!");
		}

	}

}