package com.defeo.luke.hazardsp.Client;

import android.app.Activity;
import com.defeo.luke.hazardsp.Activities.MapActivity;
import com.defeo.luke.hazardsp.GameEngine.ServerGame;
import com.defeo.luke.hazardsp.UI.*;


/**
 * @author Sittikorn Assavanives
 */
public class Client {

	private EventHandler eventHandler;
	private MapActivity mapActivity;
	private Activity currentActivity;
    private GameUpdater gameUpdater;
    private ServerGame game;


    // Static client object so we can access this globally
	private static Client staticClient;
	
	// Get the static client
	public static Client get() {
		return staticClient;
	}

	public Client() {
        staticClient = this;
    }

	public GameUpdater getGameUpdater() {
		return gameUpdater;
	}

	
	public EventHandler getEventHandler() {
		return eventHandler;
	}
	
	public void setEventHandler(EventHandler eventHandler){
		this.eventHandler = eventHandler;
	}
	
	public MapActivity getMapActivity() {
		return mapActivity;
	}

	public void setMapActivity(MapActivity mapActivity) {
		this.mapActivity = mapActivity;
	}

	public void setGameUpdater(GameUpdater gameUpdater) {
		this.gameUpdater = gameUpdater;
	}

	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}


    public ServerGame getGame() {
        return game;
    }

    public void setGame(ServerGame game) {
        this.game = game;
    }
}