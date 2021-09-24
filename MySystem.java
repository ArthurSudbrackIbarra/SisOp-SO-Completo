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

	public static final int MEMORY_SIZE = 1024;
	public static final int PAGE_SIZE = 16;

	public static final int MAX_CPU_CYCLES = 5;

	private ProcessManager processManager;

    public MySystem(){   // a VM com tratamento de interrupções
		vm = new VM();
		this.processManager = new ProcessManager();
	}
	
    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {

		MySystem system = new MySystem();	

		system.addProgram(Programs.pa);
		system.addProgram(Programs.testIn);
		system.addProgram(Programs.pc);

		// Memoria antes da execucao:
		Auxiliary.dump(system.vm.m, 0, 150);

		system.start();

		// Memoria apos execucao:
		Auxiliary.dump(system.vm.m, 0, 150);
		
	}
    // -------------------------------------------------------------------------------------------------------
    // --------------- TUDO ABAIXO DE MAIN É AUXILIAR PARA FUNCIONAMENTO DO SISTEMA - nao faz parte 

	// -------------------------------------------- teste do sistema ,  veja classe de programas

	public void addProgram(Program program){

		boolean createdProcess = processManager.createProcess(vm.m, program);

		if(createdProcess){
			System.out.println("O processo para o programa " + program.getName() + " foi adicionado a fila com sucesso!");
		} else {
			System.out.println("O processo para o programa " + program.getName() + " não pôde ser criado!");
		}

	}

	public void start(){
		processManager.runAllProcesses(vm.cpu);
	}

}