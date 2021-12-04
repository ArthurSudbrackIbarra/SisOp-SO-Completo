import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Console extends Thread {

    public static Semaphore SEMA_CONSOLE = new Semaphore(0);

    private CPU cpu;
    private Scanner reader;

    public Console(CPU cpu) {
        this.cpu = cpu;
        this.reader = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {
            try {
                SEMA_CONSOLE.acquire();
                // Entrou algum processo bloqueado.
                IORequest ioRequest = ProcessManager.BLOCKED_LIST.removeFirst();
                if (ioRequest.getOperationType() == IORequest.OperationTypes.READ) {
                    read(ioRequest);
                } else {
                    write(ioRequest);
                }
            } catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
    }

    private void read(IORequest ioRequest) {
        System.out.println("[Processo " + ioRequest.getProcess().getId() + " - READ]");
        System.out.print("Input: ");
        int input = Integer.parseInt(reader.nextLine());
        // Informa o valor IO à CPU.
        cpu.addIORequestFinishedValue(input);
        // Informa o processo que pediu IO à CPU. O incremento da lista de
        // pedidios de IO finalizados irá interromper a CPU no próximo ciclo.
        cpu.addIORequestFinished(ioRequest);
        if (ProcessManager.READY_LIST.size() <= 0) {
            if (Dispatcher.SEMA_DISPATCHER.availablePermits() == 0) {
                Dispatcher.SEMA_DISPATCHER.release();
            }
        }
    }

    private void write(IORequest ioRequest) {
        PCB process = ioRequest.getProcess();
        System.out.println("[Processo " + process.getId() + " - WRITE]");
        int physicalAddress = MemoryManager.translate(process.getReg()[8], process.getTablePage());
        int output = cpu.m[physicalAddress].p;
        // Informa o valor IO à CPU.
        cpu.addIORequestFinishedValue(output);
        // Informa o processo que pediu IO à CPU. O incremento da lista de
        // pedidios de IO finalizados irá interromper a CPU no próximo ciclo.
        cpu.addIORequestFinished(ioRequest);
        if (ProcessManager.READY_LIST.size() <= 0) {
            if (Dispatcher.SEMA_DISPATCHER.availablePermits() == 0) {
                Dispatcher.SEMA_DISPATCHER.release();
            }
        }
    }
}
