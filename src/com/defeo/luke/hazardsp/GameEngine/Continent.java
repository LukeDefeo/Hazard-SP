package com.defeo.luke.hazardsp.GameEngine;

import java.util.List;

/**
 * @author Luke De Feo
 */
public class Continent {

    private String name;
    private int continentArmyBonus;
    private List<Territory> territoriesInContinent;

    public Continent(String continentName, int continentArmyBonus, List<Territory> territoriesInContinent) {
        this.name = continentName;
        this.continentArmyBonus = continentArmyBonus;
        this.territoriesInContinent = territoriesInContinent;

    }

    public String getName() {
        return name;
    }

    public int getContinentArmyBonus() {
        return continentArmyBonus;
    }

    public List<Territory> getTerritoriesInContinent() {
        return territoriesInContinent;
    }

    public boolean playerControlsContinent(List<Territory> playerControlledTerritories) {
        boolean playerOwnsContinent = false;
        for (Territory territory : territoriesInContinent) {
            if (!playerControlledTerritories.contains(territory))
                return false;
        }
        return true;
    }

}
