package com.defeo.luke.hazardsp.UI;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import com.defeo.luke.hazardsp.AI.AIManager;
import com.defeo.luke.hazardsp.Activities.R;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.GameEngine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class handles In-Game events triggered either by touch screen activity
 * or by push updates sent by the server in order to update the screen
 *
 * @author Thomas Yorkshire
 */
public class EventHandler {

    public enum Sounds {EXPLOSION, MARCHING, VICTORY, ENDGAME, FORTIFY,}

    MapView gameView;
    ServerGame game;
    Player player;
    boolean isTerritoryHighlighted;
    TerritorySprite highlightedTerritory;
    MessageFactory messageFactory;
    Territory potentialMoveInTerritory;
    private ArrayList<TerritorySprite> territorySprites;
    Territory sourceTerritory;
    Territory targetTerritory;
    int fortifyTargetStart;
    int fortifySourceStart;
    boolean fortifyInProgress = false;
    MediaPlayer mp;
    private Context context;
    Lock lock;
    boolean territoryAttacked = false;
    int tempMoveInCount;
    AIManager AIManager;

    public EventHandler(MapView view) {
        Client.get().setEventHandler(this);
        this.gameView = view;
        game = Client.get().getGame();
        player = Client.get().getGame().getCurrentPlayerTurn();
        messageFactory = new MessageFactory(this, game);
        territorySprites = gameView.getSprites().getTerritorySprites();
        context = view.getContext();
        mp = new MediaPlayer();
        lock = new ReentrantLock();
        tempMoveInCount = 0;
        AIManager = new AIManager(messageFactory, game, this);
    }




