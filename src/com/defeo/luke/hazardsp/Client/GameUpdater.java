package com.defeo.luke.hazardsp.Client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import com.defeo.luke.hazardsp.Activities.MainActivity;
import com.defeo.luke.hazardsp.Activities.R;
import com.defeo.luke.hazardsp.GameEngine.*;
import com.defeo.luke.hazardsp.UI.ClientContinentBuilder;
import com.defeo.luke.hazardsp.UI.ClientWorldBuilder;
import com.defeo.luke.hazardsp.UI.EventHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Luke De Feo
 */
public class GameUpdater {
    public static final File worldFile = new File("./Maps/World.map");
    public static final File continentFile = new File("./Maps/Continents.map");
    ServerGame game;
    List<String> gameRoom;
    private Context context;

    public GameUpdater(List<String> playerNames, Context context) {
        this.gameRoom = playerNames;
        this.context = context;
    }

    // When the game is over, show alert dialog to all players about the winner and losers
    private void handleGameOverUpdate(Player winner) {
        Log.i("GAMEUPDATER", "WINNER : " + winner.getUserName());
        final Player winningPlayer = winner;
        if (winningPlayer.equals(Client.get().getEventHandler().getPlayer())) {
            Client.get().getCurrentActivity().runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(Client.get().getCurrentActivity())
                    .setTitle("Game Over")
                    .setMessage("Congratulations you are victorious.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int which) {
                    		Intent intent = new Intent(Client.get().getCurrentActivity(), MainActivity.class);
                    		Client.get().getCurrentActivity().startActivity(intent);
                    	}
                    })
                    .show()
                    .setIcon(R.drawable.done);
                    Client.get().getEventHandler().playSound(EventHandler.Sounds.ENDGAME);
                }
            });
        } else {
            Client.get().getCurrentActivity().runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(Client.get().getCurrentActivity())
                    .setTitle("Game Over")
                    .setMessage(winningPlayer.getUserName() + " has won the Game.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int which) {
                    		Intent intent = new Intent(Client.get().getCurrentActivity(), MainActivity.class);
                    		Client.get().getCurrentActivity().startActivity(intent);
                    	}
                    })
                    .show()
                    .setIcon(R.drawable.r_u_sure);
                    Client.get().getEventHandler().playSound(EventHandler.Sounds.ENDGAME);
                }
            });
        }
    }

    // When the player is defeated, show alert dialog to all players about the defeated player
    private void handlePlayerKilledUpdate(Player player) {
        final Player playerKilled = game.getPlayers().get(player);
        if (playerKilled.equals(Client.get().getEventHandler().getPlayer())) {
            Client.get().getCurrentActivity().runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(Client.get().getCurrentActivity())
                    .setTitle("Game Over")
                    .setMessage("You have been Defeated!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int which) {
                    		dialog.cancel();
                    	}
                    })
                    .setNeutralButton("Main Menu", new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int which) {
                    		Intent intent = new Intent(Client.get().getCurrentActivity(), MainActivity.class);
                    		Client.get().getCurrentActivity().startActivity(intent);
                    	}
                    })
                    .show()
                    .setIcon(R.drawable.done);
                }
            });
        } else {
            Client.get().getCurrentActivity().runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(Client.get().getCurrentActivity())
                    .setTitle("Player Defeted")
                    .setMessage(playerKilled.getUserName() + " has been defeated")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int which) {
                    		dialog.cancel();
                    	}
                    })
                    .show()
                    .setIcon(R.drawable.done);
                }
            });
        }
    }

    // When the game is started, initialise the game
    public synchronized void startGame(List<String> humanPlayers, List<String> aiTemp) {
        ClientWorldBuilder worldBuilder = new ClientWorldBuilder(context);
        Map<String, Territory> world = worldBuilder.readWorldFile();
        ClientContinentBuilder continentBuilder = new ClientContinentBuilder(world, context);
        List<Continent> continents = continentBuilder.readContinentFile();
        Map<String, Player> players = new HashMap<String, Player>();

        int playerCounter = 1;
        for (String playerID : humanPlayers) {
            Player player = new Player(playerID, continents,playerCounter,false);
            playerCounter++;
            Log.i("GAMEUPDDATER", " player in " + player.getUserName());
            players.put(player.getUserName(), player);
        }

        for (String ai : aiTemp) {
            Player player = new Player(ai, continents,playerCounter,true);
            playerCounter++;
            players.put(player.getUserName(), player);
        }

        Player firstPlayerToGo = players.get(humanPlayers.get(0));
        firstPlayerToGo.setCurrentPhase(Player.Phase.REINFORCE);
        game = new ServerGame(world, continents, players, firstPlayerToGo);
        game.initialiseGame();
        Client.get().setGame(game);

    }



    public static File getWorldFile() {
        return worldFile;
    }

    public static File getContinentFile() {
        return continentFile;
    }

    public ServerGame getGame() {
        return game;
    }

    public List<String> getGameRoom() {
        return gameRoom;
    }
}