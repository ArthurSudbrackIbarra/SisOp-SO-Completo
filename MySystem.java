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

	public static final int MAX_INTEGER_SIZE = 32767;
	public static final int MIN_INTEGER_SIZE = -32768;

	public static final int MEMORY_SIZE = 110;
	public static final int PAGE_SIZE = 10;

	public static final int MAX_CPU_CYCLES = 10;

    public MySystem(){   
		vm = new VM(); // a VM com tratamento de interrupções
	}
	
    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {

		MySystem system = new MySystem();	

		system.addProgram(Programs.pa);
		system.addProgram(Programs.pb);
		system.addProgram(Programs.testIn);

		// Memoria antes da execucao:
		Auxiliary.dump(system.vm.m, 0, MEMORY_SIZE);

		system.start();

		// Memoria apos execucao:
		Auxiliary.dump(system.vm.m, 0, MEMORY_SIZE);
		
	}

	public void addProgram(Program program){

		boolean createdProcess = vm.processManager.createProcess(vm.m, program);

		if(createdProcess){
			System.out.println("O processo para o programa " + program.getName() + " foi adicionado a fila com sucesso!");
		} else {
			System.out.println("O processo para o programa " + program.getName() + " não pôde ser criado! (Falta de memória).");
		}

	}

	public void start(){
		vm.processManager.runAllProcesses(vm.cpu);
	}

}