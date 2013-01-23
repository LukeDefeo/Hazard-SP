package com.defeo.luke.hazardsp.GameEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 01/11/2012
 * Time: 14:15
 */
public class ContinentBuilder {
    File continentFile;
    private Map<String,Territory> world;

    public ContinentBuilder(File continentFile, Map<String,Territory> world) {
        this.continentFile = continentFile;
        this.world=world;
    }

        public List<Continent> readContinentFile() {
            List<Continent> continents = new ArrayList<Continent>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(continentFile));

                String line;

                //Create Continents, read in names of Contients
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
                System.out.println("Graph File " + continentFile + " not found");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return continents;
        }
    }

