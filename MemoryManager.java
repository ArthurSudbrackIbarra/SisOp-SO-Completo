import java.util.LinkedList;

public class MemoryManager {

    private static boolean[] frames;

    static {
        // frames
        frames = new boolean[(int) Math.ceil((double) MySystem.MEMORY_SIZE / MySystem.PAGE_SIZE)];
        // colocando frames como livres
        for (int i = 0; i < frames.length; i++) {
            frames[i] = true;
        }
    }

    public static LinkedList<Integer> alloc(Program program) {

        int instructionsNumber = program.getProgramCode().length;

        int framesNeeded = (int) Math.ceil((double) instructionsNumber / MySystem.PAGE_SIZE);
        int framesAllocated = 0;

        boolean hasSpace = false;
        LinkedList<Integer> pageTable = new LinkedList<>();

        for (int i = 0; i < frames.length; i++) {
            if (frames[i]) {
                frames[i] = false;
                framesAllocated += 1;
                pageTable.add(i);
            }
            if (framesNeeded <= framesAllocated) {
                // OK!
                hasSpace = true;
                break;
            }
        }

        if (hasSpace)
            return pageTable;

        return null;

    }

    public static void dealloc(LinkedList<Integer> pageTable) {
        for (int i = 0; i < pageTable.size(); i++) {
            frames[pageTable.get(i)] = true;
        }
    }

    public static int translate(int logicAddress, LinkedList<Integer> pageTable) {
        int pageIndex = pageOfPc(logicAddress);
        int offset = logicAddress % MySystem.PAGE_SIZE;
        int physicalAddress = (pageTable.get(pageIndex) * MySystem.PAGE_SIZE) + offset;
        return physicalAddress;
    }

    public static int pageOfPc(int logicAddress) {
        int pageIndex = logicAddress / MySystem.PAGE_SIZE;
        return pageIndex;
    }

    public static void printPageTable(LinkedList<Integer> pageTable) {
        for (int i = 0; i < pageTable.size(); i++) {
            System.out.println("[" + i + "] " + pageTable.get(i));
        }
    }

}
