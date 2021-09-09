public class InterruptHandler {

    private MySystem system;

    public InterruptHandler(MySystem system){
        this.system = system;
    }

    public boolean handle(InterruptTypes interruptFlag, int pc){

        // Nao houve interrupcao.
        if(interruptFlag == InterruptTypes.NO_INTERRUPT) return false;
        
        // Houve interrupcao.
        System.out.println("INTERRUPCAO ACIONADA NA POSICAO " + pc + " DE MEMORIA - MOTIVO:");
        
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
            system.trap();
            return false;

            case END_OF_PROGRAM:
            System.out.println("Final de programa.");
            return true;

            default:
            return false;
        }
    }
}
