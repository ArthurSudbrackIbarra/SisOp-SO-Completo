public class Program {

    private String name;
    private Word[] programCode;

    public Program(String name, Word[] programCode) {
        this.name = name;
        this.programCode = programCode;
    }

    public String getName() {
        return name;
    }

    public Word[] getProgramCode() {
        return programCode;
    }

}
