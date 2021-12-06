// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {

    public Word[] m;

    private Shell shell;
    public CPU cpu;
    private Dispatcher dispatcher;
    private Console console;

    public VM() {

        // Memória
        this.m = new Word[MySystem.MEMORY_SIZE]; // m ee a memoria
        for (int i = 0; i < MySystem.MEMORY_SIZE; i++) {
            m[i] = new Word(Opcode.___, -1, -1, -1);
        }

        // Shell
        this.shell = new Shell(new ProcessManager(m));

        // CPU
        cpu = new CPU(m);

        // Escalonador
        this.dispatcher = new Dispatcher(cpu);

        // Console
        this.console = new Console(cpu);

    }

    public void startThreads() {
        // Iniciando as threads.
        shell.start();
        cpu.start();
        dispatcher.start();
        console.start();
    }

}
// ------------------- V M - fim
// ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------