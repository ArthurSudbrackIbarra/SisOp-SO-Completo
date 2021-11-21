public class InterruptHandler {

    private CPU cpu;

    public InterruptHandler(CPU cpu) {
        this.cpu = cpu;
    }

    public void handle() {

        InterruptTypes interruptFlag = cpu.getInterruptFlag();

        // Houve interrupcao.
        System.out.println("\nINTERRUPCAO ACIONADA - MOTIVO:");

        switch (interruptFlag) {

        case INVALID_ADDRESS:
            System.out.println("Endereco invalido: programa do usuario acessando endereço fora de limites permitidos.");
            endProcess();

        case INVALID_INSTRUCTION:
            System.out.println("Instrucao invalida: a instrucao carregada é invalida.");
            endProcess();

        case OVERFLOW:
            System.out.println("Overflow de numero inteiro.");
            endProcess();

        case INVALID_REGISTER:
            System.out.println("Registrador(es) invalido(s) passados como parametro.");
            endProcess();

        case TRAP_INTERRUPT:
            System.out.println("Chamada de sistema [TRAP].");
            packageForConsole();

        case CLOCK_INTERRUPT:
            System.out.println("Ciclo maximo de CPU atingido (" + MySystem.MAX_CPU_CYCLES + ").");
            saveProcess();

        case END_OF_PROGRAM:
            System.out.println("Final de programa.\n");
            endProcess();

        default:
            return;
        }
    }

    private void endProcess() {
        if (ProcessManager.RUNNING != null) {
            // Finaliza processo (perde a referencia).
            ProcessManager.destroyProcess(ProcessManager.RUNNING.getId());
            ProcessManager.RUNNING = null;
        }
        // Libera escalonador.
        Dispatcher.SEMA_DISPATCHER.release();
    }

    private void saveProcess() {
        ProcessManager.RUNNING = null;
        // Salva PCB.
        PCB process = cpu.unloadPCB();
        // Coloca na fila de prontos.
        ProcessManager.READY_LIST.add(process);
        // Libera escalonador.
        Dispatcher.SEMA_DISPATCHER.release();
    }

    private void packageForConsole() {
        ProcessManager.RUNNING = null;
        PCB process = cpu.unloadPCB();
        // Coloca na lista de bloqueados.
        ProcessManager.BLOCKED_LIST.add(process);
        // Libera escalonador.
        Dispatcher.SEMA_DISPATCHER.release();
    }
}