    public void longTouchEvent(TerritorySprite ts) {
        Log.i("EVENT HANDLER", "LONG TOUCH EVENT");
        if (this.player.getUserName().equals(game.getCurrentPlayerTurn().getUserName())) {


            switch (game.getCurrentPlayerTurn().getCurrentPhase()) {
                case REINFORCE:
                    if (ts.getTerritory().getOwner().equals(this.player)) {
                        messageFactory.sendLongReinforceMessage(this.player, this.player.getNoOfArmiesToPlace(), ts.getTerritory());
                        this.player.setNoOfArmiesToPlace(0);
                        ts.getTerritory().addArmies(this.player.getNoOfArmiesToPlace());
                        refreshScreen();
                        playMarchingSound();
                        Log.i("EVENT HANDLDER", "DO LONG REINFORCE");
                    }
                    break;
                case ATTACK:
                    Log.i("TOUCH", "LONG ATTACK PENDING");

                    if (isTerritoryHighlighted) {
                        Log.i("TOUCH", "LONG ATTACK PENDING 2");

                        List<Territory> attackableTerritories = highlightedTerritory.getTerritory().getAttackableTerritories();
                        for (Territory attackableTerritory : attackableTerritories) {
                            if (ts.getTerritory().equals(attackableTerritory)) {
                                Log.i("TOUCH", "LONG ATTACK SUCESS");

                                messageFactory.sendLongAttackMessage(this.player, ts.getTerritory().getOwner(), highlightedTerritory.getTerritory(), ts.getTerritory());

                                territoryAttacked = true;
                                isTerritoryHighlighted = false;
                                potentialMoveInTerritory = attackableTerritory;
                                if (game.getCurrentPlayerTurn().getCurrentPhase() == Player.Phase.MOVE_IN) {
                                    highlightForMoveIn(highlightedTerritory.getTerritory(), attackableTerritory);
                                    playSound(EventHandler.Sounds.VICTORY);
                                } else {
                                    setGUINormal();
                                    refreshScreen();
                                    if (this.player.getTerritoriesOwned().contains(potentialMoveInTerritory)) {
                                        playVictorySound();
                                    } else {
                                        playExplosionSound();
                                    }
                                    isTerritoryHighlighted = false;
                                }
                                break;      //is more efficient this way!
                            }
                        }
                        //territory not attackable

                        //DONT CHANGE GUI ON LONG PRESS
//                        isTerritoryHighlighted = false;
//                        setGUINormal();
//                        refreshScreen();


                    }
                    break;
                case MOVE_IN:
                    int sourceArmiesMove = sourceTerritory.getNoOfArmiesPresent();
                    int targetArmiesMove = targetTerritory.getNoOfArmiesPresent();
                    if (ts.getTerritory().equals(sourceTerritory)) {
                        Log.i("EVENT HANDLDER", "DO LONG MOVE ");
                        sourceTerritory.setNoOfArmiesPresent(sourceArmiesMove + targetArmiesMove - 1);
                        targetTerritory.setNoOfArmiesPresent(1);
                        tempMoveInCount = 0;
                        refreshScreen();
                        break;
                    }
                    if (ts.getTerritory().equals(targetTerritory)) {
                        Log.i("EVENT HANDLDER", "DO LONG MOVE");
                        targetTerritory.setNoOfArmiesPresent(sourceArmiesMove + targetArmiesMove - 1);
                        sourceTerritory.setNoOfArmiesPresent(1);
                        tempMoveInCount = sourceArmiesMove - 1;
                        refreshScreen();
                    }


                    break;
                case FORTIFY:
                    Log.i("EVENT HANDLDER", "DO LONG FORTIFY 1");
                    if (fortifyInProgress) {
                        Log.i("EVENT HANDLDER", "DO LONG FORTIFY 2");
                        int sourceArmiesFort = sourceTerritory.getNoOfArmiesPresent();
                        int targetArmiesFort = targetTerritory.getNoOfArmiesPresent();
                        Log.i("EVENT HANDLDER", "ST: " + sourceTerritory.getName() + " TT " + targetTerritory.getName());
                        Log.i("EVENT HANDLDER", "AT " + ts.getTerritory().getName());
                        if (ts.getTerritory().equals(sourceTerritory)) {
                            Log.i("EVENT HANDLDER", "DO LONG FORTIFY 3");
                            sourceTerritory.setNoOfArmiesPresent(sourceArmiesFort + targetArmiesFort - 1);
                            targetTerritory.setNoOfArmiesPresent(1);
                            refreshScreen();
                            break;
                        }
                        if (ts.getTerritory().equals(targetTerritory)) {
                            Log.i("EVENT HANDLDER", "DO LONG FORTIFY 4");
                            targetTerritory.setNoOfArmiesPresent(sourceArmiesFort + targetArmiesFort - 1);
                            sourceTerritory.setNoOfArmiesPresent(1);
                            refreshScreen();
                        }

                    }
                    break;

            }


        }
    }

    /**
     * This functions deals with handling the actions associated with items that
     * have been clicked on screen. There are two possible reasons for this.
     * Either an update has to be sent to the server, OR some client-only event
     * has to be shown on screen - For example, continent highlighting.
     *
     * @param item - The spriteFactory that has been clicked.
     */

