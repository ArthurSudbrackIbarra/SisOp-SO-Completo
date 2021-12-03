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
                IORequest ioRequest = ProcessManager.BLOCKED_LIST.remove(0);
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
        cpu.setCurrentIORequestValue(input);
        // Informa o processo que pediu IO à CPU.
        cpu.setCurrentIORequest(ioRequest);
        // Interrompe CPU.
        cpu.setFinishedIO(true);
        if (ProcessManager.READY_LIST.size() <= 0) {
            cpu.SEMA_CPU.release();
        }
    }

    private void write(IORequest ioRequest) {
        PCB process = ioRequest.getProcess();
        System.out.println("[Processo " + process.getId() + " - WRITE]");
        int physicalAddress = MemoryManager.translate(process.getReg()[8], process.getTablePage());
        int output = cpu.m[physicalAddress].p;
        // Informa o valor IO à CPU.
        cpu.setCurrentIORequestValue(output);
        // Informa o processo que pediu IO à CPU.
        cpu.setCurrentIORequest(ioRequest);
        // Interrompe CPU.
        cpu.setFinishedIO(true);
        if (ProcessManager.READY_LIST.size() <= 0) {
            cpu.SEMA_CPU.release();
        }
    }

}