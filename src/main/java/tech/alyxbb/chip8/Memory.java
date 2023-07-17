package tech.alyxbb.chip8;

import java.io.FileInputStream;
import java.io.IOException;

public class Memory {
    byte[] memory = new byte[0x1000];

    public byte getValue(int position){
        return memory[position];
    }
    public void setValue(int position,byte value){
        memory[position]=value;
    }
    public short getShort(int poistion){
        return (short)((memory[poistion]<<8)|(Byte.toUnsignedInt(memory[poistion+1])));
    }
    
    
    public Memory(String fileLocation) {
        loadFile(fileLocation);
        loadFont();
    }
    
    public void loadFile(String location){
        try(            FileInputStream file = new FileInputStream(location)
        ) {
            short memOffset = 0x0200;
            int nextByte=file.read();
            do {
                memory[memOffset]=(byte) nextByte;
                memOffset++;
                nextByte=file.read();
            } while (nextByte!=-1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void loadFont(){
        short[] font = {
                0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
                0x20, 0x60, 0x20, 0x20, 0x70, // 1
                0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
                0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
                0x90, 0x90, 0xF0, 0x10, 0x10, // 4
                0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
                0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
                0xF0, 0x10, 0x20, 0x40, 0x40, // 7
                0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
                0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
                0xF0, 0x90, 0xF0, 0x90, 0x90, // A
                0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
                0xF0, 0x80, 0x80, 0x80, 0xF0, // C
                0xE0, 0x90, 0x90, 0x90, 0xE0, // D
                0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
                0xF0, 0x80, 0xF0, 0x80, 0x80  // F
        };
        short fontStart=0x50;
        for (int i = 0; i <font.length ; i++) {
            memory[fontStart+i]=(byte) font[i];
        }
    }
        
}
