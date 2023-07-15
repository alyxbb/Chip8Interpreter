package tech.alyxbb.chip8;

public class Register16 {    
    private Short value;
    
    public short getValue(){
        return value;
    }
    public int getIntValue(){
        return Short.toUnsignedInt(value);
    }
    public void setValue(int value){
        this.value = (short) value;
    }
    public void setValue(short value){
        this.value= value;
    }
}
