package com.defeo.luke.hazardsp.GameEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Luke De Feo
 */
public class Territory implements Serializable {

    private String name;                              //now a string since read territories from a file
    private Player owner = null;
    private int noOfArmiesPresent = 0;
    private Map<String, Territory> adjacentTerritories;
    private int guiID;
    private static int guiIDCounter=0;
    private boolean captured;

    public Territory(String territoryName,int guiID) {
        this.name = territoryName;
        this.owner = new Player("NULL PLAYER", null,0,false);
        this.guiID = guiID;
    }

    public int getGuiID() {
        return guiID;
    }

    /**
     * Determines wether a territory is adjacent to another territory
     *
     * @param AnotherTerritory
     * @return
     */
    public Boolean isAdjacent(Territory AnotherTerritory) {
        if (this.adjacentTerritories.containsKey(AnotherTerritory.getName()))
            return true;

        return false;
    }

    /**
     * overloaded version of isAdjacent when only have a territory name
     *
     * @param anotherTerritoryName
     * @return
     */
    public Boolean isAdjacent(String anotherTerritoryName) {
        if (this.adjacentTerritories.containsKey(anotherTerritoryName))
            return true;

        return false;
    }

    /**
     * to be set immediately after construction and never then recalled.
     *
     * @param territories
     */
    public void setAdjacentTerritories(Map<String, Territory> territories) {
        this.adjacentTerritories = territories;
    }

    /**
     * Used to change the ownership of a territory, this method also handles updating
     * all ownership and number of territory fields for both the old and new owners
     *
     * @param newOwner
     */
    public void changeOwner(Player newOwner) {
        //remove old owneriship
        this.owner.getTerritoriesOwned().remove(this);
        setOwner(newOwner);
    }

    public void incrementNoOfArmiesPresent() {
        this.noOfArmiesPresent++;
    }

    public void decrementNoOfArmiesPresent() {
        this.noOfArmiesPresent--;
    }

    public void addArmies(int noOfArmiesToAdd) {
        this.noOfArmiesPresent += noOfArmiesToAdd;
    }

    public void removeArmies(int noOfArmiesToRemove) {
        this.noOfArmiesPresent -= noOfArmiesToRemove;
    }


    public void setNoOfArmiesPresent(int noOfArmiesPresent) {
        this.noOfArmiesPresent = noOfArmiesPresent;
    }

    public List<Territory> getAttackableTerritories() {
        List<Territory> attackableTerritories = new ArrayList<Territory>();

        for (Territory territory : getAdjacentTerritories().values()) {
            if (!territory.getOwner().equals(this.getOwner())) {
                attackableTerritories.add(territory);
            }
        }
        return attackableTerritories;
    }

    public List<Territory> getFortifyableTerritories() {
        List<Territory> fortifyableTerritories = new ArrayList<Territory>();

        for (Territory territory : getAdjacentTerritories().values()) {
            if (territory.getOwner().equals(this.getOwner())) {
                fortifyableTerritories.add(territory);
            }
        }
        return fortifyableTerritories;
    }



    @Override
    public String toString() {
        return new String("Territory Name: " + name + "\nOwner : " + owner.getUserName() + "\nNumber of Armies Present: " + noOfArmiesPresent + "\n");
    }

    public boolean playerOwnsTerritory(String playerName) {
        if (playerName.equals(this.owner.getUserName())) {
            return true;
        } else
            return false;
    }

    public int getNoOfArmiesPresent() {
        return noOfArmiesPresent;
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public Map<String, Territory> getAdjacentTerritories() {
        return adjacentTerritories;
    }


    public void setOwner(Player owner) {
        this.owner = owner;
        owner.getTerritoriesOwned().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Territory territory = (Territory) o;

        if (name != null ? !name.equals(territory.name) : territory.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

}
