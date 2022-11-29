import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SteelPipe extends Pipe{

    // steel pipe: image
    private final Image STEEL_PIPE = new Image("res/level-1/steelPipe.png");

    // flame: image, gap above the steel pipe, duration and render frequency
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final int ABOVE_PIPE_GAP = 10;
    private final int FLAME_DURATION = 3;
    private final int FLAME_FREQ = 20;

    // frame count
    private int frameCount = 0;

    // constructor
    public SteelPipe(boolean hasLeveledUp) {
        super(hasLeveledUp);
    }

    // getters: pipes and flames
    public Rectangle getTopBox(){
        return STEEL_PIPE.getBoundingBoxAt(new Point(getX(),getTopY()));
    }
    public Rectangle getBottomBox(){
        return STEEL_PIPE.getBoundingBoxAt(new Point(getX(),getBottomY()));
    }
    public Rectangle getTopFlameBox(){
        return FLAME_IMAGE.getBoundingBoxAt(new Point(getX(),getTopBox().bottom() + ABOVE_PIPE_GAP));
    }
    public Rectangle getBottomFlameBox(){
        return FLAME_IMAGE.getBoundingBoxAt(new Point(getX(),getBottomBox().top() - ABOVE_PIPE_GAP));
    }

    /** Method: to render the plastic pipe and the flame
     */
    public void renderPipe() {
        // frame count add by 1 for every update()
        frameCount += 1;
        STEEL_PIPE.draw(getX(),getTopY());
        STEEL_PIPE.draw(getX(),getBottomY(),ROTATOR);

        // if the remainder of the frame count with flame frequency is between 0 and 3, render the flame
        int frameRemainder = frameCount % FLAME_FREQ;
        if(0 <= frameRemainder && frameRemainder < FLAME_DURATION){
            shootFlame();
        }
    }

    /** Method: to render(shoot) the flame
     */
    public void shootFlame(){
        FLAME_IMAGE.draw(getX(),getTopBox().bottom() + ABOVE_PIPE_GAP);
        FLAME_IMAGE.draw(getX(),getBottomBox().top() - ABOVE_PIPE_GAP,ROTATOR);
    }


}
