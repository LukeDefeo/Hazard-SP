package com.defeo.luke.hazardsp.AI;

import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.GameEngine.MessageFactory;
import com.defeo.luke.hazardsp.GameEngine.Player;
import com.defeo.luke.hazardsp.GameEngine.ServerGame;
import com.defeo.luke.hazardsp.UI.EventHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Megan Thomas
 */

public class AIManager {

    ServerGame game;
    Player currentAITurn;
    MessageFactory messageFactory;
    List<BobLogic> bobLogics;
    EventHandler eventHandler;


    public AIManager(MessageFactory messageFactory, ServerGame game, EventHandler eventHandler) {
        this.messageFactory = messageFactory;
        this.game = game;
        this.eventHandler = eventHandler;
        genLogic();
    }

    public void runAI(Player currentPlayerTurn) {
        if (!bobLogics.isEmpty()) {
            currentAITurn = currentPlayerTurn;
            for (BobLogic bobLogic : bobLogics) {
                if (bobLogic.getMe().equals(currentPlayerTurn)) {
                    final BobLogic logicTemp = bobLogic;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(750);
                                logicTemp.reinforce();
                                logicTemp.attack();
                                Thread.sleep(2000);
                                eventHandler.refreshScreen();
                                logicTemp.fortify();
                                eventHandler.refreshScreen();
                                Thread.sleep(500);


                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }


                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();

                }
            }

        }
    }


    private void genLogic() {
        List<BobLogic> bobs = new ArrayList<BobLogic>();
        for (Player player : game.getPlayers().values()) {
            if (player.isAI()) {
                bobs.add(new BobLogic(player, messageFactory,eventHandler));
            }
        }
        this.bobLogics = bobs;
    }
}