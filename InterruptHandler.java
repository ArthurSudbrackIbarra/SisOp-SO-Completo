public class InterruptHandler {

    private CPU cpu;

    public InterruptHandler(CPU cpu) {
        this.cpu = cpu;
    }

    public void handle(InterruptTypes interruptFlag) {

        // Nao houve interrupcao.
        if (interruptFlag == InterruptTypes.NO_INTERRUPT)
            return;

        // Houve interrupcao.
        System.out.println("\nINTERRUPCAO ACIONADA - MOTIVO:");

        switch (interruptFlag) {

        case INVALID_ADDRESS:
            System.out.println("Endereco invalido: programa do usuario acessando endereço fora de limites permitidos.");
            endProcessAndReleaseDispatcher();

        case INVALID_INSTRUCTION:
            System.out.println("Instrucao invalida: a instrucao carregada é invalida.");
            endProcessAndReleaseDispatcher();

        case OVERFLOW:
            System.out.println("Overflow de numero inteiro.");
            endProcessAndReleaseDispatcher();

        case INVALID_REGISTER:
            System.out.println("Registrador(es) invalido(s) passados como parametro.");
            endProcessAndReleaseDispatcher();

        case TRAP_INTERRUPT:

        case CLOCK_INTERRUPT:
            System.out.println("Ciclo maximo de CPU atingido (" + MySystem.MAX_CPU_CYCLES + ").");
            saveProcessAndReleaseDispatcher();

        case END_OF_PROGRAM:
            System.out.println("Final de programa.\n");
            endProcessAndReleaseDispatcher();

        default:
            return;
        }
    }

    private void endProcessAndReleaseDispatcher() {
        // Finaliza processo (perde a referencia).
        ProcessManager.RUNNING = null;
        // Libera escalonador.
        Dispatcher.SEMA_DISPATCHER.release();
    }

    private void saveProcessAndReleaseDispatcher() {
        // Salva PCB.
        PCB process = cpu.unloadPCB();
        // Coloca na fila de prontos.
        ProcessManager.READY_LIST.add(process);
        // Libera escalonador.
        Dispatcher.SEMA_DISPATCHER.release();
    }
}
