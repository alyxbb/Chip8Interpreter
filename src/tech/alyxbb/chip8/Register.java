package tech.alyxbb.chip8;

public class Register {
    private byte value;
    
    public byte getValue(){
        return value;
    }
    public short getShortValue(){
        return (short) Byte.toUnsignedInt(value);
    }
    public void setValue(byte value){
        this.value = value;
    }
    public void setValue(short value){
        this.value = (byte) value;
    }
}
