import java.util.LinkedList;

public class MemoryManager {

    public int memorySize;
    public int pageSize;

    private boolean[] frames;
    public int framesCount;

    // Falta saber quais instrucoes estao em cada frame

    public MemoryManager(int memorySize, int pageSize){
        this.memorySize = memorySize;
        this.pageSize = pageSize;
        // frames
        this.frames = new boolean[(int) Math.ceil((double) memorySize/pageSize)];     
        // colocando frames como livres
        for(int i = 0; i < frames.length; i++){
            frames[i] = true;
        }
        this.framesCount = frames.length;
    }

    public LinkedList<Integer> alloc(int instructionsNumber){

        int framesNeeded = (int) Math.ceil((double) instructionsNumber/pageSize);
        int framesAllocated = 0;

        boolean hasSpace = false;
        LinkedList<Integer> pageTable = new LinkedList<>();

        for(int i = 0; i < framesCount; i++){
            if(frames[i]){
                frames[i] = false;
                framesAllocated += 1;
                pageTable.add(i);
            }
            if(framesNeeded <= framesAllocated){
                // OK!
                hasSpace = true;
                break;
            }
        }

        if(hasSpace) return pageTable;
        return null;

    }

    public void destroy(LinkedList<Integer> pageTable){
        for(int i = 0; i < pageTable.size(); i++){
            frames[pageTable.get(i)] = true;
        }
    }

    public int translate(int logicAddress, LinkedList<Integer> pageTable){
        int pageIndex = logicAddress / pageSize;
        int offset = logicAddress % pageSize;
        int physicalAddress =  (pageTable.get(pageIndex) * pageSize) + offset;
        return physicalAddress;
    }

    public void printPageTable(LinkedList<Integer> pageTable){
        for(int i = 0; i < pageTable.size(); i++){
            System.out.println("[" + i + "] " + pageTable.get(i));
        }
    }
    
}
