// Classe auxiliar responsavel por checar se certas interrupcoes ocorrem.
public class InterruptChecker {

    private MySystem system;

    public InterruptChecker(MySystem system){
        this.system = system;
    }

    // Checa se um endereco de memoria e invalido.
    public boolean isInvalidAddress(int index){
        if(index < 0 || index >= system.vm.cpu.m.length) return true;
        return false;
    }

    // Checa se um registrador e invalido.
    public boolean isInvalidRegister(int index){
        if(index < 0 || index >= system.vm.cpu.reg.length) return true;
        return false;
    }

    // Checa se uma operacao matematica de soma, subtracao ou multiplicacao causa
    // overflow de numero inteiro.
    public boolean causesMathematicalOverflow(int a, int b, int operation){
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