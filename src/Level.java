import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Level {

    // window size (for convenience)
    protected final int WIDTH = Window.getWidth();
    protected final int HEIGHT = Window.getHeight();

    // both level: messages
    protected final int FONT_SIZE = 48;
    protected final Font FONT = new Font("res/font/slkscr.ttf",FONT_SIZE);
    protected final double MSG_HEIGHT = (HEIGHT-FONT_SIZE) /2.0;
    protected final int MSG_GAP = 75;
    protected final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    protected final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final Point SCORE_COOR = new Point(100,100);
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String SCORE_MSG = "SCORE: ";

    // both level: frame related
    protected final int INITIAL_PIPE_REGENERATE_FRAME = 100;
    protected final double TIMESCALE_RATE = 1.5;


    /** Method: to render the real-time score
     */
    public void renderScoreCounter(int score){
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg,SCORE_COOR.x,SCORE_COOR.y);
    }

    /** Method: to render different screens
     */
    public abstract void renderInstructionScreen(Input input);

    public void renderGameOverScreen(int score){
        FONT.drawString(GAME_OVER_MSG,(WIDTH-FONT.getWidth(GAME_OVER_MSG))/2.0,MSG_HEIGHT);
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg,(WIDTH-FONT.getWidth(finalScoreMsg))/2.0,MSG_HEIGHT+MSG_GAP);
    }

    /** Method: to detect collisions (3 rectangle parameter)
     */
    public boolean detectCollision(Rectangle anyBox, Rectangle aBox, Rectangle bBox){
        return anyBox.intersects(aBox) || anyBox.intersects(bBox);
    }

    /** Method: to detect collisions (2 rectangle parameter)
     */
    public boolean detectCollision(Rectangle aBox, Rectangle bBox){
        return aBox.intersects(bBox);
    }

    /** Method: to detect if the bird is out-of-bound
     */
    public boolean detectOutOfBound(double birdY) {
        return (birdY > HEIGHT || birdY < 0);
    }

    public int scaleFreq(int initialFreq,int timescale){
        return (int) (initialFreq / Math.pow(TIMESCALE_RATE,timescale));
    }

}
