import java.util.HashMap;
import java.util.LinkedList;

public class ProcessManager {

    private LinkedList<PCB> processList;
    private int idCounter;

    private HashMap<Integer, String> programNamesMap;

    public ProcessManager(){
        this.processList = new LinkedList<>();
        this.idCounter = 1;
        this.programNamesMap = new HashMap<>();
    }

    public boolean createProcess(Word[] m, Program program){

        Word[] programCode = program.getProgramCode();

        LinkedList<Integer> tablePage = MemoryManager.alloc(programCode.length);

        if(tablePage == null) return false;

        int id = idCounter;
        int pc = 0;
        int[] reg = new int[9];

        PCB pcb = new PCB(id, pc, reg, tablePage);
        processList.add(pcb);

        Auxiliary.load(programCode, m, tablePage);

        programNamesMap.put(id, program.getName());

        idCounter++;

        return true;

    }

    public void destroyProcess(int id){
        processList.removeIf((PCB pcb) -> pcb.getId() == id);
    }

    public void runAllProcesses(CPU cpu){

        int i = 0;

        while(!processList.isEmpty()){

            if(i >= processList.size()){
                i = 0;
            }

            PCB currentProcess = processList.get(i);
            cpu.loadPCB(currentProcess);

            System.out.println("\nAGORA RODANDO O PROCESSO: " + programNamesMap.get(currentProcess.getId()) + "\n");

            boolean processHasEnded = cpu.run();

            if(processHasEnded){
                destroyProcess(currentProcess.getId());
                MemoryManager.desalloc(currentProcess.getTablePage());
                i--;
            } else {
                processList.set(i, cpu.unloadPCB());
            }

            i++;

        }

    }
    
}
