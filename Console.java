import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Console extends Thread {

    public static Semaphore SEMA_CONSOLE = new Semaphore(0);

    private CPU cpu;
    private Scanner reader;

    public static LinkedList<IORequest> IO_REQUESTS = new LinkedList<>();
    public static LinkedList<Integer> FINISHED_IO_PROCESS_IDS = new LinkedList<>();

    public Console(CPU cpu) {
        super("Console");
        this.cpu = cpu;
        this.reader = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {
            try {
                SEMA_CONSOLE.acquire();
                // Entrou algum processo bloqueado.
                IORequest ioRequest = IO_REQUESTS.removeFirst();
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

    private void read(PCB process) {
        System.out.println(
                "\n\n[Processo com ID = " + process.getId() + " - "
                        + ProcessManager.getProgramNameByProcessId(process.getId())
                        + " - READ] [AVISO: Console está esperando input do usuário]:\n");
        System.out.println(
                "\n\nSe não houver nenhum processo na lista de prontos a ser executado, crie outro processo, " +
                        "o qual será interrompido por uma interrupção de finalização de IO, para dar sequência ao processo que pediu IO.\n");
        int input = Integer.parseInt(reader.nextLine());
        System.out.println("\nConsole recebeu o input do usuário [OK]\n");
        process.setIOValue(input);
        addFinishedIOProcessId(process.getId());
        removeIORequest(process.getId());
    }

    private void write(PCB process) {
        System.out.println(
                "\n\n[Processo com ID = " + process.getId() + " - "
                        + ProcessManager.getProgramNameByProcessId(process.getId()) + " - WRITE]\n");
        System.out.println(
                "\n\nSe não houver nenhum processo na lista de prontos a ser executado, crie outro processo, " +
                        "o qual será interrompido por uma interrupção de finalização de IO, para dar sequência ao processo que pediu IO.\n");
        int physicalAddress = MemoryManager.translate(process.getReg()[8], process.getPageTable());
        int output = cpu.m[physicalAddress].p;
        process.setIOValue(output);
        addFinishedIOProcessId(process.getId());
        removeIORequest(process.getId());
        System.out.println(
                "\nCrie outro processo, o qual será interrompido por uma interrupção de finalização de IO, para dar sequência ao processo que pediu IO.");
    }

    private static void removeIORequest(int processId) {
        for (int i = 0; i < IO_REQUESTS.size(); i++) {
            if (IO_REQUESTS.get(i).getProcess().getId() == processId) {
                IO_REQUESTS.remove(i);
            }
        }
    }

    public static void addFinishedIOProcessId(int id) {
        FINISHED_IO_PROCESS_IDS.add(id);
    }

    public static int getFirstFinishedIOProcessId() {
        return FINISHED_IO_PROCESS_IDS.removeFirst();
    }
}
