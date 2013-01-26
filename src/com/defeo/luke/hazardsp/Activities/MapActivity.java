package com.defeo.luke.hazardsp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.UI.MapView;


/**
 * @author Thomas Yorshire
 */
public class MapActivity extends HazardActivity {
    AlertDialog.Builder builder;
    AlertDialog alert;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Client.get().setMapActivity(this);
        // Create mapview object
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MapView mapView = new MapView(this, metrics.heightPixels, metrics.widthPixels);
        //Start the map!
        setContentView(mapView);
        Toast toast = Toast.makeText(Client.get().getCurrentActivity(), "Tap on territories to reinforce", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    // Back button to go back to main screen
    public void onBackPressed() {
        intent = new Intent(this, MainActivity.class);
        // Dialog box to confirm quitting game room action
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to leave?")
                .setMessage("Warning: You will not be able to rejoin this game!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show()
                .setIcon(R.drawable.r_u_sure);
    }
}