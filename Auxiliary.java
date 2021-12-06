import java.util.LinkedList;

// -------------------------------------------  classes e funcoes auxiliares
public class Auxiliary {

    public static String dump(Word w) {
        String dump = "";
        dump += "[";
        dump += w.opc;
        dump += ", ";
        dump += w.r1;
        dump += ", ";
        dump += w.r2;
        dump += ", ";
        dump += w.p;
        dump += "]";
        return dump;
    }

    public static String dump(Word[] m, int ini, int fim) {
        String dump = "";
        for (int i = ini; i < fim; i++) {
            dump += i + ":" + dump(m[i]) + "\n";
        }
        return dump;
    }

    public static void load(Program program, Word[] m, LinkedList<Integer> pageTable) {
        Word[] programCode = program.getProgramCode();
        for (int i = 0; i < programCode.length; i++) {
            int physicalAddress = MemoryManager.translate(i, pageTable);
            m[physicalAddress].opc = programCode[i].opc;
            m[physicalAddress].r1 = programCode[i].r1;
            m[physicalAddress].r2 = programCode[i].r2;
            m[physicalAddress].p = programCode[i].p;
        }
    }
}
// ------------------------------------------- fim classes e funcoes auxiliares