import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Rock extends Weapon {

    // rock: image and shoot frame
    private final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private final int SHOOT_FRAME = 25;

    // getter
    public Rectangle getWeaponBox() {
        return ROCK_IMAGE.getBoundingBoxAt(new Point(getX(),getY()));
    }

    /** Method: to render the rock according to its status
     */
    public void renderWeapon(){
        // if the rock is shot:
        if(getShot()){
            // within shoot frame range: render the rock
            if((getFrameCount() - getShootFrame()) < SHOOT_FRAME){
                ROCK_IMAGE.draw(getX(),getY());
            }
            // after shoot frame range: set the rock as should disappear (remove from the weapon list)
            else{
                setDisappear();
            }
        }
        // if the rock is not shot: render the rock as normal
        else{
            ROCK_IMAGE.draw(getX(),getY());
        }
    }
}


