import java.util.HashMap;
import java.util.LinkedList;

public class ProcessManager {

    public static LinkedList<PCB> READY_LIST = new LinkedList<>();
    public static LinkedList<PCB> BLOCKED_LIST = new LinkedList<>();
    public static PCB RUNNING = null;

    private int idCounter;

    private HashMap<Integer, String> programNamesMap;

    public ProcessManager() {
        this.idCounter = 1;
        this.programNamesMap = new HashMap<>();
    }

    public boolean createProcess(Word[] m, Program program) {

        LinkedList<Integer> tablePage = MemoryManager.alloc(program);

        if (tablePage == null)
            return false;

        int id = idCounter;
        int pc = 0;
        int[] reg = new int[9];

        PCB pcb = new PCB(id, pc, reg, tablePage);
        READY_LIST.add(pcb);

        Auxiliary.load(program, m, tablePage);

        programNamesMap.put(id, program.getName());

        idCounter++;

        // Libera dispatcher se nao tem processo rodando.
        if (RUNNING == null) {
            Dispatcher.SEMA_DISPATCHER.release();
        }

        return true;

    }

    public void destroyProcess(int id) {
        READY_LIST.removeIf((PCB pcb) -> pcb.getId() == id);
    }

}
