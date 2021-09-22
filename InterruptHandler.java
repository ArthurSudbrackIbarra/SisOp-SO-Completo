import java.util.Scanner;

public class InterruptHandler {

    public static boolean handle(InterruptTypes interruptFlag, Word[] m, int[] reg){

        // Nao houve interrupcao.
        if(interruptFlag == InterruptTypes.NO_INTERRUPT) return false;
        
        // Houve interrupcao.
        System.out.println("INTERRUPCAO ACIONADA - MOTIVO:");
        
        switch (interruptFlag) {

            case INVALID_ADDRESS:
                System.out.println("Endereco invalido: programa do usuario acessando endereço fora de limites permitidos.");
            return true;

            case INVALID_INSTRUCTION:
                System.out.println("Instrucao invalida: a instrucao carregada é invalida.");
            return true;

            case MATHEMATICAL_OVERFLOW:
                System.out.println("Overflow em operacao matematica.");
            return true;

            case INVALID_REGISTER:
                System.out.println("Registrador(es) invalido(s) passados como parametro.");
            return true;

            case TRAP_INTERRUPT:
                int inOrOut = reg[7];
                int address = reg[8];
                if(inOrOut == 1){ // IN
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("[CHAMADA DE SISTEMA TRAP] Informe um valor inteiro (IN): ");
                    int value = Integer.parseInt(scanner.nextLine());
                    m[address].opc = Opcode.DATA;
                    m[address].p = value;
                    scanner.close();
                } else if (inOrOut == 2){ // OUT
                    System.out.println("\n[CHAMADA DE SISTEMA TRAP] [OUTPUT]\n" + m[address].p + "\n");
                }
            return false;

            case END_OF_PROGRAM:
                System.out.println("Final de programa.");
            return true;

            default:
            return false;
        }
    }
}
