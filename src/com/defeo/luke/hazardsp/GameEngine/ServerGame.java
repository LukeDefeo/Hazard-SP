package com.defeo.luke.hazardsp.GameEngine;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 26/10/2012
 * Time: 13:10
 */

/**
 * This class is the master game object held on the server, it is used as a reference for all the
 * game models on the clients. It can be changed from the game manager receives the messages from the
 * clients
 */
public class ServerGame extends Game {

    //private Iterator<Map.Entry<String, Player>> playerTurnIterator;
    private Iterator<Player> playerTurnIterator;
    private AttackProcessor attackProcessor;
    private List<Player> playerCycle;
    private String mahh;
    public ServerGame(Map<String, Territory> world, List<Continent> continents, Map<String, Player> players, Player firstPLayerToGo) {
        super(world, continents, players, firstPLayerToGo);
        this.attackProcessor = new AttackProcessor();
        //this.playerTurnIterator = getPlayers().entrySet().iterator();
        playerCycle = new ArrayList<Player>();
        Iterator<Map.Entry<String, Player>> playerCycleIterator = getPlayers().entrySet().iterator();
        while (playerCycleIterator.hasNext()) {
            playerCycle.add(playerCycleIterator.next().getValue());
        }
        this.playerTurnIterator = playerCycle.iterator();

    }

    /**
     * first method to be called, should only be called once
     *
     * @return
     */
    public void initialiseGame() {

        divideUpTerritories();
        placeInitialArmies();

        //set players phases to Reinforce
        for (Player player : getPlayers().values()) {
            player.setCurrentPhase(Player.Phase.END);
        }

        //for the first turn this needs to be done as its normally set at the end of the turn
        getCurrentPlayerTurn().determineArmiesToPlace();
        getCurrentPlayerTurn().setCurrentPhase(Player.Phase.REINFORCE);

    }

    private void divideUpTerritories() {
        //iterate randomly through territories assigning a player to each territory by cycling through players continuously
        List<String> territoryNames = new ArrayList<String>(getWorld().size());
        for (Territory territory : getWorld().values()) {
            territoryNames.add(territory.getName());
        }

        Iterator<Map.Entry<String, Player>> startGamePlayerIterator = getPlayers().entrySet().iterator();
        Random rng = new Random();
        int noOfTimesToLoop = territoryNames.size();

        for (int i = 0; i < noOfTimesToLoop; i++) {
            if (startGamePlayerIterator.hasNext() == false)
                startGamePlayerIterator = getPlayers().entrySet().iterator();

            Player player = startGamePlayerIterator.next().getValue();
            //get a random territory
            int randomTerritoryIndex = rng.nextInt(territoryNames.size());

            Territory randomTerritory = getWorld().get(territoryNames.remove(randomTerritoryIndex));
            randomTerritory.setOwner(player);

        }
    }

    private void placeInitialArmies() {
        //Determine initial amount of armies for each player and allocate them randomly
        Random rng = new Random();
        int noOfPlayers = getPlayers().size();
        for (Player player : getPlayers().values()) {
            player.determineArmiesToPlace(noOfPlayers);

            //ensure each territory has at least 1 army
            for (Territory territory : player.getTerritoriesOwned()) {
                territory.addArmies(1);
                player.decrementNoOfArmiesToPlace();
            }

            //randomly assign the rest
            while (player.getNoOfArmiesToPlace() > 0) {
                int randomNumber = rng.nextInt(player.getTerritoriesOwned().size());
                Territory randomTerritory = player.getTerritoriesOwned().get(randomNumber);
                randomTerritory.addArmies(1);
                player.decrementNoOfArmiesToPlace();
            }
        }
    }


    public boolean reinforce(Player currentPlayer, Territory desiredLocation) {

        if (isReinforcePossible(currentPlayer, desiredLocation)) {

            //update relavent fields in game model game model
            desiredLocation.incrementNoOfArmiesPresent();
            currentPlayer.decrementNoOfArmiesToPlace();

            //check armies to place of current player, if 0 then change phase to attack
            if (currentPlayer.getNoOfArmiesToPlace() <= 0) {
                currentPlayer.setCurrentPhase(Player.Phase.ATTACK);
            }
            return true;

        } else {
            return false;
        }
    }

