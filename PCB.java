import java.util.LinkedList;

public class PCB {

    private int id;

    private int pc;
    private int[] reg;

    private LinkedList<Integer> tablePage;

    public PCB(int id, int pc, int[] reg, LinkedList<Integer> tablePage) {
        this.id = id;
        this.pc = pc;
        this.reg = reg;
        this.tablePage = tablePage;
    }

    public int getId() {
        return id;
    }

    public int getPc() {
        return pc;
    }

    public int[] getReg() {
        return reg;
    }

    public LinkedList<Integer> getTablePage() {
        return tablePage;
    }

}
