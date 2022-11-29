import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Rectangle;

import java.util.Random;

public abstract class Weapon {

    // weapon: y coordinate range
    private final int MAX_Y = 500;
    private final int MIN_Y = 100;

    // weapon: position coordinates
    private final double INITIAL_X = Window.getWidth();
    private int randomY = new Random().nextInt(MAX_Y - MIN_Y) + MIN_Y;
    private double x;
    private double y;

    // weapon: speed
    private final int INITIAL_SPEED = 5;
    private final int SHOOT_SPEED = -5;
    private double weaponSpeed;
    private final double TIMESCALE_RATE = 1.5;

    // weapon: status
    private boolean picked;
    private boolean shot;
    private boolean disappear;

    // weapon: frame
    private int frameCount;
    private int shootFrame;

    // constructor
    public Weapon() {
        this.x = INITIAL_X;
        this.y = randomY;
        this.picked = false;
        this.shot = false;
        this.disappear = false;
        this.frameCount = 0;
    }

    // getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public boolean getPicked(){
        return picked;
    }
    public boolean getShot(){return shot;}
    public boolean getDisappear(){return disappear;}
    public int getShootFrame(){
        return shootFrame;
    }
    public int getFrameCount(){
        return frameCount;
    }
    public abstract Rectangle getWeaponBox();

    /** Method: to set the weapon as picked, so we know it has already been picked
     */
    public void setPicked(){
        picked = true;
    }

    /** Method: to set the weapon as disappear (if out of shoot frame range)
     */
    public void setDisappear(){
        disappear = true;
    }

    /** Method: to update the picked weapon with the real-time bird coordinates (before it shoot)
     */
    public void holdByBird(double birdX, double birdY) {
        x = birdX;
        y = birdY;
    }

    /** Method: to render the weapon (abstract as different for rock/bomb)
     */
    public abstract void renderWeapon();

    /** Method: to update the weapon's movement
     */
    public void update(Input input,int timescale,Bird bird) {
        // add frameCount by 1 for every update()
        frameCount += 1;
        renderWeapon();
        // if the weapon is never picked: update the weapon speed according to current timescale
        if(!picked){
            weaponSpeed = INITIAL_SPEED * Math.pow(TIMESCALE_RATE, timescale);
        }
        // if the weapon has been picked:
        else{
            /* if pressed S: shoot the weapon
             * update the bird as not holding weapon, shot as true (as already shot)
             * mark weapon position and frame count at this shoot moment
             * update weapon speed as shoot speed (which is not affected by the timescale)
             */
            if(input.wasPressed(Keys.S)){
                bird.dropWeapon();
                x = bird.getBox().right();
                y = bird.getY();
                shot = true;
                shootFrame = frameCount;
                weaponSpeed = SHOOT_SPEED;
            }
        }
        // weapon x-coordinate moves accordingly
        x -= weaponSpeed;
    }



}