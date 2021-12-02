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
                    read(ioRequest.getProcess());
                } else {
                    write(ioRequest.getProcess());
                }
            } catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
    }

    public void read(PCB process) {
        System.out.println("[Processo " + process.getId() + " - READ]");
        System.out.print("Input: ");
        int input = Integer.parseInt(reader.nextLine());
        // Informa o valor à CPU.
        cpu.setIORequestValue(input);
        // Interrompe CPU.
        cpu.setInterruptFlag(InterruptTypes.IO_FINISHED);
    }

    public void write(PCB process) {
        System.out.println("[Processo " + process.getId() + " - WRITE]");
        int output = process.getReg()[8];
        // Informa o valor à CPU.
        cpu.setIORequestValue(output);
        // Interrompe CPU.
        cpu.setInterruptFlag(InterruptTypes.IO_FINISHED);
    }

}
