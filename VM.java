// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {

    public Word[] m;      
    public CPU cpu;    

    public ProcessManager processManager;

    public VM(){
        // memória
        this.m = new Word[MySystem.MEMORY_SIZE]; // m ee a memoria
        for (int i = 0; i < MySystem.MEMORY_SIZE; i++) {
            m[i] = new Word(Opcode.___,-1,-1,-1);
        };
        // cpu
        cpu = new CPU(m);
        // gerenciador de processos
        this.processManager = new ProcessManager();
    }	
}
// ------------------- V M  - fim ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------