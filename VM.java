// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {

    public Word[] m;    
    public MemoryManager memoryManager; 
    
    public CPU cpu;    

    public VM(){   // vm deve ser configurada com endereço de tratamento de interrupcoes
        // memória
        this.m = new Word[MySystem.MEMORY_SIZE]; // m ee a memoria
        for (int i = 0; i < MySystem.MEMORY_SIZE; i++) {
            m[i] = new Word(Opcode.___,-1,-1,-1);
        };
        // gerenciador de memoria
        this.memoryManager = new MemoryManager();
        // cpu
        cpu = new CPU(m, memoryManager);
    }	
}
// ------------------- V M  - fim ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------