import java.util.concurrent.Semaphore;

public class Dispatcher extends Thread {

    public static Semaphore SEMA_DISPATCHER = new Semaphore(0);

    private CPU cpu;

    public Dispatcher(CPU cpu) {
        this.cpu = cpu;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Espera processo pronto.
                SEMA_DISPATCHER.acquire();
                PCB nextProccess = ProcessManager.READY_LIST.get(0);
                ProcessManager.READY_LIST.remove(0);
                ProcessManager.RUNNING = nextProccess;
                cpu.loadPCB(nextProccess);
                // CPU liberada.
                cpu.SEMA_CPU.release();
            } catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
    }

}
