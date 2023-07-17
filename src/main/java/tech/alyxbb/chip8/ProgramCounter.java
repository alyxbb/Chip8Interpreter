package tech.alyxbb.chip8;

public class ProgramCounter extends Register16 {
    public static final int INSTRUCTION_SIZE=2;
    public void increment(){
        this.setValue(this.getValue()+INSTRUCTION_SIZE);
    }
}