    public boolean longReinforce(Player player, Territory territory, int armies) {
        if (isLongReinforcePossible(armies, player, territory)) {
            territory.addArmies(armies);
            player.setNoOfArmiesToPlace(0);
            player.setCurrentPhase(Player.Phase.ATTACK);
            return true;
        } else {
            return false;
        }
    }

    public boolean attack(Player attacker, Player defender, Territory attackingTerritory, Territory defendingTerritory, int noOfAttackingArmies) {

        //Get number maximum number of defending armies availble for attack
        int defendingArmiesAvailable = defendingTerritory.getNoOfArmiesPresent();
        //determine actual no of defending armes to be used
        int actualDefendingArmies;

        if (defendingArmiesAvailable > 2)
            actualDefendingArmies = 2;
        else
            actualDefendingArmies = defendingArmiesAvailable;

        if (isAttackPossible(attacker, defender, attackingTerritory, defendingTerritory, noOfAttackingArmies, actualDefendingArmies)) {
            System.out.println("Attacking with " + noOfAttackingArmies);
            System.out.println("Defending with " + actualDefendingArmies);
            //pass these values into a dice rolling method that returns the outcome
            List<Integer> attackResult = attackProcessor.doAttack(noOfAttackingArmies, actualDefendingArmies);

            //update game model
            attackingTerritory.removeArmies(attackResult.get(0));
            defendingTerritory.removeArmies(attackResult.get(1));

            //check if takeover occurs, set flag and change owner
            if (defendingTerritory.getNoOfArmiesPresent() < 1) {
                defendingTerritory.changeOwner(attacker);
                defendingTerritory.incrementNoOfArmiesPresent();
                attackingTerritory.decrementNoOfArmiesPresent();
                if (attackingTerritory.getNoOfArmiesPresent() > 1) {
                    defendingTerritory.setCaptured(true);
                    attacker.setCurrentPhase(Player.Phase.MOVE_IN);
                }

            }

            return true;

        } else
            return false;
    }


    public boolean longAttack(Player attacker, Player defender, Territory attackingTerritory, Territory defendingTerritory, int attackingArmies, int defendingArmies) {
        if (longAttackPossible(attacker, defender, attackingTerritory, defendingTerritory, attackingArmies, defendingArmies)) {
            boolean canAttack = true;
            System.out.println("start long attack");
            int attackNo = 0;

            while (canAttack) {
                System.out.println("attack iteration " + attackNo++);
                System.out.println("total attacking " + attackingTerritory.getNoOfArmiesPresent());
                System.out.println("total defending " + defendingTerritory.getNoOfArmiesPresent());
                int availableAttacking = (attackingTerritory.getNoOfArmiesPresent() - 1);
                int availableDefending = defendingTerritory.getNoOfArmiesPresent();
                int actualAttacking = 0;
                int actualDefending = 0;

                if (availableAttacking > 3)
                    actualAttacking = 3;
                else
                    actualAttacking = availableAttacking;

                if (availableDefending > 2)
                    actualDefending = 2;
                else
                    actualDefending = availableDefending;

                System.out.println("actual attacking " + actualAttacking);
                System.out.println("actual defending " + actualDefending);

                List<Integer> attackResult = attackProcessor.doAttack(actualAttacking, actualDefending);
                attackingTerritory.removeArmies(attackResult.get(0));
                defendingTerritory.removeArmies(attackResult.get(1));
                System.out.println("attacking destroyed: " + attackResult.get(0));
                System.out.println("defending destroyed: " + attackResult.get(1));
                if (attackingTerritory.getNoOfArmiesPresent() == 1 || defendingTerritory.getNoOfArmiesPresent() == 0) {
                    canAttack = false;
                    if (defendingTerritory.getNoOfArmiesPresent() < 1) {
                        System.out.println("territory captured");
                        defendingTerritory.changeOwner(attacker);
                        defendingTerritory.incrementNoOfArmiesPresent();
                        attackingTerritory.decrementNoOfArmiesPresent();
                        if (attackingTerritory.getNoOfArmiesPresent() > 1) {
                            defendingTerritory.setCaptured(true);
                            attacker.setCurrentPhase(Player.Phase.MOVE_IN);
                        }

                    } else {
                        System.out.println("depleted attacking armies");
                    }
                }

            }
            return true;
        } else {
            return false;
        }
    }


