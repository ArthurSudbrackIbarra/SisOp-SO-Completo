import java.util.LinkedList;
import java.util.Scanner;

public class InterruptHandler {

    public static boolean handle(InterruptTypes interruptFlag, Word[] m, int[] reg, LinkedList<Integer> pageTable){

        // Nao houve interrupcao.
        if(interruptFlag == InterruptTypes.NO_INTERRUPT) return false;
        
        // Houve interrupcao.
        System.out.println("\nINTERRUPCAO ACIONADA - MOTIVO:");
        
        switch (interruptFlag) {

            case INVALID_ADDRESS:
                System.out.println("Endereco invalido: programa do usuario acessando endereço fora de limites permitidos.");
            return true;

            case INVALID_INSTRUCTION:
                System.out.println("Instrucao invalida: a instrucao carregada é invalida.");
            return true;

            case OVERFLOW:
                System.out.println("Overflow de numero inteiro.");
            return true;

            case INVALID_REGISTER:
                System.out.println("Registrador(es) invalido(s) passados como parametro.");
            return true;

            case TRAP_INTERRUPT:
                int inOrOut = reg[7];
                int physicalAddress = MemoryManager.translate(reg[8], pageTable);
                if(inOrOut == 1){ // IN
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("[CHAMADA DE SISTEMA TRAP] Informe um valor inteiro (IN): ");      
                    int value = Integer.parseInt(scanner.nextLine());
                    scanner.close();
                    if(InterruptChecker.causesOverflow(value, -1, InterruptChecker.SOLE_NUMBER)){
                        return handle(InterruptTypes.OVERFLOW, m, reg, pageTable);
                    }
                    m[physicalAddress].opc = Opcode.DATA;
                    m[physicalAddress].p = value;                
                } else if (inOrOut == 2){ // OUT
                    System.out.println("\n[CHAMADA DE SISTEMA TRAP] [OUTPUT]\n" + m[physicalAddress].p + "\n");
                }
            return false;

            case CLOCK_INTERRUPT:
                System.out.println("Ciclo maximo de CPU atingido (" + MySystem.MAX_CPU_CYCLES + ").");
            return false;

            case END_OF_PROGRAM:
                System.out.println("Final de programa.\n");
            return true;

            default:
            return false;

        }
    }
}
