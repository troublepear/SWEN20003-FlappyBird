import bagel.*;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class Level0 extends Level {

    // level 0: background image
    private final Image BACKGROUND = new Image("res/level-0/background.png");

    // level 0: messages
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final int LEVEL_UP_SCORE = 5;
    private final int LEVEL_UP_DURATION = 150;

    // level 0: frame related
    private int totalFrameCount = 0;
    private int frameCount = 0;
    private int currentFrameCount;
    private int pipe_regenerate_frame;

    // level 0: boolean
    private boolean hasStarted = false;
    private boolean hasLeveledUp = false;
    private boolean hasFinished = false;
    private boolean hasFinishedLevel0 = false;
    private boolean recorded = false;
    private boolean collision = false;

    private int currentLife;
    private int currentTimescale;
    private int score = 0;

    // objects
    private Bird bird;
    private LifeBar lifeBar;
    private Timescale timescale;
    private ArrayList<Pipe> pipeList;

    // constructor
    public Level0() {
        bird = new Bird();
        lifeBar = new LifeBar();
        timescale = new Timescale();
        pipeList = new ArrayList<>();
        currentLife = lifeBar.getCurrentMaxLife();
    }

    // getter: to check if level 0 is finished with level-up score reached
    public boolean getHasFinishedLevel0() {
        return hasFinishedLevel0;
    }

    public void update(Input input) {
        // total frame count which counts from window opened
        totalFrameCount += 1;
        // render the background throughout the game
        BACKGROUND.draw(WIDTH / 2.0, HEIGHT / 2.0);

        // if current life reached zero, mark the game as finished
        if (currentLife == 0) {
            hasFinished = true;
        }
        // if current life reached the level-up score, mark the game as has finished and has leveled up
        if (score == LEVEL_UP_SCORE) {
            if (!recorded) {
                recorded = true;
                // mark the frame count at the moment of score reached (so can render the level-up screen)
                currentFrameCount = totalFrameCount;
            }
            hasLeveledUp = true;
            hasFinished = true;
        }

        // GAME NOT STARTED: render the instruction only
        if (!hasStarted) {
            renderInstructionScreen(input);
        }

        // GAME FINISHED:
        if (hasFinished) {
            // if game has finished and leveled up:
            if (hasLeveledUp) {
                // within the 150 frames, render the level-up screen
                if ((totalFrameCount - currentFrameCount) < LEVEL_UP_DURATION) {
                    renderLevelUpScreen();
                }
                // after the 150 frames, mark level 0 finished with level-up score reached
                else {
                    hasFinishedLevel0 = true;
                }
            }
            // if game has finished and has not leveled up: render the game over screen with the final score
            else {
                renderGameOverScreen(score);
            }
        }

        // GAME ACTIVE: render the bird, plastic pipe, score counter, life bar
        if (hasStarted && !hasFinished) {
            // count the frame from when game started
            frameCount += 1;

            // bird
            bird.update(input);

            // timescale and update the pipe regenerate frame accordingly
            currentTimescale = timescale.getTimescale() - 1;
            pipe_regenerate_frame = scaleFreq(INITIAL_PIPE_REGENERATE_FRAME, currentTimescale);

            // generate a new plastic pipe and add into pipe list at corresponding frame
            if (frameCount % pipe_regenerate_frame == 0) {
                pipeList.add(new PlasticPipe(hasLeveledUp));
            }

            updateScore();
            lifeBar.update(currentLife);
            timescale.update(input);
            renderScoreCounter(score);
        }
    }


    /**
     * Method: to render instruction screen, if one SPACE pressed mark the game as started
     */
    public void renderInstructionScreen(Input input) {
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth() - FONT.getWidth(INSTRUCTION_MSG)) / 2.0, MSG_HEIGHT);
        if (input.wasPressed(Keys.SPACE)) {
            hasStarted = true;
        }
    }

    /**
     * Method: to render level-up screen
     */
    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (WIDTH - FONT.getWidth(LEVEL_UP_MSG)) / 2.0, MSG_HEIGHT);
        String finalScoreMsg = FINAL_SCORE_MSG + LEVEL_UP_SCORE;
        FONT.drawString(finalScoreMsg, (WIDTH - FONT.getWidth(finalScoreMsg)) / 2.0, MSG_HEIGHT + MSG_GAP);
    }

    /**
     * Method: to update the score for level 0
     */
    public void updateScore(){
        for (Pipe pipe : pipeList) {
            // update every pipe in the pipe list
            pipe.update(currentTimescale);

            // only check the pipe which is not passed (not collided,scored)
            boolean isPassed = pipe.getPassed();
            if (!isPassed) {
                Rectangle topPipeBox = pipe.getTopBox();
                Rectangle bottomPipeBox = pipe.getBottomBox();
                Rectangle birdBox = bird.getBox();

                // Case 1: bird collide with bird, lose 1 life and mark pipe as passed
                if (detectCollision(birdBox, topPipeBox, bottomPipeBox)) {
                    pipe.markPassed();
                    currentLife -= 1;
                }

                // Case 2: bird successfully passed the pipe, earn 1 score and mark pipe as passed
                if (bird.getX() > topPipeBox.right()) {
                    pipe.markPassed();
                    score += 1;
                }
            }
        }

        // Case 3: bird out-of-bond, lose 1 life and reborn the bird at initial position
        if (detectOutOfBound(bird.getY())) {
            bird.reborn();
            currentLife -= 1;
        }
    }

}