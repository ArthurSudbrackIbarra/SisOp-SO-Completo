import java.util.HashMap;
import java.util.LinkedList;

public class ProcessManager {

    public static LinkedList<PCB> READY_LIST = new LinkedList<>();
    public static LinkedList<PCB> BLOCKED_LIST = new LinkedList<>();
    public static PCB RUNNING = null;

    private Word[] memory;

    private int idCounter;

    private static HashMap<Integer, String> programNamesMap = new HashMap<>();

    public ProcessManager(Word[] memory) {
        this.idCounter = 1;
        this.memory = memory;
    }

    public void createProcess(Program program) {

        LinkedList<Integer> tablePage = MemoryManager.alloc(program);

        if (tablePage == null) {
            System.out.println("Não foi possível criar um processo para o programa " + program.getName()
                    + " pois não há espaço suficiente na memória.");
            return;
        }

        int id = idCounter;
        int pc = 0;
        int[] reg = new int[9];

        PCB pcb = new PCB(id, pc, reg, tablePage);
        READY_LIST.add(pcb);

        Auxiliary.load(program, this.memory, tablePage);

        programNamesMap.put(id, program.getName());

        idCounter++;

        System.out.println(
                "Criado processo do programa: " + program.getName() + "\nTabela de páginas: " + tablePage + "\n");

        // Libera dispatcher se nao tem processo rodando.
        if (READY_LIST.size() == 1 && RUNNING == null) {
            Dispatcher.SEMA_DISPATCHER.release();
        }

    }

    public Word[] getMemory() {
        return memory;
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

    public static String getProgramNameByProcessId(int processId) {
        return programNamesMap.get(processId);
    }

}
