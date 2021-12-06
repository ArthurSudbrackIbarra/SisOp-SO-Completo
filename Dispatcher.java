import java.util.concurrent.Semaphore;

public class Dispatcher extends Thread {

    public static Semaphore SEMA_DISPATCHER = new Semaphore(0);

    private CPU cpu;

    public Dispatcher(CPU cpu) {
        super("Dispatcher");
        this.cpu = cpu;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Espera processo pronto.
                SEMA_DISPATCHER.acquire();
                if (ProcessManager.READY_LIST.size() > 0) {
                    ProcessManager.RUNNING = ProcessManager.READY_LIST.removeFirst();
                    PCB nextProccess = ProcessManager.RUNNING;
                    System.out.println("\nEscalonando processo com ID = " + nextProccess.getId() + " ["
                            + ProcessManager.getProgramNameByProcessId(nextProccess.getId()) + "]\n");
                    cpu.loadPCB(nextProccess);
                    // CPU liberada.
                    cpu.SEMA_CPU.release();
                }
            } catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
    }

}
