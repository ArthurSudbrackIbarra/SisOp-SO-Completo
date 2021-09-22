// Classe auxiliar responsavel por checar se certas interrupcoes ocorrem.
public class InterruptChecker {

    // Checa se um endereco de memoria e invalido.
    public static boolean isInvalidAddress(int index, int memorySize){
        if(index < 0 || index >= memorySize) return true;
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