    public boolean moveIntoTerritory(Player player, Territory sourceTerritory, Territory targetTerritorty, int armiesToMove) {

        if (isMoveIntoTerritoryPossible(player, sourceTerritory, targetTerritorty, armiesToMove)) {

            // change fields in the territories
            targetTerritorty.addArmies(armiesToMove);
            sourceTerritory.removeArmies(armiesToMove);
            targetTerritorty.setCaptured(false);

            // Change phase BACK to attack
            player.setCurrentPhase(Player.Phase.ATTACK);
            return true;

        } else {
            return false;
        }
    }

    public boolean endAttack(Player player) {
        if (isEndAttackPossible(player)) {
            player.setCurrentPhase(Player.Phase.FORTIFY);
            return true;
        } else
            return false;

    }

    private boolean isEndAttackPossible(Player player) {
        if (!player.equals(getCurrentPlayerTurn())) {
            reportError("Not Players turn");
            return false;
        }
        if (getCurrentPlayerTurn().getCurrentPhase() != Player.Phase.ATTACK) {
            reportError("not correct phase");
            return false;
        }
        return true;

    }


    /**
     * Used in the fortify phase to move armies form one territory to
     * another
     *
     * @return
     */
    public boolean fortify(Player player, Territory sourceTerritory, Territory targetTerritory, int armiesSource, int armiesTarget) {

        if (isFortifyPossible(player, sourceTerritory, targetTerritory, armiesSource, armiesTarget)) {
            //change value of armies at both territories
            sourceTerritory.setNoOfArmiesPresent(armiesSource);
            targetTerritory.setNoOfArmiesPresent(armiesTarget);
            return true;
        } else
            return false;

    }


    /**
     * Called at the end of a players go, hands over the turn to the next player and
     * end currents players go
     */
    public void endTurn() {

        //set phase of current player to end
        this.getCurrentPlayerTurn().setCurrentPhase(Player.Phase.END);

        //set currentPlayerTurn to next player
        assignNextPlayerToGo();

        //change the next player to go's phase from end to reinforce and detirmine their number of armies to place
        this.getCurrentPlayerTurn().setCurrentPhase(Player.Phase.REINFORCE);
        System.out.println("changed player to go to " + getCurrentPlayerTurn().getUserName());

        this.getCurrentPlayerTurn().determineArmiesToPlace();
        System.out.println("Set armies to place for player " + getCurrentPlayerTurn().getUserName() + " to " + getCurrentPlayerTurn().getNoOfArmiesToPlace());

    }

    /**
     * Helper method to assign the next player to go
     */
    private void assignNextPlayerToGo() {
        //if run out of players in the iterator, create a new one, ie continuously cycle
        if (!playerTurnIterator.hasNext()) {
            playerTurnIterator = playerCycle.iterator();
        }

        setCurrentPlayerTurn(playerTurnIterator.next());
    }


    public boolean isPlayerKilled(Player player) {
        if (player.getTerritoriesOwned().isEmpty()) {
            safeKillPlayer(player);
            return true;

        }
        else
            return false;
    }

    public boolean isGameOver() {
        if (getPlayerCycle().size() == 1)
            return true;
        else
            return false;
    }

    private void reportError(String errorMessage) {
        System.out.println(errorMessage);
    }

    private boolean isReinforcePossible(Player currentPlayer, Territory desiredLocation) {
        //check its the current players go and in correct phase (use generic method)
        if (!isPlayersTurn(currentPlayer))
            return false;


        if (currentPlayer.getCurrentPhase() != Player.Phase.REINFORCE) {
            reportError("Player in incorrect phase");
            return false;
        }

        //check player owns target territory
        if (!currentPlayer.getTerritoriesOwned().contains(desiredLocation)) {
            reportError("Player Doesn't Own Territory");
            return false;
        }

        //Check player has >0 armies to place
        if (currentPlayer.getNoOfArmiesToPlace() < 0) {
            reportError("Player has no armies left to place");
            return false;
        } else {
            //everything ok
            return true;
        }
    }

