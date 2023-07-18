package tech.alyxbb.chip8;

import tech.alyxbb.chip8.screen.Display;
import tech.alyxbb.chip8.screen.ScreenController;

import java.util.Random;
import java.util.Stack;

public class Interpreter {
    Memory memory ;
    Register[] registers = new Register[16];
    Stack<Short> stack = new Stack<>();
    ProgramCounter pc = new ProgramCounter();
    Register16 iReg = new Register16();
    
    ScreenController screenController = new ScreenController();
    
    Random randomizer = new Random();
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
        
        pc.setValue(Memory.PROGRAM_LOCATION);
    }
    private void run(){
        while(true){
        int instruction = Short.toUnsignedInt(fetch());
        short last3Nibbles = (short) (instruction&0xFFF);
        byte nibble1 = (byte) (instruction>>>12);
        byte nibble2 = (byte) (last3Nibbles>>>8);
        byte nibble3 = (byte) ((last3Nibbles>>>4)&0xF);
        byte nibble4 = (byte) (last3Nibbles&0xF);
        byte endByte = (byte) (last3Nibbles&0xFF);
            switch (nibble1) {
                case 0x0 -> {
                    if (last3Nibbles == 0x0E0) { // clear screen
                        screenController.clearScreen();
                    } else if (last3Nibbles==0x0EE) { // return
                        pc.setValue(stack.pop());
                    }
                }
                case 0x1 -> // jump
                        pc.setValue(last3Nibbles);
                case 0x2 -> { // subroutine
                    stack.push(pc.getValue());
                    pc.setValue(last3Nibbles);
                }
                case 0x3 -> { // skip if equal
                    if (registers[nibble2].getValue()==endByte){
                        pc.increment();
                    }
                }
                case 0x4 -> {//skip if not equal
                    if (registers[nibble2].getValue()!=endByte){
                        pc.increment();
                    }
                }
                case 0x5 -> {// skip if equal
                    if (registers[nibble2].getValue()==registers[nibble3].getValue()){
                        pc.increment();
                    }
                }
                case 0x9 -> { // skip if not equal
                    if (registers[nibble2].getValue()!=registers[nibble3].getValue()){
                        pc.increment();
                    }
                }
                case 0x6 -> // set
                        registers[nibble2].setValue(endByte);
                case 0x7 -> // add
                        registers[nibble2].setValue(registers[nibble2].getValue() + endByte);
                case 0x8 -> {//logic and maths
                    switch (nibble4){
                        case 0x0 ->// set
                            registers[nibble2].setValue(registers[nibble3].getValue());
                        case 0x1 ->// or
                            registers[nibble2].setValue(registers[nibble2].getValue()|registers[nibble3].getValue());
                        case 0x2 -> // and
                            registers[nibble2].setValue(registers[nibble2].getValue()&registers[nibble3].getValue());
                        case 0x3 -> // xor
                            registers[nibble2].setValue(registers[nibble2].getValue()^registers[nibble3].getValue());
                        case 0x4 -> {//add
                            int total = registers[nibble2].getShortValue()+registers[nibble3].getShortValue();
                            if (total>0xFF){
                                registers[0xF].setValue(1);
                            } else {
                                registers[0xF].setValue(0);
                            }
                            registers[nibble2].setValue(total);
                        }
                        case 0x5 ->{// subtract
                            int total = registers[nibble2].getShortValue()-registers[nibble3].getShortValue();
                            if (total>=0){
                                registers[0xF].setValue(1);
                            } else {
                                registers[0xF].setValue(0);
                            }
                            registers[nibble2].setValue(total);
                        }
                        case 0x7 ->{ //subtract flipped
                            int total = registers[nibble3].getShortValue()-registers[nibble2].getShortValue();
                            if (total>=0){
                                registers[0xF].setValue(1);
                            } else {
                                registers[0xF].setValue(0);
                            }
                            registers[nibble2].setValue(total);
                        }
                        
                        case 0x6 ->{// right shift TODO add option for ambiguity
                            //registers[nibble2].setValue(registers[nibble3].getValue());
                            registers[0xF].setValue(registers[nibble2].getValue()&0b0000_0001);
                            registers[nibble2].setValue(registers[nibble2].getShortValue()>>1);
                        }
                        case 0xE -> { // left shift
                            //registers[nibble2].setValue(registers[nibble3].getValue());
                            registers[0xF].setValue((registers[nibble2].getShortValue()&0b1000_0000)/0b1000_0000);
                            registers[nibble2].setValue(registers[nibble2].getShortValue()<<1);
                        }

                    }
                    
                }
                case 0xA -> // set index
                        iReg.setValue(last3Nibbles);
                case 0xB ->  //jump with offset TODO add support for ambiguous behaviour.
                    pc.setValue(last3Nibbles+registers[0].getValue());
                case 0xC ->{ // random
                    byte randNo = (byte) (randomizer.nextInt()&endByte);
                    registers[nibble2].setValue(randNo);
                }
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
                case 0xF -> {
                    switch (endByte){
                        case 0x1E -> {// add to index
                            iReg.setValue(iReg.getIntValue()+registers[nibble2].getShortValue());
                            if (iReg.getIntValue()>=0x1000){
                                registers[0xF].setValue(1);
                            }
                        }
                        case 0x29 -> { // get font char
                            int charNeeded = registers[nibble2].getShortValue();
                            iReg.setValue(Memory.FONT_LOCATION+(Memory.FONT_HEIGHT * charNeeded));
                        }
                        case 0x33 -> { // to bcd
                            int binary = registers[nibble2].getShortValue();
                            memory.setValue(iReg.getIntValue(), (byte) (binary/100));
                            memory.setValue(iReg.getIntValue()+1, (byte)((binary/10)%10));
                            memory.setValue(iReg.getIntValue()+2,(byte)(binary%10));
                        }
                        case 0x55 -> {// store multiple TODO add support for ambiguous instruction 0x55 and 0x65
                            for (int i = 0; i <= nibble2; i++) {
                                memory.setValue(iReg.getIntValue()+i, registers[i].getValue());
                            }
                        }
                        case 0x65 -> {
                            for (int i = 0; i <= nibble2; i++) {
                                registers[i].setValue(memory.getValue(iReg.getIntValue()+i));
                            }
                        }
                            
                    }
                    
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
