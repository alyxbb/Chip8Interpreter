package tech.alyxbb.chip8.screen;

public class CliScreen implements ScreenFacade {


    @Override
    public void render(boolean[][] pixels) {
        System.out.print("\033[H\033[2J");
        for (boolean[] row: pixels
             ) {
            for (boolean pixel:row
                 ) {
                if (pixel){
                    System.out.print("\033[0;47m ");
                }else {
                    System.out.print("\033[0;40m ");
                }
            }
            System.out.println("\033[0m");
        }
    }


}
