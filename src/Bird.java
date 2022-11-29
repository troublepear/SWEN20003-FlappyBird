import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bird {

    // bird images
    private Image currentBirdImage;
    private Image currentFlapImage;
    private final Image BIRD_LEVEL0 = new Image("res/level-0/birdWingDown.png");
    private final Image FLAP_LEVEL0 = new Image("res/level-0/birdWingUp.png");
    private final Image BIRD_LEVEL1 = new Image("res/level-1/birdWingDown.png");
    private final Image FLAP_LEVEL1 = new Image("res/level-1/birdWingUp.png");

    // bird position coordinates
    private final double X = 200;
    private final double INITIAL_Y = 350;
    private double y;

    // bird motion
    private final double FLY_SIZE = 6;
    private final double GRAVITY = 0.4;
    private final double MAX_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount = 0;
    private double velocity;
    private Rectangle boundingBox;

    // bird holding weapon status
    private boolean holding;

    // constructor
    public Bird() {
        this.y = INITIAL_Y;
        this.velocity = 0;
        this.currentBirdImage = BIRD_LEVEL0;
        this.currentFlapImage = FLAP_LEVEL0;
        this.boundingBox = currentBirdImage.getBoundingBoxAt(new Point(X,y));
        this.holding = false;
    }

    // getters
    public double getX() {
        return X;
    }
    public double getY() {
        return y;
    }
    public boolean getHoldingStatus(){
        return holding;
    }
    public Rectangle getBox() {
        return boundingBox;
    }

    /** Method: to mark bird as holding the weapon (make sure that bird only carries 1 weapon at a time)
     */
    public void holdWeapon(){
        holding = true;
    }

    /** Method: to mark bird as not holding the weapon (shows bird is available to pick up weapon)
     */
    public void dropWeapon(){
        holding = false;
    }

    /** Method: to reborn the bird at the initial position, i.e. set bird's y coordinate/velocity to the initial value
     */
    public void reborn() {
        y = INITIAL_Y;
        velocity = 0;
    }

    /** Method: to set the bird for game level 1, i.e. change bird image to level 1
     */
    public void setToLevel1(){
        currentBirdImage = BIRD_LEVEL1;
        currentFlapImage = FLAP_LEVEL1;
    }

    /** Method: to update bird's movement
     */
    public Rectangle update(Input input) {
        // frameCount add by 1 for every update()
        frameCount += 1;
        // for every space pressed, bird fly up 6 pixel
        if(input.wasPressed(Keys.SPACE)) {
            velocity = -FLY_SIZE;
            currentBirdImage.draw(X,y);
        }
        else {
            // velocity is the minimum value between (velocity + gravity) or 10
            velocity = Math.min(velocity + GRAVITY, MAX_VELOCITY);

            // if at every 10 frames, draw the bird flap
            if (frameCount % SWITCH_FRAME == 0) {
                currentFlapImage.draw(X,y);
                boundingBox = currentFlapImage.getBoundingBoxAt(new Point(X,y));
            }
            // otherwise, draw the bird
            else {
                currentBirdImage.draw(X,y);
                boundingBox = currentBirdImage.getBoundingBoxAt(new Point(X,y));
            }
        }
        // bird's y coordinate change according to current velocity
        y += velocity;

        return boundingBox;
    }


}
