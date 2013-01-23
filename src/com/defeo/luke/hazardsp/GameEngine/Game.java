package com.defeo.luke.hazardsp.GameEngine;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 26/10/2012
 * Time: 13:46
 */


public abstract class Game {

    private final List<Continent> continents;
    private Map<String,Player> players;
    private Map<String,Territory> world;
    private Player currentPlayerTurn;


    public Game(Map<String,Territory> world, List<Continent> continents, Map<String,Player> players, Player currentPlayerTurn) {
        this.world = world;
        this.continents = continents;
        this.players = players;
        this.currentPlayerTurn = currentPlayerTurn;
    }


    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, Territory> getWorld() {
        return world;
    }

    public List<Continent> getContinents() {
        return continents;
    }


    public Player getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }


    public void setCurrentPlayerTurn(Player currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
    }
}
