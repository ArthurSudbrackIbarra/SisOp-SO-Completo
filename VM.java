// ------------------- V M  - constituida de CPU e MEMORIA -----------------------------------------------
// -------------------------- atributos e construcao da VM -----------------------------------------------
public class VM {
    public int tamMem;    
    public Word[] m;     
    public CPU cpu;    

    public VM(MySystem system){   // vm deve ser configurada com endereço de tratamento de interrupcoes
        // memória
        tamMem = 1024;
        m = new Word[tamMem]; // m ee a memoria
        for (int i=0; i<tamMem; i++) { m[i] = new Word(Opcode.___,-1,-1,-1); };
        // cpu
        cpu = new CPU(m, system);
    }	
}
// ------------------- V M  - fim ------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------