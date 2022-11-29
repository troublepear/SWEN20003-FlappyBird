import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Rectangle;

import java.util.Random;

public abstract class Pipe {

    // bottom pipe: rotator (defined protected as need to be accessed by subclass)
    protected final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private final double TIMESCALE_RATE = 1.5;

    // pipe set: gaps
    private final int PIPE_GAP = 168;
    private final int HIGH_GAP = 100;
    private final int MID_GAP = 300;
    private final int LOW_GAP = 500;
    private final int[] GAPS = {HIGH_GAP,MID_GAP,LOW_GAP};
    private int gapLevel0 = GAPS[new Random().nextInt(GAPS.length)] - MID_GAP;
    private int gapLevel1 = new Random().nextInt(LOW_GAP-HIGH_GAP) + HIGH_GAP - MID_GAP;

    // pipe set: initial y coordinates
    private final double INITIAL_TOP_Y = -PIPE_GAP/2.0;
    private final double INITIAL_BOTTOM_Y = Window.getHeight() + PIPE_GAP/2.0;

    // pipe set: coordinates
    private double x;
    private double topY;
    private double bottomY;

    // pipe set: speed
    private final double INITIAL_SPEED = 5;
    private double pipeSpeed = INITIAL_SPEED;

    // pipe set: status
    private boolean passed;

    // constructor
    public Pipe(boolean hasLeveledUp) {
        this.passed = false;
        this.x = Window.getWidth();
        if(hasLeveledUp) {
            this.topY = INITIAL_TOP_Y + gapLevel1;
            this.bottomY = INITIAL_BOTTOM_Y + gapLevel1;
        }
        else {
            this.topY = INITIAL_TOP_Y + gapLevel0;
            this.bottomY = INITIAL_BOTTOM_Y + gapLevel0;
        }
    }

    // getters
    public double getX() {
        return x;
    }
    public double getTopY() {
        return topY;
    }
    public double getBottomY(){
        return bottomY;
    }
    public boolean getPassed(){return passed;}
    public abstract Rectangle getTopBox();
    public abstract Rectangle getBottomBox();

    /** Method: to mark the pipe as passed (so it would not be checked again)
     */
    public void markPassed(){
        passed = true;
    }

    /** Method: to render the pipe (abstract as different for plastic/steel pipe)
     */
    public abstract void renderPipe();

    /** Method: to update pipe movement according to the real-time timescale
     */
    public void update(int timescale) {
        renderPipe();
        x -= INITIAL_SPEED * Math.pow(TIMESCALE_RATE,timescale);
    }

}
