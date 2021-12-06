import java.util.HashMap;
import java.util.LinkedList;

public class ProcessManager {

    public static LinkedList<PCB> READY_LIST = new LinkedList<>();
    public static LinkedList<PCB> BLOCKED_LIST = new LinkedList<>();
    public static PCB RUNNING = null;

    private Word[] memory;

    private int idCounter;

    private HashMap<Integer, String> programNamesMap;

    public ProcessManager(Word[] memory) {
        this.idCounter = 1;
        this.programNamesMap = new HashMap<>();
        this.memory = memory;
    }

    public void createProcess(Program program) {

        LinkedList<Integer> tablePage = MemoryManager.alloc(program);

        if (tablePage == null)
            System.out.println("Não foi possível criar o processo pois não há espaço o suficiente na memória.");

        int id = idCounter;
        int pc = 0;
        int[] reg = new int[9];

        PCB pcb = new PCB(id, pc, reg, tablePage);
        READY_LIST.add(pcb);

        Auxiliary.load(program, this.memory, tablePage);

        programNamesMap.put(id, program.getName());

        idCounter++;

        // Libera dispatcher se nao tem processo rodando.
        if (READY_LIST.size() == 1 && RUNNING == null) {
            Dispatcher.SEMA_DISPATCHER.release();
        }

    }

    public static PCB findBlockedProcessById(int id) {
        for (int i = 0; i < BLOCKED_LIST.size(); i++) {
            if (BLOCKED_LIST.get(i).getId() == id) {
                return BLOCKED_LIST.remove(i);
            }
        }
        return null;
    }

    public static void destroyProcess(int processId, LinkedList<Integer> pageTable) {
        MemoryManager.dealloc(pageTable);
        for (int i = 0; i < READY_LIST.size(); i++) {
            if (READY_LIST.get(i).getId() == processId) {
                READY_LIST.remove(i);
            }
        }
        for (int i = 0; i < BLOCKED_LIST.size(); i++) {
            if (BLOCKED_LIST.get(i).getId() == processId) {
                BLOCKED_LIST.remove(i);
            }
        }
    }

}
