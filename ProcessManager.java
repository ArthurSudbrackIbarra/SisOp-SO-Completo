import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ProcessManager {

    private Auxiliary aux;
    private MemoryManager memoryManager;

    private LinkedList<PCB> processList;

    public ProcessManager(MemoryManager memoryManager){
        this.aux = new Auxiliary(memoryManager);
        this.memoryManager = memoryManager;
        this.processList = new LinkedList<>();
    }

    public boolean createProcess(Word[] m, Word[] program){

        LinkedList<Integer> tablePage = memoryManager.alloc(program.length);

        if(tablePage == null) return false;

        int id = processList.size() + 1;
        int pc = 0;
        int[] reg = new int[9];

        PCB pcb = new PCB(id, pc, reg, tablePage);
        processList.add(pcb);

        aux.carga(m, program);

        return true;

    }

    public void destroyProcess(int id){
        processList.removeIf((PCB pcb) -> pcb.getId() == id);
    }
    
}
