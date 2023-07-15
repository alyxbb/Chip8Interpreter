package tech.alyxbb.chip8;

import java.util.Stack;

public class Interpreter {
    Memory memory ;
    Register[] registers = new Register[16];
    Stack<Short> stack = new Stack<>();
    Register16 pc = new Register16();
    Register16 iReg = new Register16();
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
    }
    public void run(){
        
    }
    public Interpreter(String[] args){
      init(args) ; 
      run();
    }
}
