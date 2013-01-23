package com.defeo.luke.hazardsp.UI;

//import android.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.GameEngine.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Thomas Yorkshire
 */
public class MapView extends View {

    EventHandler eventHandler;
    SpriteFactory spriteFactory;
    Context context;

    public MapView(Context context, int heightPixels, int widthPixels) {
        super(context);

        this.context = context;

        Log.i("VIEW HEIGHT", String.valueOf(heightPixels));
        Log.i("VIEW WITDTH", String.valueOf(widthPixels));

        // Create a pallete

        // Set up the map;
        try {
            spriteFactory = new SpriteFactory(context, (float) heightPixels,
                    (float) widthPixels);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        eventHandler = new EventHandler(this);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        // start at 0,0 and go to 0,max to use a vertical
        // gradient the full height of the screen.
        p.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.BLACK, 0xFF333333, TileMode.MIRROR));
        canvas.drawPaint(p);


        if (canvas == null) {
            Log.i("DRAW", "Canvas");
        }
        ;
        if (spriteFactory.getTerritorySprites() == null) {
            Log.i("DRAW", "Territory spriteFactory is null!");
        }
        ;

        // Draw each TERRITORY spriteFactory object
        for (TerritorySprite ts : spriteFactory.getTerritorySprites()) {

            if (ts == null) {
                Log.i("DRAW", "Territory spriteFactory is null!");
            }

            if (canvas == null) {
                Log.i("DRAW", "CANVAS is null!");
            }
            if (ts == null) {
                Log.i("DRAW", "ts is null");
            }


            canvas.drawPath(ts.getPath(), ts.getPaint());
            canvas.drawPath(ts.getPath(), ts.getLine());
        }

        // Draw the text for territories
        for (TerritorySprite ts : spriteFactory.getTerritorySprites()) {
            canvas.drawText(ts.getText(), ts.getY(), ts.getX(),
                    ts.getTextPaint());

            // Draw each BUTTON spriteFactory object
            for (int b = 0; b < 3; b++) {

                ButtonSprite bs = spriteFactory.getButtonSprites().get(b);
                canvas.drawPath(bs.getPath(), bs.getPaint());
                canvas.drawPath(bs.getPath(), bs.getLine());

                //Get text centre
                Rect textRect = new Rect();
                bs.getTextPaint().getTextBounds(bs.getText(), 0, 1, textRect);
                int textHeightCentre = textRect.height() / 2;

                canvas.drawText(bs.getText(), bs.getY(), bs.getX() + textHeightCentre,
                        bs.getTextPaint());

            }


            //Draw the player text button spriteFactory
            ButtonSprite bsa = spriteFactory.getButtonSprites().get(3);

            canvas.drawPath(bsa.getPath(), spriteFactory.getPaintPalette().getPaint(18));
            canvas.drawPath(bsa.getPath(), bsa.getLine());

            //Get text centre
//			Rect textRecta = new Rect();
//			bsa.getTextPaint().getTextBounds(bsa.getText(), 0, 1, textRecta);
//			int textHeightCentrea = textRecta.height()/2;

//			canvas.drawText(bsa.getText(), bsa.getY(), bsa.getX() + textHeightCentrea,
//					spriteFactory.getPaintPalette().getPaint(18));


            // Draw each PLAYER BUTTON spriteFactory object
            for (PlayerButtonSprite bs : spriteFactory.getPlayerButtonSprites()) {

                canvas.drawPath(bs.getPath(), bs.getPaint());
                canvas.drawPath(bs.getPath(), bs.getLine());

                //Get text centre
                Rect textRect = new Rect();
                bs.getTextPaint().getTextBounds(bs.getText(), 0, 1, textRect);
                int textHeightCentre = textRect.height() / 2;

                canvas.drawText(bs.getText(), bs.getY(), bs.getX() + textHeightCentre,
                        bs.getTextPaint());

            }

            ArrayList<Player> players = new ArrayList<Player>(Client.get().getGame().getPlayers().values());
            int totalMetric = Client.get().getGame().getWorld().size()*2;
            for (Player player : players) {
                totalMetric += player.getTotalNoOfArmies();
            }



            // Draw each Display BUTTON spriteFactory object
            for (int a = 0; a < players.size(); a++) {

                DisplaySprite ds = spriteFactory.getDisplaySprites().get(a);

                //Get text centre
                Rect textRect = new Rect();
                ds.getTextPaint().getTextBounds(players.get(a).getUserName(), 0, players.get(a).getUserName().length(), textRect);
                int textHeightCentre = textRect.height() / 2;
                int playerMetric = 100 * (players.get(a).getTotalNoOfArmies() + (players.get(a).getTerritoriesOwned().size() * 2)) / totalMetric;
                        canvas.drawText(players.get(a).getUserName() + " " + playerMetric + "%", ds.getY(), ds.getX() + textHeightCentre,
                        spriteFactory.getPaintPalette().getPaint((players.get(a).getGuiID()) + 11));
            }

            //Print out the reinforce armies
            DisplaySprite ds = spriteFactory.getDisplaySprites().get(6);
            canvas.drawText("" + Client.get().getGame().getCurrentPlayerTurn().getNoOfArmiesToPlace(), ds.getY(), ds.getX(),
                    ds.getTextPaint());


        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        boolean beenHandled = false;

        if (event.getAction() == 1) {

            // Determine which territory has been clicked...
            for (TerritorySprite ts : spriteFactory.getTerritorySprites()) {

                if (ts.isClicked(event.getX(), event.getY())) {

                    // Send to the event handler
                    //Log.i("TOUCH EVENT DETECTED", "SOFTWARE");

                    beenHandled = true;
                    if ((event.getEventTime() - event.getDownTime()) < 500)
                        eventHandler.touchEvent(ts);
                    else
                        eventHandler.longTouchEvent(ts);

                    break;
                }
            }

            //Check buttons
            if (!beenHandled) {

                //If next turn button

                if (spriteFactory.getPlayerButtonSprites().get(0).isClicked(event.getX(), event.getY())) {

                    eventHandler.touchEvent(spriteFactory.getPlayerButtonSprites().get(0));
                    beenHandled = true;
                }

            }

            if (!beenHandled) {

                eventHandler.touchEvent(new BackgroundSprite());

            }

        }
        return true;

    }

    SpriteFactory getSprites() {
        return spriteFactory;
    }
}
