// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {

    public Word[] m;
    public CPU cpu;

    public ProcessManager processManager;
    public Dispatcher dispatcher;

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

    }

    public void startTest() {
        cpu.start();
        dispatcher.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        processManager.createProcess(m, Programs.pa);
        processManager.createProcess(m, Programs.pb);
    }
}
// ------------------- V M - fim
// ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------