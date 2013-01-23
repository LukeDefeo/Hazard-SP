package com.defeo.luke.hazardsp.GameEngine;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 29/10/2012
 * Time: 16:19
 */

public class WorldBuilder {
    File worldFile;

    public WorldBuilder(File worldFile) {
        this.worldFile = worldFile;

    }

    public Map<String,Territory> readWorldFile() {
        //map representing the world
        Map<String,Territory> world = new LinkedHashMap<String, Territory>(80);
        int counter = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(worldFile));
            bufferedReader.mark(10000);

            String line;
            //Create territorys, read in names of territorys
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                world.put(tokens[0], new Territory(tokens[0],counter));
                counter++;
            }

            bufferedReader.reset();

            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                //get the territory the current line of the file is refering to
                Territory currentLineTerritory = world.get(tokens[0]);

                //generate blank Map to represent adjacent terriotrys for this lines territories
                Map<String,Territory> adjacentTerritories = new HashMap<String, Territory>();

                //for each of the tokens on the line (adjacent territorys)
                for (int i = 1; i < tokens.length; i++) {
                    Territory adjTerritory = world.get(tokens[i]);
                    adjacentTerritories.put(adjTerritory.getName(), adjTerritory);
                }
                currentLineTerritory.setAdjacentTerritories(adjacentTerritories);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Graph File " + worldFile + " not found");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return world;

    }

}
