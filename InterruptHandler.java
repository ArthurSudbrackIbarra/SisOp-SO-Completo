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
                System.out.println(
                        "Endereco invalido: programa do usuario acessando endereço fora de limites permitidos.");
                endProcess();
                break;

            case INVALID_INSTRUCTION:
                System.out.println("Instrucao invalida: a instrucao carregada é invalida.");
                endProcess();
                break;

            case OVERFLOW:
                System.out.println("Overflow de numero inteiro.");
                endProcess();
                break;

            case INVALID_REGISTER:
                System.out.println("Registrador(es) invalido(s) passados como parametro.");
                endProcess();
                break;

            case TRAP_INTERRUPT:
                System.out.println("Chamada de sistema [TRAP].");
                packageForConsole();
                break;

            case CLOCK_INTERRUPT:
                System.out.println("Ciclo maximo de CPU atingido (" + MySystem.MAX_CPU_CYCLES + ").");
                saveProcess();
                break;

            case IO_FINISHED:
                System.out.println("Operação IO de processo concluída.");
                ioFinishedRoutine();
                break;

            case END_OF_PROGRAM:
                System.out.println("Final de programa.\n");
                endProcess();
                break;

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
        // Resetando interruptFlag da CPU.
        cpu.setInterruptFlag(InterruptTypes.NO_INTERRUPT);
        // Libera escalonador.
        if (Dispatcher.SEMA_DISPATCHER.availablePermits() == 0) {
            Dispatcher.SEMA_DISPATCHER.release();
        }
    }

    private void saveProcess() {
        ProcessManager.RUNNING = null;
        // Salva PCB.
        PCB process = cpu.unloadPCB();
        // Coloca na fila de prontos.
        ProcessManager.READY_LIST.add(process);
        // Resetando interruptFlag da CPU.
        cpu.setInterruptFlag(InterruptTypes.NO_INTERRUPT);
        // Libera escalonador.
        if (Dispatcher.SEMA_DISPATCHER.availablePermits() == 0) {
            Dispatcher.SEMA_DISPATCHER.release();
        }
    }

    private void packageForConsole() {
        ProcessManager.RUNNING = null;
        PCB process = cpu.unloadPCB();
        IORequest ioRequest;
        // Checa se o pedido é de leitura ou de escrita.
        if (cpu.getReg()[7] == 1) {
            ioRequest = new IORequest(process, IORequest.OperationTypes.READ);
        } else {
            ioRequest = new IORequest(process, IORequest.OperationTypes.WRITE);
        }
        // Coloca na lista de bloqueados.
        ProcessManager.BLOCKED_LIST.add(ioRequest);
        // Resetando interruptFlag da CPU.
        cpu.setInterruptFlag(InterruptTypes.NO_INTERRUPT);
        // Libera o console.
        Console.SEMA_CONSOLE.release();
        // Libera escalonador.
        if (Dispatcher.SEMA_DISPATCHER.availablePermits() == 0) {
            Dispatcher.SEMA_DISPATCHER.release();
        }
    }

    // ARRUMAR ESSE MÉTODO AINDA
    private void ioFinishedRoutine() {
        int finishedIOProcessId = Console.getFirstFinishedIOProcessId();
        PCB finishedIOProcess = ProcessManager.findProcessById(finishedIOProcessId);
        // Salva PCB.
        PCB interruptedProcess = cpu.unloadPCB();
        // Coloca na fila de prontos.
        ProcessManager.READY_LIST.add(interruptedProcess);
        // Resetando interruptFlag da CPU.
        cpu.setInterruptFlag(InterruptTypes.NO_INTERRUPT);
        // Colocando processo na fila de prontos na primeira
        // posição para ser executado logo em seguida.
        ProcessManager.READY_LIST.add(0, finishedIOProcess);
        // Trocando processo.
        ProcessManager.RUNNING = finishedIOProcess;
        // Escreve o valor (ioRequestValue) na memória ou printa ele na tela.
        int physicalAddress = MemoryManager.translate(cpu.getReg()[8], cpu.getPageTable());
        if (finishedIOProcess.getReg()[7] == 1) {
            cpu.m[physicalAddress].opc = Opcode.DATA;
            cpu.m[physicalAddress].p = finishedIOProcess.getIOValue();
        } else {
            System.out.println(
                    "\n[OUTPUT FROM PROCESS " + finishedIOProcess.getId() + "] " + finishedIOProcess.getIOValue()
                            + "\n");
        }
        // Libera escalonador.
        if (Dispatcher.SEMA_DISPATCHER.availablePermits() == 0) {
            Dispatcher.SEMA_DISPATCHER.release();
        }
    }
}
