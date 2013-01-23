package com.defeo.luke.hazardsp.UI;

import android.content.Context;
import android.content.res.AssetManager;
import com.defeo.luke.hazardsp.GameEngine.Territory;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Luke
 */

public class ClientWorldBuilder {
	Context context;

	public ClientWorldBuilder(Context context) {
		this.context = context;
	}

	// Read the world file
	public Map<String, Territory> readWorldFile() {
		// Map representing the world
		Map<String, Territory> world = new LinkedHashMap<String, Territory>(80);
        int counter = 0;
		try {
			// Open spriteFactory list data
			AssetManager am = context.getAssets();
			InputStream is = am.open("worldmap.mp3");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(isr);
			String line;
			// Create territories, read in names of territories
			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.split(",");
                world.put(tokens[0], new Territory(tokens[0], counter));
                counter++;
			}

			is = am.open("worldmap.mp3");
			isr = new InputStreamReader(is);
			bufferedReader = new BufferedReader(isr);

			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.split(",");
				// Get the territory the current line of the file is referring to
				Territory currentLineTerritory = world.get(tokens[0]);
				// Generate blank map to represent adjacent territories for this lines territories
				Map<String, Territory> adjacentTerritories = new HashMap<String, Territory>();
				// For each of the tokens on the line (adjacent territories)
				for (int i = 1; i < tokens.length; i++) {
					Territory adjTerritory = world.get(tokens[i]);
					adjacentTerritories.put(adjTerritory.getName(),
							adjTerritory);
				}
				currentLineTerritory
						.setAdjacentTerritories(adjacentTerritories);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Graph File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return world;
	}
}