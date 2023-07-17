package tech.alyxbb.chip8;

import tech.alyxbb.chip8.screen.Display;
import tech.alyxbb.chip8.screen.ScreenController;

import java.util.Stack;

public class Interpreter {
    Memory memory ;
    Register[] registers = new Register[16];
    Stack<Short> stack = new Stack<>();
    ProgramCounter pc = new ProgramCounter();
    Register16 iReg = new Register16();
    
    ScreenController screenController = new ScreenController();
    public static void main(String[] args) {
        new Interpreter(args);
    }

    private void init(String[] args) {
        if(args.length==0){
            throw new RuntimeException("Please pass the file name as a string");
        }
        memory = new Memory(args[0]);
        
        for (int i = 0; i < registers.length; i++) {
            registers[i]=new Register();    
        }
        
        pc.setValue(0x200);
    }
    private void run(){
        while(true){
        int instruction = Short.toUnsignedInt(fetch());
        short subInst = (short) (instruction&0xFFF);
        byte nibble2 =(byte) (subInst>>>8);
        byte nibble3 = (byte) ((subInst>>>4)&0xF);
        byte nibble4 = (byte) (subInst&0xF);
        byte endByte = (byte) (subInst&0xFF);
            switch (instruction >>> 12) {
                case 0x0 -> {
                    if (subInst == 0x0E0) { // clear screen
                        screenController.clearScreen();
                    }
                }
                case 0x1 -> // jump
                        pc.setValue(subInst);
                case 0x6 -> // set
                        registers[nibble2].setValue(endByte);
                case 0x7 -> // add
                        registers[nibble2].setValue(registers[nibble2].getValue() + endByte);
                case 0xA -> // set index
                        iReg.setValue(subInst);
                case 0xD -> { // display
                    byte x = (byte) (registers[nibble2].getShortValue() % Display.WIDTH);
                    byte y = (byte) (registers[nibble3].getShortValue() % Display.HEIGHT);
                    int index = iReg.getIntValue();
                    byte[] image = new byte[nibble4];
                    for (int i = 0; i < image.length; i++) {
                        image[i] = memory.getValue(index + i);
                    }
                    screenController.draw(image, x, y);
                }
            }
    }
    }

    private short fetch() {
        short instruction = memory.getShort(pc.getIntValue());
        pc.increment();
        return instruction;
    }

    public Interpreter(String[] args){
      init(args) ; 
      run();
    }
}
