package tech.alyxbb.chip8.screen;

public class ScreenController {
    private final ScreenFacade screen = new CliScreen();
    private Display display = new Display();
    
    public void clearScreen(){
        display = new Display();
        screen.render(display.getDisplay());
    }
    
    public void draw(byte[] toDraw,byte x,byte y){
        byte currentY = y;
        for (byte row:
                toDraw) {
            if(currentY>=Display.HEIGHT){
                break;
            }
            int mask = 0b1000_0000;
            for (byte currentX = x; currentX < x+8; currentX++) {
                if (currentX>=Display.WIDTH){
                    break;
                }
                if ((row&mask)!=0){
                    display.setPixel(currentX,currentY,!display.getPixel(currentX,currentY));
                }
                mask >>>= 1;
                
            }
            currentY++;
        }
        screen.render(display.getDisplay());
    }
    
    
}
