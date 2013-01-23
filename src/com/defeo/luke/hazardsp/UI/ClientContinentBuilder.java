package com.defeo.luke.hazardsp.UI;

import android.content.Context;
import android.content.res.AssetManager;
import com.defeo.luke.hazardsp.GameEngine.*;



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Luke
 */
public class ClientContinentBuilder {
	private Map<String,Territory> world;
	private Context context;

	public ClientContinentBuilder(Map<String,Territory> world, Context context) {
		this.world=world;
		this.context = context;
	}

	/*
	 * Read the continent file
	 */
	public List<Continent> readContinentFile() {
		List<Continent> continents = new ArrayList<Continent>();
		try {
			// Open spriteFactory list data
			AssetManager am = context.getAssets();
			InputStream is = am.open("continentmap.mp3");

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(isr);

			String line;

			// Create Continents, read in names of Continents
			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.split(",");

				String continentName = tokens[0];
				int continentArmyBonus = Integer.parseInt(tokens[1]);

				List<Territory> territoriesInContinent = new ArrayList<Territory>();
				for (int i = 2; i < tokens.length; i++) {
					Territory territoryInContinent = world.get(tokens[i]);
					territoriesInContinent.add(territoryInContinent);
				}
				Continent continent = new Continent(continentName,continentArmyBonus,territoriesInContinent);
				continents.add(continent);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Graph File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return continents;
	}
}