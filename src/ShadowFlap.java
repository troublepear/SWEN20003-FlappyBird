import bagel.*;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @Author: SUIJUAN WANG - 825717
 */
public class ShadowFlap extends AbstractGame {

    // objects
    private final Level0 level0;
    private final Level1 level1;

    public ShadowFlap() {
        super(1024,768,"TroubleBird via.troublepear");
        level0 = new Level0();
        level1 = new Level1();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        boolean hasLeveledUp = level0.getHasFinishedLevel0();

        if(input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if(hasLeveledUp){
            level1.update(input);
        }
        else{
            level0.update(input);
        }

    }


}
