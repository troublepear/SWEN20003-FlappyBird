import bagel.Input;
import bagel.Keys;

public class Timescale {

    // timescale range
    private final int MAX_TIMESCALE = 5;
    private final int MIN_TIMESCALE = 1;

    // current timescale (initial at 1)
    private final int INITIAL_TIMESCALE = 1;
    private int timescale = INITIAL_TIMESCALE;

    // getter
    public int getTimescale(){
        return timescale;
    }

    /** Method: to update the real-time timescale according to the key pressed (K or L)
     */
    public void update(Input input){
        if(input.wasPressed(Keys.L) && timescale < MAX_TIMESCALE) {
            timescale += 1;
        }
        if(input.wasPressed(Keys.K) && timescale > MIN_TIMESCALE){
            timescale -= 1;
        }
    }

}