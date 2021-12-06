import java.util.Scanner;

public class Shell extends Thread {

    private Scanner scanner;
    private ProcessManager processManager;

    public Shell(ProcessManager processManager) {
        super("Shell");
        this.scanner = new Scanner(System.in);
        this.processManager = processManager;
    }

    @Override
    public void run() {
        String menu = "===== MENU =====\n\n" +
                "\n= Criar um processo =\n" +
                "\n[1] - ProgMinimo" +
                "\n[2] - Fibonacci10" +
                "\n[3] - Fatorial" +
                "\n[4] - PA (Fibonacci)" +
                "\n[5] - PB (Fatorial)" +
                "\n[6] - PC (Bubblesort)" +
                "\n[7] - Test In" +
                "\n[8] - Test Out" +
                "\n\n= Opções extras =\n" +
                "\n[dump] - Dump da memória" +
                "\n\nDigite uma das opções mostradas:";
        System.out.println(menu);
        while (true) {
            System.out.println("\n[AVISO: Shell está esperando input do usuário]\n");
            String option = scanner.nextLine();
            System.out.println("\nShell recebeu o input do usuário [OK]\n");
            switch (option.toLowerCase()) {
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
                case "8":
                    processManager.createProcess(Programs.testOut);
                    break;
                case "dump":
                    System.out.println(Auxiliary.dump(processManager.getMemory(), 0, MySystem.MEMORY_SIZE));
                default:
                    System.out.println("\nErro: Opção inválida de input para o Schell\n");
            }
        }
    }
}
