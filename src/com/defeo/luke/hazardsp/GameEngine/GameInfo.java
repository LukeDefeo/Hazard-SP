/**
 * 
 */
package com.defeo.luke.hazardsp.GameEngine;

import java.io.Serializable;


/**
 * @author ben
 *
 */
public class GameInfo implements Serializable {
	
	private int id;
	private String name;
	private GameSettings settings;
	private int currentNumPlayers;
	
	public GameInfo(int id, String name, GameSettings settings, int currentNumPlayers){
		this.setId(id);
		this.setName(name);
		this.setSettings(settings);
		this.setCurrentNumPlayers(currentNumPlayers);
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the settings
	 */
	public GameSettings getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	private void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	/**
	 * @return the currentNumPlayers
	 */
	public int getCurrentNumPlayers() {
		return currentNumPlayers;
	}

	/**
	 * @param currentNumPlayers the currentNumPlayers to set
	 */
	private void setCurrentNumPlayers(int currentNumPlayers) {
		this.currentNumPlayers = currentNumPlayers;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	private void setId(int id) {
		this.id = id;
	}
	
}
