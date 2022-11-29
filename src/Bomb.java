import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bomb extends Weapon{

    // bomb: image and shoot frame
    private final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private final int SHOOT_FRAME = 50;

    // getter
    public Rectangle getWeaponBox() {
        return BOMB_IMAGE.getBoundingBoxAt(new Point(getX(),getY()));
    }

    /** Method: to render the bomb according to its status
     */
    public void renderWeapon(){
        // if the bomb is shot:
        if(getShot()){
            // within shoot frame range: render the bomb
            if((getFrameCount() - getShootFrame()) < SHOOT_FRAME){
                BOMB_IMAGE.draw(getX(),getY());
            }
            // after shoot frame range: set the bomb as should disappear (remove from the weapon list)
            else{
                setDisappear();
            }
        }
        else{
            BOMB_IMAGE.draw(getX(),getY());
        }
    }
}
