import java.util.LinkedList;

// -------------------------------------------  classes e funcoes auxiliares
public class Auxiliary {

    public static void dump(Word w) {
        System.out.print("[ "); 
        System.out.print(w.opc); System.out.print(", ");
        System.out.print(w.r1);  System.out.print(", ");
        System.out.print(w.r2);  System.out.print(", ");
        System.out.print(w.p);  System.out.println("  ] ");
    }

    public static void dump(Word[] m, int ini, int fim) {
        for (int i = ini; i < fim; i++) {
            System.out.print(i); System.out.print(":  ");  dump(m[i]);
        }
    
    }
    public static void load(Word[] programCode, Word[] m, LinkedList<Integer> pageTable) {
        for (int i = 0; i < programCode.length; i++) {
            int physicalAddress = MemoryManager.translate(i, pageTable);
            m[physicalAddress].opc = programCode[i].opc;
            m[physicalAddress].r1 = programCode[i].r1;
            m[physicalAddress].r2 = programCode[i].r2;
            m[physicalAddress].p = programCode[i].p;
        }
    }
}
// -------------------------------------------  fim classes e funcoes auxiliares