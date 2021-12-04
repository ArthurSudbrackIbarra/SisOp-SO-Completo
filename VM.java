// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {

    public Word[] m;
    public CPU cpu;

    public ProcessManager processManager;
    public Dispatcher dispatcher;
    public Console console;

    public VM() {

        // Memória
        this.m = new Word[MySystem.MEMORY_SIZE]; // m ee a memoria
        for (int i = 0; i < MySystem.MEMORY_SIZE; i++) {
            m[i] = new Word(Opcode.___, -1, -1, -1);
        }

        // CPU
        cpu = new CPU(m);

        // Gerenciador de processos
        this.processManager = new ProcessManager();

        // Escalonador
        this.dispatcher = new Dispatcher(cpu);

        // Console
        this.console = new Console(cpu);

    }

    public void startTest() {
        cpu.start();
        dispatcher.start();
        console.start();
        processManager.createProcess(m, Programs.testIn);
        processManager.createProcess(m, Programs.pa);
    }
}
// ------------------- V M - fim
// ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------