import java.util.Scanner;

public class Shell extends Thread {

    private Scanner scanner;
    ProcessManager processManager;

    public Shell(ProcessManager processManager) {
        super("Shell");
        this.scanner = new Scanner(System.in);
        this.processManager = processManager;
    }

    @Override
    public void run() {
        String menu = "== MENU ==\n" +
                "\n[1] - ProgMinimo" +
                "\n[2] - Fibonacci10" +
                "\n[3] - Fatorial" +
                "\n[4] - PA (Fibonacci)" +
                "\n[5] - PB (Fatorial)" +
                "\n[6] - PC (Bubblesort)" +
                "\n[7] - Test In" +
                "\n[Outro] - Test Out" +
                "\n\nDigite um dos números mostrados para criar uma instância do processo desejado:";
        System.out.println(menu);
        while (true) {
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    processManager.createProcess(Programs.progMinimo);
                    break;
                case "2":
                    processManager.createProcess(Programs.fibonacci10);
                    break;
                case "3":
                    processManager.createProcess(Programs.fatorial);
                    break;
                case "4":
                    processManager.createProcess(Programs.pa);
                    break;
                case "5":
                    processManager.createProcess(Programs.pb);
                    break;
                case "6":
                    processManager.createProcess(Programs.pc);
                    break;
                case "7":
                    processManager.createProcess(Programs.testIn);
                    break;
                default:
                    processManager.createProcess(Programs.testOut);
            }
        }
    }
}
