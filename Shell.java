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
        String menu = "===== MENU =====\n" +
                "\n= Criar um processo =\n" +
                "\n[1] - Fibonacci10" +
                "\n[2] - Fatorial" +
                "\n[3] - PA (Fibonacci)" +
                "\n[4] - PB (Fatorial)" +
                "\n[5] - PC (Bubblesort)" +
                "\n[6] - Test In" +
                "\n[7] - Test Out" +
                "\n\n= Opções extras =\n" +
                "\n[dump] - Dump da memória" +
                "\n\n = Múltiplos comandos =\n\n" +
                "Você pode executar vários comandos de uma vez usando o separador ';'" +
                "\n\nExemplo: 4;dump;3" +
                "\nEfeito: Cria um processo [4 - PB], realiza o dump da memória, cria um processo [5 - PC]";
        System.out.println(menu);
        while (true) {
            System.out.println("\n[AVISO: Shell está esperando input do usuário]\n");
            String option = scanner.nextLine();
            System.out.println("\nShell recebeu o input do usuário [OK]\n");
            String[] processes = option.toLowerCase().split(";");
            for (String process : processes) {
                switch (process) {
                    case "1":
                        processManager.createProcess(Programs.fibonacci10);
                        break;
                    case "2":
                        processManager.createProcess(Programs.fatorial);
                        break;
                    case "3":
                        processManager.createProcess(Programs.pa);
                        break;
                    case "4":
                        processManager.createProcess(Programs.pb);
                        break;
                    case "5":
                        processManager.createProcess(Programs.pc);
                        break;
                    case "6":
                        processManager.createProcess(Programs.testIn);
                        break;
                    case "7":
                        processManager.createProcess(Programs.testOut);
                        break;
                    case "dump":
                        System.out.println(Auxiliary.dump(processManager.getMemory(), 0, MySystem.MEMORY_SIZE));
                        break;
                    default:
                        System.out.println("\n[ERRO: Opção inválida de input para o Schell]\n");
                }
            }
        }
    }
}
