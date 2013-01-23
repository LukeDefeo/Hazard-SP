package com.defeo.luke.hazardsp.GameEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 09/11/2012
 * Time: 14:18
 */
public class AttackProcessor {


    public List<Integer> doAttack(int noAttackingArmies, int noDefendingArmies) {
//        System.out.println("attacking armies: " + noAttackingArmies);
//        System.out.println("defending armies " + noDefendingArmies);
        int attackingArmiesDestroyed = 0;
        int defendingArmiesDestroyed = 0;

        //Generate lists of ints to represent die
        List<Integer> attackingRolls = new ArrayList<Integer>();
        for (int i = 0; i < noAttackingArmies; i++)
            attackingRolls.add(rollDice());

        List<Integer> defendingRolls = new ArrayList<Integer>();
        for (int i = 0; i < noDefendingArmies; i++)
            defendingRolls.add(rollDice());

        //sort both lists in decending order
        Collections.sort(attackingRolls, Collections.reverseOrder());
        Collections.sort(defendingRolls, Collections.reverseOrder());

//        System.out.println("attacking rolls" + attackingRolls);
//        System.out.println("defending rolls" + defendingRolls);

        //do risk attack (accorinding to risk rules)
        for (Integer defendingRoll : defendingRolls) {
            //first defending army destroyed the first attacking??

            if(attackingRolls.isEmpty())
                break;
            if (defendingRoll >= attackingRolls.remove(0))
                attackingArmiesDestroyed++;
            else
                defendingArmiesDestroyed++;
        }
//        System.out.println("attacking Armies Destroyed: " + attackingArmiesDestroyed);
//        System.out.println("defending Armies Destroyed: " + defendingArmiesDestroyed);
//        System.out.println("");

        List<Integer> result = new ArrayList<Integer>();
        result.add(attackingArmiesDestroyed);
        result.add(defendingArmiesDestroyed);

        return result;
    }

    private int rollDice() {
        return (int) (6.0 * Math.random()) + 1;
    }


}
