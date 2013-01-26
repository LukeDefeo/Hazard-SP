package com.defeo.luke.hazardsp.GameEngine;


import android.widget.Toast;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.UI.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 13/11/2012
 * Time: 17:01
 */
public class MessageFactory {

    ServerGame game;
    EventHandler eventHandler;

    public MessageFactory(EventHandler eventHandler,ServerGame game) {
        this.game = game;
        this.eventHandler = eventHandler;

    }

    
    public void sendReinforceMessage(Territory territory, Player owner) {
    	System.out.println("sending reinforce message: " +territory.getName()+ " " +owner.getUserName());
        game.reinforce(owner, territory);
        eventHandler.refreshScreen();
        if (owner.getCurrentPhase() == Player.Phase.ATTACK) {
            if (eventHandler.firstTurn) {
                Toast toast = Toast.makeText(Client.get().getCurrentActivity(), "Tap a territory to attack from", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    public void sendAttackMessage(Territory attackingTerritory, Territory defendingTerritory, Player attacker, Player defender, int armies) {

    	System.out.println("sending attack message: " +attacker.getUserName());
        game.attack(attacker, defender, attackingTerritory, defendingTerritory, armies);
        eventHandler.refreshScreen();
        List<String> some = new ArrayList<>();


    }

    public void sendMoveIntoTerritoryMessage(int armies, Territory sourceTerritory, Territory targetTerritory, Player owner) {
        game.moveIntoTerritory(owner, sourceTerritory, targetTerritory, armies);
        eventHandler.refreshScreen();
        System.out.println("sending moveIntoTerritory message: " + owner.getUserName());

    }

    public void sendFortifyMessage(Player owner, Territory sourceTerritory, Territory targetTerritory, int armiesSource,int armiesTarget) {
        game.fortify(owner, sourceTerritory, targetTerritory, armiesSource, armiesTarget);
        eventHandler.refreshScreen();
        System.out.println("sending reinforce message: " + owner.getUserName());

    }



    public void sendEndTurnMessage(Player player) {
        game.endTurn();
        eventHandler.setPlayer(game.getCurrentPlayerTurn());
        System.out.println("sending endTurn message");
    }

    public void sendLongReinforceMessage(Player player, int armies, Territory territory) {
        game.longReinforce(player, territory, armies);
        eventHandler.refreshScreen();

    }
    
    public void sendEndAttackMessage(Player owner) {
        game.endAttack(owner);
        eventHandler.refreshScreen();
        System.out.println("sending endOfAttack message: " + owner.getUserName());
    }

    public void sendLongAttackMessage(Player attacker, Player defender, Territory attackingTerritory, Territory defendingTerritory) {
        game.longAttack(attacker, defender, attackingTerritory, defendingTerritory, attackingTerritory.getNoOfArmiesPresent(), defendingTerritory.getNoOfArmiesPresent());
        eventHandler.refreshScreen();

    }

}