    private boolean isLongReinforcePossible(int armies, Player currentPlayer, Territory desiredLocation) {
        //check its the current players go and in correct phase (use generic method)
        if (!isPlayersTurn(currentPlayer))
            return false;


        if (currentPlayer.getCurrentPhase() != Player.Phase.REINFORCE) {
            reportError("Player in incorrect phase");
            return false;
        }

        //check player owns target territory
        if (!currentPlayer.getTerritoriesOwned().contains(desiredLocation)) {
            reportError("Player Doesn't Own Territory");
            return false;
        }

        //Check player has >0 armies to place
        if (currentPlayer.getNoOfArmiesToPlace() == armies) {
            return true;
        } else {
            reportError("Incorect armies for long reinforce");
            return false;
        }
    }

    private boolean isAttackPossible(Player attacker, Player defender, Territory attackingTerritory, Territory defendingTerritory, int actualAttackingArmies, int defendingArmies) {
        //check its the attacking players go
        if (!isPlayersTurn(attacker))
            return false;

        //check players in the correct phase
        if (attacker.getCurrentPhase() != Player.Phase.ATTACK || defender.getCurrentPhase() != Player.Phase.END) {
            reportError("PLayers not in correct phase");
            return false;
        }

        //check that players own the territories
        if (!attacker.getTerritoriesOwned().contains(attackingTerritory) || !defender.getTerritoriesOwned().contains(defendingTerritory)) {
            reportError("PLayers Do not own territories");
            return false;
        }

        //check territories are adjacent
        if (!attackingTerritory.isAdjacent(defendingTerritory)) {
            reportError("Territories not adjacent");
            return false;
        }

        //determine maximum armies able to attack, at least one armies must remain behind
        int noAttackingArmiesAvailable = attackingTerritory.getNoOfArmiesPresent() - 1;

        //check that there are enough armies at each territory to process this attack
        if (noAttackingArmiesAvailable < actualAttackingArmies) {
            reportError("Not enough Armies available to attack");
            return false;
        }

        if (defendingArmies < 1) {
            reportError("No armies to defend territory");
            return false;
        }

        //everything ok
        return true;
    }

    private boolean longAttackPossible(Player attacker, Player defender, Territory attackingTerritory, Territory defendingTerritory, int attackingArmies, int defendingArmies) {
        //check its the attacking players go
        if (!isPlayersTurn(attacker))
            return false;

        //check players in the correct phase
        if (attacker.getCurrentPhase() != Player.Phase.ATTACK || defender.getCurrentPhase() != Player.Phase.END) {
            reportError("PLayers not in correct phase");
            return false;
        }

        //check that players own the territories
        if (!attacker.getTerritoriesOwned().contains(attackingTerritory) || !defender.getTerritoriesOwned().contains(defendingTerritory)) {
            reportError("PLayers Do not own territories");
            return false;
        }

        //check territories are adjacent
        if (!attackingTerritory.isAdjacent(defendingTerritory)) {
            reportError("Territories not adjacent");
            return false;
        }

        if (attackingTerritory.getNoOfArmiesPresent() != attackingArmies || defendingTerritory.getNoOfArmiesPresent() != defendingArmies) {
            reportError("Territory army info wrong");
            return false;
        }

        //everything ok
        return true;
    }


    private boolean isMoveIntoTerritoryPossible(Player player, Territory sourceTerritory, Territory targetTerritorty, int armiesToMove) {
        // check its the players go;
        isPlayersTurn(player);

        // check players is supposed to be moving in
        if (player.getCurrentPhase() != Player.Phase.MOVE_IN) {
            reportError("Player not in correct phase");
            return false;
        }

        // check territory no of armies is 0
        if (targetTerritorty.isCaptured() == false) {
            reportError("target territory not captured");
            return false;
        }

        //check enough armies at originating territory
        if (sourceTerritory.getNoOfArmiesPresent() < (armiesToMove + 1)) {
            reportError("Not enought armies at source territories");
            return false;
        }

        if (isGeneralTerritoryMoveIsPossible(player, sourceTerritory, targetTerritorty))
            return true;
        else
            return false;

    }

