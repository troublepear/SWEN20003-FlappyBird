import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Level1 extends Level{

    // level 1：background
    private final Image BACKGROUND = new Image("res/level-1/background.png");

    // level 1: messages
    private final String SHOOT_INSTRUCTION_MSG = "PRESS 'S' TO SHOOT";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final int SHOOT_MSG_GAP = 68;

    // level 1: game status
    private boolean hasStarted = false;
    private final boolean hasLeveledUp = true;
    private boolean hasFinished = false;
    private boolean win = false;
    private boolean overlap = false;

    private int score = 0;
    private final int WIN_SCORE = 50;
    private int currentLife;
    private int currentTimescale;

    // level 1: frame
    private int pipe_regenerate_frame;
    private final int INITIAL_WEAPON_REGENERATE_FRAME = 120;
    private int weapon_regenerate_frame;
    private int frameCount = 0;
    private int shootFrame;

    // level 1: objects
    private Bird bird;
    private LifeBar lifeBar;
    private Timescale timescale;
    private ArrayList<Pipe> pipeList;
    private ArrayList<Weapon> weaponList;
    private Weapon weaponHolding;

    public Level1(){
        bird = new Bird();
        lifeBar = new LifeBar();
        timescale = new Timescale();
        pipeList = new ArrayList<>();
        weaponList = new ArrayList<>();
        lifeBar.setToLevel1();
        bird.setToLevel1();
        currentLife = lifeBar.getCurrentMaxLife();
    }

    public void update(Input input){
        // render level 1 background throughout the level 1 game
        BACKGROUND.draw(WIDTH/2.0,HEIGHT/2.0);

        // if current life reached zero, update game status to finished
        if(currentLife == 0){
            hasFinished = true;
        }
        // if reached the win score, update game status to finished
        if(score == WIN_SCORE){
            win = true;
            hasFinished = true;
        }


        // GAME NOT STARTED: render instruction screen
        if(!hasStarted){
            renderInstructionScreen(input);
        }

        // GAME FINISHED: render corresponding screen (win or game over)
        if(hasFinished){
            if(win){
                renderWinScreen();
            }
            else{
                renderGameOverScreen(score);
            }
        }

        // GAME ACTIVE: if game has started and has not finished
        if(hasStarted && !hasFinished){

            // trim pipe/weapon list to size for every update()
            trimToSize();

            // count the frame from game started
            frameCount += 1;

            bird.update(input);
            Rectangle birdBox = bird.getBox();

            // update the pipes/weapons regenerate frame according to the current timescale
            currentTimescale = timescale.getTimescale() -1;
            pipe_regenerate_frame = scaleFreq(INITIAL_PIPE_REGENERATE_FRAME,currentTimescale);
            weapon_regenerate_frame = scaleFreq(INITIAL_WEAPON_REGENERATE_FRAME,currentTimescale);

            // add new generated pipe set into the pipe list according to the current regenerate frame
            if(frameCount % pipe_regenerate_frame == 0){
                pipeList.add(new Random().nextBoolean() ? new PlasticPipe(hasLeveledUp): new SteelPipe(hasLeveledUp));
            }

            // add new generated weapon into the weapon list according to the current regenerate frame
            if(frameCount % weapon_regenerate_frame == 0){
                // randomly generate a new rock or bomb weapon
                Weapon randomWeapon = new Random().nextBoolean() ? new Rock(): new Bomb();
                for(Pipe pipe:pipeList){
                    // check weapon overlap with every pipe set in pipe list
                    overlap = detectCollision(randomWeapon.getWeaponBox(),pipe.getTopBox(),pipe.getBottomBox());
                    // if it overlaps, immediately break the loop
                    if(overlap){
                        break;
                    }
                }
                // if not overlap, add the weapon into the weapon list
                if(!overlap) {
                    weaponList.add(randomWeapon);
                }
            }

            updateScore();
            // update the weapon list, update weapon holding and bird holding status
            for(int i=0;i<weaponList.size();i++){
                Weapon weapon = weaponList.get(i);
                // update every weapon in weapon list
                weapon.update(input,currentTimescale,bird);
                // if bird is not holding a weapon: check bird collide with weapon
                if(!bird.getHoldingStatus()){
                    boolean weaponPicked = detectCollision(weapon.getWeaponBox(),birdBox);
                    /*  if bird collide with weapon and weapon has not been picked before:
                     *  mark it as weaponHolding, mark it as picked, mark bird is holding the weapon
                     */
                    if(weaponPicked && !weapon.getPicked()){
                        weaponHolding = weapon;
                        weaponHolding.setPicked();
                        bird.holdWeapon();
                    }
                }
                // if bird is holding a weapon: this weapon moving with the bird
                else {
                    weaponHolding.holdByBird(bird.getBox().right(), bird.getY());
                }
                // if weapon mark as disappear (out of shoot frame range), remove it from the weapon list
                if(weapon.getDisappear()){
                    weaponList.remove(weapon);
                    weaponHolding = null;
                }
            }
            lifeBar.update(currentLife);
            timescale.update(input);
            renderScoreCounter(score);
        }
    }

    /** Method: to update the real-time score (5 different cases)
     * Earn score: 1). rock/bomb hit plastic pipe, 2). bomb hit steel pipe
     * Lose life:  1). bird out-of-bound, 2). bird collide with pipe, 3). bird collide with flame
     */
    public void updateScore(){
        // update every pipe set in pipe list
        for(int i=0;i < pipeList.size();i++){
            Pipe pipe = pipeList.get(i);
            // update each pipe in the pipe list
            pipe.update(currentTimescale);

            // only check the pipe which is not passed (not collided or scored)
            boolean isChecked = pipe.getPassed();
            if(!isChecked){
                Rectangle birdBox = bird.getBox();
                Rectangle topPipeBox = pipe.getTopBox();
                Rectangle bottomPipeBox = pipe.getBottomBox();
                boolean isSteelPipe = checkPipeType(pipe,"SteelPipe");
                boolean isPlasticPipe = checkPipeType(pipe,"PlasticPipe");

                // Case 1: bird collide with pipe, lose 1 life and mark as passed
                if(detectCollision(birdBox,topPipeBox,bottomPipeBox)) {
                    pipe.markPassed();
                    currentLife -= 1;
                }

                // Case 2: bird successfully passed the pipe, earn 1 score and mark pipe as passed
                if(bird.getX() > topPipeBox.right()){
                    pipe.markPassed();
                    score += 1;
                }

                if(isSteelPipe){
                    Rectangle topFlameBox = ((SteelPipe) pipe).getTopFlameBox();
                    Rectangle bottomFlameBox = ((SteelPipe) pipe).getBottomFlameBox();

                    // Case 3: bird collide with flame, lose 1 life and mark as passed
                    if(detectCollision(birdBox,topFlameBox,bottomFlameBox)) {
                        pipe.markPassed();
                        currentLife -= 1;
                    }

                    // Case 4: holding bomb hit the steel pipe, earn 1 score and remove weapon holding and pipe
                    if(weaponHolding != null && weaponHolding.getShot() && checkWeaponType(weaponHolding,"Bomb")){
                        boolean hit = detectCollision(weaponHolding.getWeaponBox(),topPipeBox,bottomPipeBox);
                        if(hit){
                            weaponList.remove(weaponHolding);
                            weaponHolding = null;
                            pipeList.remove(pipe);
                            score += 1;
                        }
                    }
                }

                // Case 5: holding weapon hit the plastic pipe, earn 1 score and remove weapon holding and pipe
                if(isPlasticPipe){
                    if(weaponHolding != null && weaponHolding.getShot()){
                        boolean hit = detectCollision(weaponHolding.getWeaponBox(),topPipeBox,bottomPipeBox);
                        if(hit){
                            weaponList.remove(weaponHolding);
                            weaponHolding = null;
                            pipeList.remove(pipe);
                            score += 1;
                        }
                    }
                }
            }

        }

        // Case 6: bird out-of-bound, lose 1 life and reborn the bird at the initial position
        if(detectOutOfBound(bird.getY())){
            bird.reborn();
            currentLife -= 1;
        }
    }

    /** Method: to render instruction screen
     */
    public void renderInstructionScreen(Input input){
        FONT.drawString(INSTRUCTION_MSG,(WIDTH-FONT.getWidth(INSTRUCTION_MSG))/2.0,MSG_HEIGHT);
        FONT.drawString(SHOOT_INSTRUCTION_MSG,(WIDTH-FONT.getWidth(SHOOT_INSTRUCTION_MSG))/2.0,MSG_HEIGHT+SHOOT_MSG_GAP);
        if(input.wasPressed(Keys.SPACE)) {
            hasStarted = true;
        }
    }

    /** Method: to render win screen
     */
    public void renderWinScreen(){
        FONT.drawString(CONGRATS_MSG,(WIDTH-FONT.getWidth(CONGRATS_MSG))/2.0,MSG_HEIGHT);
        String finalScoreMsg = FINAL_SCORE_MSG + WIN_SCORE;
        FONT.drawString(finalScoreMsg,(WIDTH-FONT.getWidth(finalScoreMsg))/2.0,MSG_HEIGHT+MSG_GAP);
    }

    /** Method: to check the type of the pipe (plastic or steel)
     */
    public boolean checkPipeType(Pipe pipe,String typeName) {
        return pipe.getClass().getSimpleName() == typeName;
    }

    /** Method: to check the type of the weapon (rock or bomb)
     */
    public boolean checkWeaponType(Weapon weapon,String typeName){
        return weapon.getClass().getSimpleName() == typeName;
    }

    /** Method: to trim the pipe/weapon list to size
     */
    public void trimToSize(){
        pipeList.trimToSize();
        weaponList.trimToSize();
    }


}
