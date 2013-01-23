package com.defeo.luke.hazardsp.UI;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import com.defeo.luke.hazardsp.Activities.R;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.GameEngine.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * This class creates and stores references to all the sprites used within the
 * MapView class
 * 
 * @author Thomas Yorkshire
 * 
 */
public class SpriteFactory {

	// All on screen spriteFactory objects
	ArrayList<TerritorySprite> territorySprites;
	ArrayList<ButtonSprite> buttonSprites;
	ArrayList<PlayerButtonSprite> playerButtonSprites;
	ArrayList<DisplaySprite> displaySprites;
	PaintPalette paintPalette;

	/**
	 * Creates all Sprite objects
	 * 
	 * @param context
	 * @throws java.io.IOException
	 */
	public SpriteFactory(Context context, float height, float width)
			throws IOException {

		paintPalette = new PaintPalette((int) height, (int) width);

		// Create Territory Sprites
		// Iterator<Map.Entry<String,Territory>> territoryItterator =
		// Client.get().getClientGameUpdater().getGame().getWorld().entrySet().iterator();
		territorySprites = new ArrayList<TerritorySprite>();
		buttonSprites = new ArrayList<ButtonSprite>();
		playerButtonSprites = new ArrayList<PlayerButtonSprite>();
		displaySprites = new ArrayList<DisplaySprite>();

		// Set up the paint (for territories)
		Paint tpaint = new Paint();
		tpaint.setStyle(Paint.Style.FILL);
		tpaint.setColor(Color.WHITE);
		tpaint.setStrokeWidth(2);
		tpaint.setTextAlign(Paint.Align.CENTER);
		tpaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		tpaint.setTextSize(context.getResources()
				.getDimension(R.dimen.textsize));
		tpaint.setTypeface(Typeface.DEFAULT_BOLD);

		// Set up the paint (for player lists)
		Paint ppaint = new Paint();
		ppaint.setStyle(Paint.Style.FILL);
		ppaint.setColor(Color.WHITE);
		ppaint.setStrokeWidth(2);
		ppaint.setTextAlign(Paint.Align.LEFT);
		ppaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		ppaint.setTextSize(context.getResources()
				.getDimension(R.dimen.textsize));
		ppaint.setTypeface(Typeface.DEFAULT);
		
		//Set up the paint (for reinforce)
		Paint rpaint = new Paint();
		rpaint.setStyle(Paint.Style.FILL);
		rpaint.setColor(Color.WHITE);
		rpaint.setStrokeWidth(2);
		rpaint.setTextAlign(Paint.Align.LEFT);
		rpaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		rpaint.setTextSize(context.getResources()
				.getDimension(R.dimen.textsizere));
		rpaint.setTypeface(Typeface.DEFAULT);


		// Set up the line
		Paint pline = new Paint();
		pline.setStyle(Paint.Style.STROKE);
		pline.setColor(Color.GRAY);
		pline.setStrokeWidth(2);
		pline.setFlags(Paint.ANTI_ALIAS_FLAG);

		// Open spriteFactory list data
		AssetManager am = context.getAssets();
		InputStream is = am.open("territory_paths.mp3");

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bufferedReader = new BufferedReader(isr);

		String line;
		int itt = 0;
		Path currentPath;
		Map<String, Territory> world = Client.get().getGame().getWorld();
		Iterator<Map.Entry<String, Territory>> territoryIterator = world
				.entrySet().iterator();

		while ((line = bufferedReader.readLine()) != null) {

			float scaleFactorX = (1f / 1440f) * (width);
			float scaleFactorY = (1f / 1024f) * (height);

			String mainLine;
			float centreX = 0;
			float centreY = 0;
			boolean hasAt = false;

			if (line.contains("@")) {
				hasAt = true;
				String[] lineParts = line.split("@");
				mainLine = lineParts[0];

				String[] centreParts = lineParts[1].split(":");
				centreX = Float.parseFloat(centreParts[0]) * scaleFactorX;
				centreY = (1000 - Float.parseFloat(centreParts[1]))
						* scaleFactorY;
				System.out.println("SCX" + scaleFactorX + "SCY" + scaleFactorY);
				System.out.println("Before X :"
						+ Float.parseFloat(centreParts[0]) + "Before Y :"
						+ (1000 - Float.parseFloat(centreParts[1])));

				System.out.println("After X :" + centreX + "After Y :"
						+ centreY);

			} else {
				mainLine = line;
			}

			String[] tokens = mainLine.split(",");

			// Create the path item
			currentPath = new Path();

			// FIRST MOVE ITEM
			String[] cof = tokens[0].split(":");

			float x = Float.parseFloat(cof[0]) * (scaleFactorX);
			float y = Float.parseFloat(cof[1]) * (scaleFactorY);

			currentPath.moveTo(x, y);

			boolean go = false;

			for (String s : tokens) {

				if (go) {
					String[] co = s.split(":");

					x = x + Float.parseFloat(co[0]) * (scaleFactorX);
					y = y + Float.parseFloat(co[1]) * (scaleFactorY);

					currentPath.lineTo(x, y);

				}

				go = true;
			}

			// Final co ordinate to be the inital co-ordinate
			x = Float.parseFloat(cof[0]) * (scaleFactorX);
			y = Float.parseFloat(cof[1]) * (scaleFactorY);

			currentPath.lineTo(x, y);

			// Log.i("INFO","here1");
			// Territory territory = null;// =
			// territoryItterator.next().getValue();

			// Create the territory spriteFactory
			TerritorySprite newSprite = new TerritorySprite(currentPath, pline,
					territoryIterator.next().getValue(), paintPalette);
			newSprite.setTextPaint(tpaint);
			newSprite.setText("" + ++itt);

			if (hasAt) {
				newSprite.setCentrePoint(centreY, centreX);
			}

			territorySprites.add(newSprite);
			// Log.i("INFO","here2");

		}

		/*
		 * Create the button sprites
		 */

		// String list for all button string values

		String[] stringList = { "Reinforce", "Attack", "Fortify", "Player 1",
				"Player 2", "Player 3", "Player 4", "Player 5", "Player 6",
				"Armies", "  ", ">>>" };

		// Open spriteFactory list data for the buttons
		am = context.getAssets();
		is = am.open("button_paths.mp3");

		isr = new InputStreamReader(is);
		bufferedReader = new BufferedReader(isr);

		// Reset the itterator to 0
		itt = 0;

		while ((line = bufferedReader.readLine()) != null) {
			String[] tokens = line.split(",");

			// Create the path item
			currentPath = new Path();

			float scaleFactorX = (1f / 1440f) * (width);
			float scaleFactorY = (1f / 1024f) * (height);

			// FIRST MOVE ITEM
			String[] cof = tokens[0].split(":");

			float x = Float.parseFloat(cof[0]) * (scaleFactorX);
			float y = (Float.parseFloat(cof[1]) + 10) * (scaleFactorY);

			currentPath.moveTo(x, y);
			//
			boolean go = false;
			// //
			for (String s : tokens) {
				//
				if (go) {
					String[] co = s.split(":");

					// Log.i("//herp", co[0]);

					x = x + Float.parseFloat(co[0]) * (scaleFactorX);
					y = y + Float.parseFloat(co[1]) * (scaleFactorY);

					currentPath.lineTo(x, y);

				}

				go = true;
			}

			// Final co ordinate to be the inital co-ordinate
			x = Float.parseFloat(cof[0]) * (scaleFactorX);
			y = (Float.parseFloat(cof[1]) + 10) * (scaleFactorY);

			currentPath.lineTo(x, y);

			// Log.i("INFO","here1");
			// Territory territory = null;// =
			// territoryItterator.next().getValue();

			// Create the button spriteFactory

			if (itt == 11) {

				PlayerButtonSprite newSprite = new PlayerButtonSprite(
						currentPath, pline, paintPalette, stringList[itt]);
				newSprite.setTextPaint(tpaint);
				// newSprite.setText("" + ++itt);
				++itt;
				playerButtonSprites.add(newSprite);
				// Log.i("INFO","here2");

			} else if (itt <= 2 || itt == 10) {

				ButtonSprite newSprite = new ButtonSprite(currentPath, pline,
						paintPalette, stringList[itt]);
				newSprite.setTextPaint(tpaint);
				// newSprite.setText("" + ++itt);
				++itt;
				buttonSprites.add(newSprite);
				// Log.i("INFO","here2");
			} else {
				DisplaySprite newSprite = new DisplaySprite(currentPath, pline,
						paintPalette, stringList[itt]);
				newSprite.setTextPaint(ppaint);
				// newSprite.setText("" + ++itt);
				++itt;
				displaySprites.add(newSprite);
				// Log.i("INFO","here2");

			}

		}

	}

	public ArrayList<TerritorySprite> getTerritorySprites() {
		return territorySprites;
	}

	public ArrayList<ButtonSprite> getButtonSprites() {
		// TODO Auto-generated method stub
		return buttonSprites;
	}

	public ArrayList<PlayerButtonSprite> getPlayerButtonSprites() {
		return playerButtonSprites;
	}

	public ArrayList<DisplaySprite> getDisplaySprites() {
		return displaySprites;
	}

	public PaintPalette getPaintPalette() {
		return paintPalette;
	}
	
	

}
