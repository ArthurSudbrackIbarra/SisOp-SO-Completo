import java.util.LinkedList;

public class PCB {

    private int id;

    private int pc;
    private int[] reg;

    private LinkedList<Integer> tablePage;

    private int ioValue;

    public PCB(int id, int pc, int[] reg, LinkedList<Integer> tablePage) {
        this.id = id;
        this.pc = pc;
        this.reg = reg;
        this.tablePage = tablePage;
        this.ioValue = -1;
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

    public int getIOValue() {
        return ioValue;
    }

    public void setIOValue(int ioValue) {
        this.ioValue = ioValue;
    }

    public LinkedList<Integer> getTablePage() {
        return tablePage;
    }

}
