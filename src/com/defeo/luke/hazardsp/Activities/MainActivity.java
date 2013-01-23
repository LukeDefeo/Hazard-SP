package com.defeo.luke.hazardsp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.defeo.luke.hazardsp.Client.Client;
//import com.defeo.luke.hazardsp.Client.Client;

/**
 * @author sxa287
 *         Song: https://www.youtube.com/watch?v=PpKZ1-xCNhM
 */
public class MainActivity extends HazardActivity {
    Button singleplayerButton, multiplayerButton, helpButton, creditsButton;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        new Client();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // Create game button
    public void singlePlayerButton(View view) {
        intent = new Intent(this, SinglePlayerActivity.class);
        // Go to create game screen
        startActivity(intent);
    }

    // Join game button
    public void multiplayerButton(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Coming Soon...!!!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // User info. button
    public void helpButton(View view) {
        intent = new Intent(this, HelpActivity.class);
        // Go to user info. screen
        startActivity(intent);
    }

    // Logout button
    public void creditsButton(View view) {
        intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }


    // Change buttons' colour
    private void initButtons() {
        singleplayerButton = (Button) findViewById(R.id.button_singlePlayer);
        singleplayerButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFF8DC, 0));
        multiplayerButton = (Button) findViewById(R.id.button_multiplayer);
        multiplayerButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFF8DC, 0));
        helpButton = (Button) findViewById(R.id.button_help);
        helpButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFF8DC, 0));
        creditsButton = (Button) findViewById(R.id.button_credits);
        creditsButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFF8DC, 0));
    }
}