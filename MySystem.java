import java.util.Scanner;

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

    public MySystem(){   // a VM com tratamento de interrupções
		vm = new VM(this);
	}
	
	// -------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------
	// ------------------- S O F T W A R E - inicio ----------------------------------------------------------

	public void trap(){
		int inOrOut = vm.cpu.reg[7];
		int address = vm.cpu.reg[8];
		if(inOrOut == 1){ // IN
			Scanner scanner = new Scanner(System.in);
			System.out.print("[CHAMADA DE SISTEMA TRAP] Informe um valor inteiro (IN): ");
			int value = Integer.parseInt(scanner.nextLine());
			vm.m[address].opc = Opcode.DATA;
			vm.m[address].p = value;
			scanner.close();
		} else if (inOrOut == 2){ // OUT
			System.out.println("\n[CHAMADA DE SISTEMA TRAP] [OUTPUT]\n" + vm.m[address].p + "\n");
		}
	}
	
	// ------------------- S O F T W A R E - fim ----------------------------------------------------------
	
    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {
		MySystem s = new MySystem();		
		//s.test1(); // Descomente para testar progMinimo
		//s.test2(); // Descomente para testar fibonacci10
		//s.test3(); // Descomente para testar fatorial
		//s.testPa(); // Descomente para testar fibonacci modificado (PA)
		//s.testPb(); // Descomente para testar fatorial modificado (PB)
		//s.testPc(); // Descomente para testar bubblesort (PC)
		s.testIn(); // Descomente para testar a instrucao TRAP com entrada
		//s.testOut(); // Descomente para testar a instrucao TRAP com saida.
	}
    // -------------------------------------------------------------------------------------------------------
    // --------------- TUDO ABAIXO DE MAIN É AUXILIAR PARA FUNCIONAMENTO DO SISTEMA - nao faz parte 

	// -------------------------------------------- teste do sistema ,  veja classe de programas
	public void test1(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().fibonacci10;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 33);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 33);
	}

	public void test2(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().progMinimo;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		System.out.println("---------------------------------- após execucao ");
		vm.cpu.run();
		aux.dump(vm.m, 0, 15);
	}

	public void test3(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().fatorial;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 15);
	}

	public void testPa(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().pa;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		vm.m[20] = new Word(Opcode.DATA, -1, -1, 10); // parametro para 10 termos de fibonacci armazenado na posicao 20 da memoria
		aux.dump(vm.m, 0, 40);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 40);
	}

	public void testPb(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().pb;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		vm.m[16] = new Word(Opcode.DATA, -1, -1, 7); // parametro para fatorial de 7 (7!) armazenado na posicao 16 de memoria
		aux.dump(vm.m, 0, 40);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 40);
	}

	public void testPc(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().pc;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 70);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 70);
	}

	public void testIn(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().testIn;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 15);
	}

	public void testOut(){
		Auxiliary aux = new Auxiliary();
		Word[] p = new Programs().testOut;
		aux.carga(p, vm.m);
		vm.cpu.setContext(0);
		System.out.println("---------------------------------- programa carregado ");
		aux.dump(vm.m, 0, 15);
		vm.cpu.run();
		System.out.println("---------------------------------- após execucao ");
		aux.dump(vm.m, 0, 15);
	}

}