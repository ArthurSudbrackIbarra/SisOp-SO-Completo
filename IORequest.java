public class IORequest {

    public static enum OperationTypes {
        READ, WRITE
    }

    private PCB process;
    private OperationTypes operationType;

    public IORequest(PCB process, OperationTypes operationType) {
        this.process = process;
        this.operationType = operationType;
    }

    public PCB getProcess() {
        return process;
    }

    public OperationTypes getOperationType() {
        return operationType;
    }

}
