package com.defeo.luke.hazardsp.GameEngine;


import com.defeo.luke.hazardsp.Client.Client;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 13/11/2012
 * Time: 17:01
 */
public class MessageFactory {

    ServerGame game;

    public MessageFactory() {


    }

    
    public void sendReinforceMessage(Territory territory, Player owner) {
    	System.out.println("sending reinforce message: " +territory.getName()+ " " +owner.getUserName());
    	
    }

    public void sendAttackMessage(Territory attackingTerritory, Territory defendingTerritory, Player attacker, Player defender, int armies) {

    	System.out.println("sending attack message: " +attacker.getUserName());
    	
    }

    public void sendMoveIntoTerritoryMessage(int armies, Territory sourceTerritory, Territory targetTerritory, Player owner) {

    	System.out.println("sending moveIntoTerritory message: " +owner.getUserName());
    }

    public void sendFortifyMessage(Player owner, Territory sourceTerritory, Territory targetTerritory, int armiesSource,int armiesTarget) {

    	System.out.println("sending reinforce message: " +owner.getUserName());
    	
    }



    public void sendEndTurnMessage(Player player) {

        System.out.println("sending endTurn message");
    }

    public void sendLongReinforceMessage(Player player, int armies, Territory territory) {

    }
    
    public void sendEndAttackMessage(Player owner) {

    	System.out.println("sending endOfAttack message: " +owner.getUserName());
    }

    public void sendLongAttackMessage(Player attacker, Player defender, Territory attackingTerritory, Territory defendingTerritory) {

    }
}
