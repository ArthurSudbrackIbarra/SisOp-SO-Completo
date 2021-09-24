import java.util.LinkedList;

// Classe auxiliar responsavel por checar se certas interrupcoes ocorrem.
public class InterruptChecker {

    // Checa se um endereco de memoria e invalido.
    public static boolean isInvalidAddress(int pc, LinkedList<Integer> pageTable){
        if(pc < 0 || pc >= MySystem.MEMORY_SIZE){ // Maior ou menor que memoria fisica.
            return true;
        } 
        // Invade enderecos que nao pertence a pagina
        int pageOfPc = MemoryManager.pageOfPc(pc);
        if(!pageTable.contains(pageOfPc)){
            return true;
        }
        return false;
    }

    // Checa se um registrador e invalido.
    public static boolean isInvalidRegister(int index, int regSize){
        if(index < 0 || index >= regSize) return true;
        return false;
    }

    // Checa se uma operacao matematica de soma, subtracao ou multiplicacao causa
    // overflow de numero inteiro.
    public static boolean causesMathematicalOverflow(int a, int b, int operation){
        long result;
        switch (operation) {
            case 1:
                result = (long) a + (long) b;
            break;
            case 2:
                result = (long) a - (long) b;
            break;
            case 3:
                result = (long) a * (long) b;
            break;
            default:
                return true;
        }
        if(result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) return true;
        return false;
    }
}