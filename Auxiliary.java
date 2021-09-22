import java.util.LinkedList;

// -------------------------------------------  classes e funcoes auxiliares
public class Auxiliary {

    private MemoryManager memoryManager;

    public Auxiliary(MemoryManager memoryManager){ // MemoryManager no construtor
        this.memoryManager = memoryManager;
    }

    public void dump(Word w) {
        System.out.print("[ "); 
        System.out.print(w.opc); System.out.print(", ");
        System.out.print(w.r1);  System.out.print(", ");
        System.out.print(w.r2);  System.out.print(", ");
        System.out.print(w.p);  System.out.println("  ] ");
    }
    public void dump(Word[] m, int ini, int fim) {
        for (int i = ini; i < fim; i++) {
            System.out.print(i); System.out.print(":  ");  dump(m[i]);
        }
    }
    public LinkedList<Integer> carga(Word[] p, Word[] m) {
        LinkedList<Integer> pageTable = memoryManager.alloc(p.length);
        if(pageTable == null) return null;
        for (int i = 0; i < p.length; i++) {
            int physicalAddress = memoryManager.translate(i, pageTable);
            m[physicalAddress].opc = p[i].opc;
            m[physicalAddress].r1 = p[i].r1;
            m[physicalAddress].r2 = p[i].r2;
            m[physicalAddress].p = p[i].p;
        }
        return pageTable;
    }
}
// -------------------------------------------  fim classes e funcoes auxiliares