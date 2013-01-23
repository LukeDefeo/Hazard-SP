package com.defeo.luke.hazardsp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.Client.GameUpdater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Luke
 * Date: 22/01/2013
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class SinglePlayerActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
    }

    public void button(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        List<String> playersTemp = new ArrayList<String>();
        playersTemp.add("player1");
        playersTemp.add("player2");
        GameUpdater gameUpdater = new GameUpdater(playersTemp,this);
        Client.get().setGameUpdater(gameUpdater);
        gameUpdater.startGame(playersTemp);

        startActivity(intent);

    }
}