package tech.alyxbb.chip8;

public class ProgramCounter extends Register16 {
    public void increment(){
        this.setValue(this.getValue()+2);
    }
}
