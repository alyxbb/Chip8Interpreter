package tech.alyxbb.chip8.screen;

public class Display {
    public static byte WIDTH = 64;
    public static byte HEIGHT = 32;
    public boolean[][] getDisplay() {
        return display;
    }

    public void setPixel(byte x, byte y,boolean value) {
        this.display[y][x]=value;
    }
    public boolean getPixel(byte x, byte y){
        return this.display[y][x];
    }

    private final boolean[][] display = new boolean[HEIGHT][WIDTH];
    
}
