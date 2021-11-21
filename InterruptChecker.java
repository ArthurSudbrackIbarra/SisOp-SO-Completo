import java.util.LinkedList;

// Classe auxiliar responsavel por checar se certas interrupcoes ocorrem.
public class InterruptChecker {

    // Checa se um endereco de memoria e invalido.
    public static boolean isInvalidAddress(int pc, LinkedList<Integer> pageTable) {
        if (pc < 0 || pc >= MySystem.MEMORY_SIZE) { // Maior ou menor que memoria fisica.
            return true;
        }
        // Invade enderecos que nao pertence a pagina
        int pageOfPc = MemoryManager.pageOfPc(pc);
        if (!pageTable.contains(pageOfPc)) {
            return true;
        }
        return false;
    }

    // Checa se um registrador e invalido.
    public static boolean isInvalidRegister(int index, int regSize) {
        if (index < 0 || index >= regSize)
            return true;
        return false;
    }

    // Checa se uma operacao matematica de soma, subtracao ou multiplicacao causa
    // overflow de numero inteiro ou se um numero inteiro informado e maior do que
    // o maximo permitido pelo sistema.

    public static final int SUM = 1;
    public static final int SUBTRACTION = 2;
    public static final int MULTIPLICATION = 3;
    public static final int SOLE_NUMBER = 4;

    public static boolean causesOverflow(int a, int b, int operation) {
        int result;
        switch (operation) {
        case SUM:
            result = a + b;
            break;
        case SUBTRACTION:
            result = a - b;
            break;
        case MULTIPLICATION:
            result = a * b;
            break;
        case SOLE_NUMBER:
            return a > MySystem.MAX_INTEGER_SIZE || a < MySystem.MIN_INTEGER_SIZE;
        default:
            return true;
        }
        return result > MySystem.MAX_INTEGER_SIZE || result < MySystem.MIN_INTEGER_SIZE;
    }
}