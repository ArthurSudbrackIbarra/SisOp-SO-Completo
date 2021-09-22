import java.util.HashMap;
import java.util.LinkedList;

public class ProcessManager {

    private Auxiliary aux;
    private MemoryManager memoryManager;

    private LinkedList<PCB> processList;

    private HashMap<Integer, String> programNamesMap;

    public ProcessManager(MemoryManager memoryManager){
        this.aux = new Auxiliary(memoryManager);
        this.memoryManager = memoryManager;
        this.processList = new LinkedList<>();
        this.programNamesMap = new HashMap<>();
    }

    public boolean createProcess(Word[] m, Program program){

        Word[] programCode = program.getProgramCode();

        LinkedList<Integer> tablePage = memoryManager.alloc(programCode.length);

        if(tablePage == null) return false;

        int id = processList.size() + 1;
        int pc = 0;
        int[] reg = new int[9];

        PCB pcb = new PCB(id, pc, reg, tablePage);
        processList.add(pcb);

        aux.loadToMemory(programCode, m, tablePage);

        programNamesMap.put(id, program.getName());

        return true;

    }

    public void destroyProcess(int id){
        processList.removeIf((PCB pcb) -> pcb.getId() == id);
    }

    public PCB nextProcess(){
        return processList.removeFirst();
    }

    public void runAllProcesses(){

    }
    
}