    private boolean isFortifyPossible(Player player, Territory sourceTerritory, Territory targetTerritory, int sourceArmies, int targetArmies) {
        //check its players go
        isPlayersTurn(player);

        if (sourceArmies == 0 || targetArmies == 0) {
            reportError("cant fortify with 0 armies");
            return false;
        }

        if ((sourceArmies + targetArmies) != (sourceTerritory.getNoOfArmiesPresent() + targetTerritory.getNoOfArmiesPresent())) {
            reportError("armies dont add up");
            return false;
        }

        //check its the current players go and in fortify phase phase
        if (player.getCurrentPhase() != Player.Phase.FORTIFY) {
            reportError("Player in Incorrect phase");
            return false;
        }
        if (isGeneralTerritoryMoveIsPossible(player, sourceTerritory, targetTerritory))
            return true;
        else {
            //everything ok
            return false;
        }


    }

    private boolean isGeneralTerritoryMoveIsPossible(Player player, Territory sourceTerritory, Territory targetTerritory) {
        //check player owns originating territory and target territiory
        if (!player.getTerritoriesOwned().contains(sourceTerritory) || !player.getTerritoriesOwned().contains(targetTerritory)) {
            reportError("Player doesn't own territories");
            return false;
        }

        //check territories are adjacent
        if (!sourceTerritory.isAdjacent(targetTerritory)) {
            reportError("Territories not adjacent");
            return false;
        } else {
            //everything ok
            return true;
        }
    }


    private boolean isPlayersTurn(Player currentPlayer) {
        if (currentPlayer.equals(getCurrentPlayerTurn())) {
            return true;
        } else {
            reportError("Not players turn");
            return false;
        }
    }

    private void safeKillPlayer(Player player) {
        //this messes up the go
        Player playerToGo = getCurrentPlayerTurn();
        List<Player> newPlayerCycle = new ArrayList<Player>();
        for (Player player1 : playerCycle) {
            newPlayerCycle.add(player1);
        }
        newPlayerCycle.remove(player);

        playerCycle = newPlayerCycle;
        playerTurnIterator = playerCycle.iterator();

        while (playerTurnIterator.hasNext()) {
            if(playerTurnIterator.next().equals(playerToGo)){
                break;
            }
        }
        //put iterator back
//
//        Player currentPlayerTurn = getCurrentPlayerTurn();
////        while (playerTurnIterator.hasNext()) {
////            playerTurnIterator.next();
////        }
//        playerTurnIterator = getPlayers().entrySet().iterator();
//        while (playerTurnIterator.hasNext()) {
//            if (playerTurnIterator.next().equals(player)) {
//                playerTurnIterator.remove();
//            }
//        }
//        playerTurnIterator = getPlayers().entrySet().iterator();
//        boolean turnCorrected = false;
//        if (getPlayers().size() > 1) {
//            while (turnCorrected == false) {
//                if (playerTurnIterator.next().getValue().equals(currentPlayerTurn)) {
//                    turnCorrected = true;
//                }
//        }
//
//        }
    }


    public void playerForfeit(Player player) {
        if (getCurrentPlayerTurn().equals(player)) {
            endTurn();
        }
        safeKillPlayer(player);
        Iterator<Map.Entry<String, Player>> playerIterator = getPlayers().entrySet().iterator();
        while (player.getTerritoriesOwned().size() > 0) {
            if (playerIterator.hasNext() == false)
                playerIterator = getPlayers().entrySet().iterator();
            Player newPlayer = playerIterator.next().getValue();
            Territory territory = player.getTerritoriesOwned().remove(0);
            territory.changeOwner(newPlayer);
            territory.setNoOfArmiesPresent(1);
        }
        System.out.println(player.getTerritoriesOwned().size());


    }

    public List<Player> getPlayerCycle() {
        return playerCycle;
    }
}