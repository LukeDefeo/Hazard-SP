package com.defeo.luke.hazardsp.GameEngine;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Luke
 * Date: 22/10/2012
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class GameSettings implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean timeoutSetting = false;     // false if no time out setting
    boolean AIEnabledOnTimeout = false; //false if go forfitted after timeout, true if ai plays in players place.
    int timeoutTimeSeconds = 0;          //time after which go forfit / AI kicks in
    boolean passwordEnabled = false;
    int noOfAIPlayers = 0;          //must be less than max players
    int maxNoOfHumanPlayers = 2;
    final int maxPossiblePlayers = 6;

    public GameSettings(boolean timeoutSetting, boolean AIEnabledOnTimeout, int timeoutTimeSeconds, boolean passwordEnabled, int noOfAIPlayers, int maxNoOfHumanPlayers) {
        this.timeoutSetting = timeoutSetting;
        this.AIEnabledOnTimeout = AIEnabledOnTimeout;
        this.timeoutTimeSeconds = timeoutTimeSeconds;
        this.passwordEnabled = passwordEnabled;
        setMaxNoOfPlayers(maxNoOfHumanPlayers);
        setNoOfAIPlayers(noOfAIPlayers);

    }

    private void setMaxNoOfPlayers(int maxNoOfPlayers) {
        if (maxNoOfPlayers <= maxPossiblePlayers)
            this.maxNoOfHumanPlayers = maxNoOfPlayers;
        else
            this.maxNoOfHumanPlayers = maxPossiblePlayers;
    }

    private void setNoOfAIPlayers(int noOfAIPlayers) {
        if(noOfAIPlayers + this.maxNoOfHumanPlayers <= maxPossiblePlayers)
            this.noOfAIPlayers = noOfAIPlayers;
        else
            this.noOfAIPlayers= maxPossiblePlayers - maxNoOfHumanPlayers;
    }


    public void setPasswordEnabled(boolean passwordEnabled) {
        this.passwordEnabled = passwordEnabled;
    }

    public int getMaxPossiblePlayers() {
        return maxPossiblePlayers;
    }

    public boolean isTimeoutSetting() {
        return timeoutSetting;
    }

    public boolean isAIEnabledOnTimeout() {
        return AIEnabledOnTimeout;
    }

    public int getTimeoutTimeSeconds() {
        return timeoutTimeSeconds;
    }

    public boolean isPasswordEnabled() {
        return passwordEnabled;
    }

    public int getNoOfAIPlayers() {
        return noOfAIPlayers;
    }

    public int getMaxNoOfPlayers() {
        return maxPossiblePlayers;
    }


}
