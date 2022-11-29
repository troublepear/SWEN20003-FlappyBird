import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class PlasticPipe extends Pipe{

    // plastic pipe: image
    private final Image PLASTIC_PIPE = new Image("res/level/plasticPipe.png");

    // constructor
    public PlasticPipe(boolean hasLeveledUp) {
        super(hasLeveledUp);
    }

    // getters
    public Rectangle getTopBox(){
        return PLASTIC_PIPE.getBoundingBoxAt(new Point(getX(),getTopY()));
    }
    public Rectangle getBottomBox(){
        return PLASTIC_PIPE.getBoundingBoxAt(new Point(getX(),getBottomY()));
    }

    /** Method: to render the plastic pipe
     */
    public void renderPipe(){
        PLASTIC_PIPE.draw(getX(),getTopY());
        PLASTIC_PIPE.draw(getX(),getBottomY(),ROTATOR);
    }


}