    public void touchEvent(Sprite item) {

        //	Log.i("TOUCH", "EVENT OCCURED");
        //	Log.i("TOUCH", "Type-" + item.Type);

        //----The magic happens here!---
        Log.i("EVENT HANDLER", "THE current phase is " + Client.get().getEventHandler().getPlayer().getCurrentPhase());
        Log.i("TOUCH", "0");

        if (item.Type == Sprite.SpriteType.TERRITORY) {
            //usefull vars
            TerritorySprite territorySprite = (TerritorySprite) item;
            Territory territoryObject = territorySprite.getTerritory();

            Log.i("TOUCH", "1");

            //   Log.i("TOUCH", "Turn current-" + game.getCurrentPlayerTurn().getUserName());

            if (this.player.getUserName().equals(game.getCurrentPlayerTurn().getUserName())) {

                Log.i("TOUCH", "2");

                // 	Log.i("TOUCH", "phase-" + game.getCurrentPlayerTurn().getCurrentPhase());

                switch (game.getCurrentPlayerTurn().getCurrentPhase()) {


                    case REINFORCE:
                        Log.i("TOUCH", "3");
                        if (territoryObject.getOwner().equals(this.player)) {
                            Log.i("TOUCH", "4");

                            if (this.player.getNoOfArmiesToPlace() > 0) {
                                Log.i("TOUCH", "5");
                                messageFactory.sendReinforceMessage(territoryObject, this.player);
                                playMarchingSound();
                                //so the player cant tap quickly before the response sets this
                                refreshScreen();
                            }
                        }
                        break;
                    case ATTACK:
                        if (isTerritoryHighlighted) {
                            //note highlighted territoroy is the previusly tapped on territory
                            List<Territory> attackableTerritories = highlightedTerritory.getTerritory().getAttackableTerritories();
                            for (Territory attackableTerritory : attackableTerritories) {
                                if (territoryObject.equals(attackableTerritory)) {
                                    Log.i("TOUCH", "6");

                                    int armiesAvailble = highlightedTerritory.getTerritory().getNoOfArmiesPresent() - 1;
                                    if (armiesAvailble > 3)
                                        armiesAvailble = 3;

                                    messageFactory.sendAttackMessage(highlightedTerritory.getTerritory(), attackableTerritory, this.player, attackableTerritory.getOwner(), armiesAvailble);
                                    territoryAttacked = true;
                                    isTerritoryHighlighted = false;
                                    potentialMoveInTerritory = attackableTerritory;
                                    if (game.getCurrentPlayerTurn().getCurrentPhase() == Player.Phase.MOVE_IN) {
                                        highlightForMoveIn(highlightedTerritory.getTerritory(), attackableTerritory);
                                        playSound(EventHandler.Sounds.VICTORY);
                                    } else {
                                        setGUINormal();
                                        refreshScreen();
                                        if (this.player.getTerritoriesOwned().contains(potentialMoveInTerritory)) {
                                            playVictorySound();
                                        } else {
                                            playExplosionSound();
                                        }
                                        isTerritoryHighlighted = false;

                                    }
                                    break;      //is more efficient this way!
                                }
                            }


                        } else {
                            Log.i("TOUCH", "2");

                            //player is selecting territory to attack from
                            if (territoryObject.getOwner().equals(this.player)) {
                                Log.i("TOUCH", "3");

                                //only highlight if the terriotry has >1 army to attack with
                                if (territoryObject.getNoOfArmiesPresent() > 1) {
                                    Log.i("TOUCH", "4");


                                    //set all sprites dimmed and turn only the correct ones back to normal
                                    setGUIDimmed();

                                    List<Territory> attackableTerritories = territoryObject.getAttackableTerritories();
                                    //create the spriteFactory verson of the above using guiID
                                    List<TerritorySprite> attackbleTerritorySprites = new ArrayList<TerritorySprite>();
                                    for (Territory attackableTerritory : attackableTerritories) {
                                        attackbleTerritorySprites.add(territorySprites.get(attackableTerritory.getGuiID()));
                                    }
                                    //make sure the territory tapped on stays highlighted too
                                    attackbleTerritorySprites.add(territorySprite);

                                    for (TerritorySprite attackbleTerritorySprite : attackbleTerritorySprites) {
                                        attackbleTerritorySprite.setNormal();

                                    }

                                    highlightedTerritory = territorySprite;
                                    isTerritoryHighlighted = true;

                                    refreshScreen();
                                } else {
                                    //could display message saying you need armies but prob best to just do nothing her?
                                }
                            }
                        }
                        break;

                    case MOVE_IN:

                        if (territoryObject.equals(sourceTerritory) || territoryObject.equals(targetTerritory)) {
                            //Log.i("EVENT HANDLER", "3 ");
                            if (territoryObject.equals(sourceTerritory)) {
                                // Log.i("EVENT HANDLER", "4 ");
                                if (targetTerritory.getNoOfArmiesPresent() > 1) {
                                    //  Log.i("EVENT HANDLER", "5");
                                    sourceTerritory.incrementNoOfArmiesPresent();
                                    targetTerritory.decrementNoOfArmiesPresent();
                                    tempMoveInCount--;
                                }
                            }
                            if (territoryObject.equals(targetTerritory)) {
                                //      Log.i("EVENT HANDLER", " 4");
                                if (sourceTerritory.getNoOfArmiesPresent() > 1) {
                                    //     Log.i("EVENT HANDLER", "5 ");
                                    targetTerritory.incrementNoOfArmiesPresent();
                                    sourceTerritory.decrementNoOfArmiesPresent();
                                    tempMoveInCount++;
                                }
                            }
                        } else {
                            Log.i("EVENT HANDLER", "SENding move IN MESSAGE ");
                            sourceTerritory.addArmies(tempMoveInCount);
                            targetTerritory.removeArmies(tempMoveInCount);
                            messageFactory.sendMoveIntoTerritoryMessage((tempMoveInCount), sourceTerritory, targetTerritory, this.player);
                            tempMoveInCount = 0;
                            isTerritoryHighlighted = false;
                            setGUINormal();
                        }
                        refreshScreen();
                        break;

                    case FORTIFY:
                        //if msg bufer is !null  and they didnt tap on the territoyy to fortify,
                        //send the msg
                        if (fortifyInProgress) {
                            Log.i("eventhandler", "fort bool start");
                            if (territoryObject.equals(sourceTerritory) || territoryObject.equals(targetTerritory)) {
                                Log.i("EVENT HANDLER", "3 ");
                                if (territoryObject.equals(sourceTerritory)) {
                                    Log.i("EVENT HANDLER", "4 ");
                                    if (targetTerritory.getNoOfArmiesPresent() > 1) {
                                        Log.i("EVENT HANDLER", "5");
                                        sourceTerritory.incrementNoOfArmiesPresent();
                                        targetTerritory.decrementNoOfArmiesPresent();
                                    }
                                }
                                if (territoryObject.equals(targetTerritory)) {
                                    Log.i("EVENT HANDLER", " 4");
                                    if (sourceTerritory.getNoOfArmiesPresent() > 1) {
                                        Log.i("EVENT HANDLER", "5 ");
                                        targetTerritory.incrementNoOfArmiesPresent();
                                        sourceTerritory.decrementNoOfArmiesPresent();
                                    }
                                }
                            } else {
                                Log.i("EVENT HANDLER", "SENding Fortify IN MESSAGE ");
                                //if statement here
                                if (fortifySourceStart == sourceTerritory.getNoOfArmiesPresent() && fortifyTargetStart == targetTerritory.getNoOfArmiesPresent()) {
                                    isTerritoryHighlighted = false;
                                    setGUINormal();
                                    fortifyInProgress = false;
                                } else {
                                    messageFactory.sendFortifyMessage(this.player, sourceTerritory, targetTerritory, sourceTerritory.getNoOfArmiesPresent(), targetTerritory.getNoOfArmiesPresent());
                                    playHelicopterSound();
                                    setGUINormal();
                                    fortifyInProgress = false;
                                    isTerritoryHighlighted = false;
                                }

                            }
                            refreshScreen();
                            break;


                        }

                        if (isTerritoryHighlighted) {
                            List<Territory> fortifyableTerritories = highlightedTerritory.getTerritory().getFortifyableTerritories();
                            for (Territory fortifyableTerritory : fortifyableTerritories) {
                                if (territorySprite.getTerritory().equals(fortifyableTerritory)) {
                                    targetTerritory = territoryObject;
                                    sourceTerritory = highlightedTerritory.getTerritory();
                                    fortifySourceStart = sourceTerritory.getNoOfArmiesPresent();
                                    fortifyTargetStart = targetTerritory.getNoOfArmiesPresent();
                                    setGUIDimmed();
                                    territorySprites.get(targetTerritory.getGuiID()).setNormal();
                                    territorySprites.get(sourceTerritory.getGuiID()).setNormal();
                                    fortifyInProgress = true;
                                    Log.i("EVENT HANDLER", "fortify boolean set");
                                    isTerritoryHighlighted = false;
                                    refreshScreen();
                                    break;
                                }
                            }
                            break;
                            //territory not fortifyable
                        } else {

                            if (territoryObject.getOwner().equals(this.player)) {
                                //only highlight if the terriotry has >1 army to attack with
                                if (territoryObject.getNoOfArmiesPresent() > 1) {
                                    //set all sprites dimmed and turn only the correct ones back to normal
                                    setGUIDimmed();

                                    List<Territory> fortifyableTerritories = territoryObject.getFortifyableTerritories();
                                    //create the spriteFactory verson of the above using guiID
                                    List<TerritorySprite> fortifyableTerritorySprites = new ArrayList<TerritorySprite>();
                                    for (Territory fortifyableTerritory : fortifyableTerritories) {
                                        fortifyableTerritorySprites.add(territorySprites.get(fortifyableTerritory.getGuiID()));
                                    }
                                    //make sure the territory tapped on stays highlighted too
                                    fortifyableTerritorySprites.add(territorySprite);

                                    for (TerritorySprite fortifyableTerritorySprite : fortifyableTerritorySprites) {
                                        fortifyableTerritorySprite.setNormal();
                                    }

                                    highlightedTerritory = territorySprite;
                                    isTerritoryHighlighted = true;

                                    refreshScreen();
                                }
                            }
                            break;
                        }

                }
            }

        } else if (item.Type == Sprite.SpriteType.BUTTON) {
            Log.i("EVENT HANDDER", "A button pressed");

            if (player.getCurrentPhase() == Player.Phase.ATTACK) {
                messageFactory.sendEndAttackMessage(this.player);
                isTerritoryHighlighted = false;
                setGUINormal();
                refreshScreen();
                Log.i("EVENT HANDDER", "in attack phase sending end attack message");
            } else if (player.getCurrentPhase() == Player.Phase.FORTIFY) {
                messageFactory.sendEndTurnMessage(this.player);
                fortifyInProgress = false;
                setGUINormal();
                refreshScreen();
                isTerritoryHighlighted = false;
                if (game.getCurrentPlayerTurn().isAI()) {
                    AIManager.runAI(game.getCurrentPlayerTurn());
                }
                refreshScreen();
                Log.i("EVENT HANDDER", "in fortify phase sending end attack message");
            }


        } else if (item.Type == Sprite.SpriteType.BACKGROUND) {
            System.out.println("Current player is: " + game.getCurrentPlayerTurn().toString());


            Log.i("EVENT HANDDER", "Background Clicked");
            switch (game.getCurrentPlayerTurn().getCurrentPhase()) {
                case MOVE_IN:
                    Log.i("EVENT HANDLER", "SENding move IN MESSAGE ");
                    sourceTerritory.addArmies(tempMoveInCount);
                    targetTerritory.removeArmies(tempMoveInCount);
                    messageFactory.sendMoveIntoTerritoryMessage((tempMoveInCount), sourceTerritory, targetTerritory, this.player);
                    tempMoveInCount = 0;
                    isTerritoryHighlighted = false;
                    setGUINormal();
                    refreshScreen();
                    break;
                case FORTIFY:
                    if (fortifyInProgress) {
                        Log.i("EVENT HANDLER", "SENding Fortify IN MESSAGE ");

                        messageFactory.sendFortifyMessage(this.player, sourceTerritory, targetTerritory, sourceTerritory.getNoOfArmiesPresent(), targetTerritory.getNoOfArmiesPresent());
                        setGUINormal();
                        playHelicopterSound();
                        refreshScreen();
                        fortifyInProgress = false;
                        isTerritoryHighlighted = false;

                        if (fortifySourceStart == sourceTerritory.getNoOfArmiesPresent() && fortifyTargetStart == targetTerritory.getNoOfArmiesPresent()) {
                            isTerritoryHighlighted = false;
                            setGUINormal();
                            fortifyInProgress = false;
                        } else {
                            messageFactory.sendFortifyMessage(this.player, sourceTerritory, targetTerritory, sourceTerritory.getNoOfArmiesPresent(), targetTerritory.getNoOfArmiesPresent());
                            playHelicopterSound();
                            setGUINormal();
                            fortifyInProgress = false;
                            isTerritoryHighlighted = false;
                        }
                        refreshScreen();
                        break;
                    }

            }


            //is background
            setGUINormal();
            refreshScreen();
            isTerritoryHighlighted = false;
        }

    }


