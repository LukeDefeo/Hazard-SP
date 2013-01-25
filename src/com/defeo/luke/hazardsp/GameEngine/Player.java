package com.defeo.luke.hazardsp.GameEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luke De Feo
 */
public class Player implements Serializable {
    public enum Phase {PREGAME, REINFORCE, ATTACK, MOVE_IN, FORTIFY, END}



    private boolean defeated;
    private String userName;
    private int noOfArmiesToPlace = 0;
    private Phase currentPhase;
    private List<Territory> territoriesOwned; //this should be a map :(
    List<BonusCard> bonusCards;
    private boolean AI;

    private final List<Continent> continents;   //for calculation noOfArmies to place
    private int guiID;           // only used so the GUI can link itself to this object,

    public Player(String userName, List<Continent> continents,int GUIId, boolean isAI) {
        this.userName = userName;
        this.currentPhase = Phase.PREGAME;
        this.territoriesOwned = new ArrayList<Territory>();
        this.bonusCards = new ArrayList<BonusCard>();
        this.continents = continents;
        this.guiID = GUIId;
        this.AI = isAI;

    }

    public boolean isAI() {
        return AI;
    }

    @Override
    public String toString() {
        return "Player Id: " + this.getUserName() + "\n Total no of armies: " + this.getTotalNoOfArmies() +
                "\nTotal Territories: " + this.getTotalNoOfTerritoriesOwned() + " Phase: " + currentPhase +
                " Armies To place: " + noOfArmiesToPlace;

    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public int getTotalNoOfTerritoriesOwned() {
        return territoriesOwned.size();
    }

    public String getUserName() {
        return userName;
    }

    public int getTotalNoOfArmies() {
        int noOfTerritoriesOwned = 0;
        for (Territory territory : this.territoriesOwned) {
            noOfTerritoriesOwned += territory.getNoOfArmiesPresent();
        }
        return noOfTerritoriesOwned;
    }

    /**
     * When a player Gets a new territory this method is automatically called to give him a bonus carD
     */
    public void addBonusCard() {
        BonusCard card = new BonusCard();
        bonusCards.add(card);
    }

    /**
     * when a player uses his bonus card this method is called to update the list
     *
     * @param card
     */
    private void removeBonusCard(BonusCard card) {
        bonusCards.remove(card);
    }

    /**
     * Called at the START of a players turn to Dertimine the number of
     * armys a player has to place during a turn if no
     * bonus cards are played according to the rules of risk, by the end of the turn
     * noOfArmies to palce should be 0
     *
     * @return
     */
    public int determineArmiesToPlace() {
        int continentBonus = 0;
        List<Continent> continentsOwned = getContinentsOwned();
        for (Continent continent : continentsOwned) {
            continentBonus += continent.getContinentArmyBonus();

        }
        int territoryBonus = getTotalNoOfTerritoriesOwned() / 3;
        if (territoryBonus < 3)
            territoryBonus = 3;

        this.noOfArmiesToPlace = (territoryBonus + continentBonus);
        return this.noOfArmiesToPlace;
    }

    /**
     * Called at the start of the game when the players get their initial
     * share of armies, calcualtion based on risk rules.
     *
     * @param startingNoOfPlayers
     * @return
     */
    public int determineArmiesToPlace(int startingNoOfPlayers) {
        this.noOfArmiesToPlace = 50 - startingNoOfPlayers * 5;
        return this.noOfArmiesToPlace;
    }


    /**
     * Dertimines the number of armys a player has to place during a turn if a
     * set of bonus cards are played according to the rules of risk
     *
     * @param card1
     * @param card2
     * @param card3
     * @return
     */
    public int determinermiesToPlace(BonusCard card1, BonusCard card2, BonusCard card3) {
        //get amount if no cards played
        int orignalAmount = determineArmiesToPlace();
        int bonusAmount = 0;
        //if all cards the same


        this.noOfArmiesToPlace = orignalAmount + bonusAmount;
        return this.noOfArmiesToPlace;

    }

    public int getNoOfArmiesToPlace() {
        return noOfArmiesToPlace;
    }

    public void decrementNoOfArmiesToPlace() {
        this.noOfArmiesToPlace--;
    }


    public List<Territory> getTerritoriesOwned() {
        return territoriesOwned;
    }

    /**
     * Called by game and internally to determine which continents a player owns
     *
     * @param
     * @return
     */
    public List<Continent> getContinentsOwned() {
        List<Continent> continentsOwned = new ArrayList<Continent>();
        for (Continent continent : this.continents) {
            if (continent.playerControlsContinent(territoriesOwned)) {
                continentsOwned.add(continent);
            }
        }
        return continentsOwned;
    }


    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public int getGuiID() {
        return guiID;
    }


    /**
     * used on the client only, for the server to set a given
     * number of armies to place to display on the GUI
     *
     * @param noOfArmiesToPlace
     */
    public void setNoOfArmiesToPlace(int noOfArmiesToPlace) {
        this.noOfArmiesToPlace = noOfArmiesToPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (userName != null ? !userName.equals(player.userName) : player.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }
}
