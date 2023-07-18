package tech.alyxbb.chip8.screen;

public class CliScreen implements ScreenFacade {

    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final String WHITE_BG = "\033[0;47m";
    private static final String BLACK_BG =  "\033[0;40m";
    private static final String CLEAR_FORMATTING = "\033[0m";
    @Override
    public void render(boolean[][] pixels) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(CLEAR_SCREEN);
        for (boolean[] row: pixels
             ) {
            for (boolean pixel:row
                 ) {
                if (pixel){
                    toPrint.append(WHITE_BG + " ");
                }else {
                    toPrint.append(BLACK_BG + " ");
                }
            }
            toPrint.append(CLEAR_FORMATTING + "\n");
        }
        System.out.println(toPrint.toString());
    }


}