    private void setGUINormal() {
        for (TerritorySprite territorySprite : territorySprites) {
            territorySprite.setNormal();
        }
    }

    private void setGUIDimmed() {
        for (TerritorySprite territorySprite : territorySprites) {
            territorySprite.setDimmed();
        }
    }


    /**
     * This function is triggered when the game screen needs updating.
     */
    public void refreshScreen() {
        if (game.getCurrentPlayerTurn().equals(this.player)) {
            //set next phase button to normal
        } else {
            //set it to dim
        }
        gameView.postInvalidate();
        //  gameView.invalidate();

    }


    /**
     * This function updates all information in a recent push update to all
     * the sprites on screen
     */
    public void updateSprites() {

        //Update all sprites

        //Refresh the screen
        refreshScreen();

    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public Player getPlayer() {
        return player;
    }

    public void highlightForMoveIn(Territory territory1, Territory territory2) {

        setGUIDimmed();
        Log.i("EVENT HANDLER", "SETTING TERRITORYS DIMMED FOR MOVE IN");
        TerritorySprite territorySprite1 = territorySprites.get(territory1.getGuiID());
        territorySprite1.setNormal();
        TerritorySprite territorySprite2 = territorySprites.get(territory2.getGuiID());
        territorySprite2.setNormal();
        sourceTerritory = highlightedTerritory.getTerritory();
        targetTerritory = potentialMoveInTerritory;
        refreshScreen();

    }

    public void playSound(Sounds sound) {
//        lock.lock();


        switch (sound) {
            case EXPLOSION:
                playExplosionSound();
                break;
            case VICTORY:
                playVictorySound();
                break;
            case ENDGAME:
                playEndGameSound();
                break;
            case FORTIFY:
                playHelicopterSound();
                break;
        }
    }

    // Source: http://soundbible.com/1986-Bomb-Exploding.html
    public void playExplosionSound() {
        // Play the explosion sound
        //sti
        if (mp.isPlaying())
            mp.stop();
        mp = MediaPlayer.create(context, R.raw.explosion);
        mp.start();
        soundDuration(750);
    }

    // Source: http://soundbible.com/1329-Soldiers-Marching.html
    public void playMarchingSound() {
        // Play the marching sound
        if (mp.isPlaying())
            mp.stop();
        mp = MediaPlayer.create(context, R.raw.marching);
        mp.start();
        soundDuration(700);
    }

    // Source: http://soundbible.com/suggest.php?q=victory&x=0&y=0
    private void playVictorySound() {
        // Play the victory sound  \
        if (mp.isPlaying())
            mp.stop();
        mp = MediaPlayer.create(context, R.raw.victory);
        mp.start();
        soundDuration(1200);
    }

    // Source: http://www.allmusiclibrary.com/free_sound_effects/victory_fanfare.mp3
    public void playEndGameSound() {
        // Play end game sound

        if (mp.isPlaying())
            mp.stop();
        mp = MediaPlayer.create(context, R.raw.end_game);
        mp.start();
        soundDuration(6500);
    }

    // Souce: http://soundbible.com/323-Military-Helicopter.html
    public void playHelicopterSound() {
        // Play helicopter sound
        if (mp.isPlaying())
            mp.stop();
        mp = MediaPlayer.create(context, R.raw.helicopter);
        mp.start();
        soundDuration(1750);
    }

    public void soundDuration(int time) {
        final int time1 = time;
        Client.get().getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        mp.stop();
                    }
                }, time1);
            }
        });

    }

    public boolean isTerritoryAttacked() {
        return territoryAttacked;
    }

    public void setTerritoryAttacked(boolean territoryAttacked) {
        this.territoryAttacked = territoryAttacked;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}