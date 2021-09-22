// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {
    public int memorySize; 
    public int pageSize;

    public Word[] m;    
    public MemoryManager memoryManager; 
    
    public CPU cpu;    

    public VM(MySystem system, int memorySize, int pageSize){   // vm deve ser configurada com endereço de tratamento de interrupcoes
        this.memorySize = memorySize;
        this.pageSize = pageSize;
        // memória
        this.m = new Word[memorySize]; // m ee a memoria
        for (int i = 0; i < memorySize; i++) {
            m[i] = new Word(Opcode.___,-1,-1,-1);
        };
        // gerenciador de memoria
        this.memoryManager = new MemoryManager(memorySize, pageSize);
        // cpu
        cpu = new CPU(m, system, memoryManager);
    }	
}
// ------------------- V M  - fim ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------