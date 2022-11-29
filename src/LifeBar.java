import bagel.Image;

public class LifeBar {

    // life bar: image
    private final Image FULL_LIFE = new Image("res/level/fullLife.png");
    private final Image NO_LIFE = new Image("res/level/noLife.png");

    // life bar: coordinates and gap
    private final int FIRST_X = 100;
    private final int FIRST_Y = 15;
    private final int LIFE_GAP = 50;

    // life bar: life
    private final int MAX_LIFE_LEVEL0 = 3;
    private final int MAX_LIFE_LEVEL1 = 6;
    private int currentLife;
    private int currentMaxLife = MAX_LIFE_LEVEL0;

    // getter
    public int getCurrentMaxLife(){
        return currentMaxLife;
    }

    /** Method: to set the life bar to level 1
     */
    public void setToLevel1(){
        currentMaxLife = MAX_LIFE_LEVEL1;
    }

    /** Method: to render the life bar
     */
    public void renderLifeBar() {
        for(int i=0;i<currentMaxLife;i++) {
            if(i < currentLife) {
                FULL_LIFE.drawFromTopLeft(FIRST_X + i*LIFE_GAP,FIRST_Y);
            }
            else{
                NO_LIFE.drawFromTopLeft(FIRST_X + i*LIFE_GAP,FIRST_Y);
            }
        }
    }

    /** Method: to update the real-time life bar
     */
    public void update(int life) {
        currentLife = life;
        renderLifeBar();
    }

}
