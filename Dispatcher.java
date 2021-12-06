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
                    PCB nextProccess = ProcessManager.READY_LIST.removeFirst();
                    System.out.println("\nEscalonando processo com ID = " + nextProccess.getId());
                    ProcessManager.RUNNING = nextProccess;
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
