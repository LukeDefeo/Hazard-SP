package com.defeo.luke.hazardsp.AI;

import com.defeo.luke.hazardsp.GameEngine.MessageFactory;
import com.defeo.luke.hazardsp.GameEngine.Player;
import com.defeo.luke.hazardsp.GameEngine.*;
import com.defeo.luke.hazardsp.UI.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 * @author Megan Thomas
 *
 */

public class BobLogic extends Thread {
	private MessageFactory messageFactory;
	private double NBSR;
	private double BSR;
	private Player me;
//	private boolean isUpdated;
	private boolean moveIn;
	private boolean myTurn;
	private boolean reinforce;
	private boolean attack;
	private boolean fortify;
    private EventHandler eventHandler;

    public BobLogic(Player me, MessageFactory messageFactory, EventHandler eventHandler) {
		
		//initialise everything
		this.NBSR = 0.0;
		this.BSR = 0.0;
		this.moveIn = false;
		this.reinforce = false;
		this.attack = false;
		this.fortify = false;
        this.eventHandler = eventHandler;

		this.me = me;
		this.messageFactory = messageFactory;
		
	}	

	
	public void setPlayer(Player player) {
		this.me = player;
	}
	
	public void setMyTurn() {
		this.myTurn = true;
	}
	
	public void setReinforce() {
		this.reinforce = true;
	}
	
	public void setAttack() {
		this.attack = true;
	}
	
	public void setFortify() {
		this.fortify = true;
	}
	
	
	
	//gets territories owned from ClientGame object
	//returns a list of all territories with adjacent enemy territories
	private List<Territory> getBorderTerritories() {
		//System.out.println("getting border territories for " +me.getUserName());
		
		List<Territory> border = new ArrayList<Territory>();
		List<Territory> owned = me.getTerritoriesOwned();
				
		for(int i = 0; i < owned.size(); i++) {
			Map<String, Territory> adjacent = owned.get(i).getAdjacentTerritories();
			for(String j : adjacent.keySet()) {
				if(adjacent.get(j).getOwner() != me) {
					border.add(owned.get(i));
					break;
				}
			}
			
		}
		
		//System.out.println(border);
		return border;
		}
	
	
	/*for each border territory, get total no. of enemy armies adjacent (border security threat)
	 *divide by amount of armies in border territory (border security ratio)
	 **/ 
	private double calculateBSR(Territory t) {
		Map<String, Territory> adjacent = t.getAdjacentTerritories();
		int BST = 0;
		
		for(String i : adjacent.keySet()) {
			if(adjacent.get(i).getOwner() != t.getOwner()) {
				BST += adjacent.get(i).getNoOfArmiesPresent();
			}
		}
		
		BSR = (double)BST/t.getNoOfArmiesPresent();
		
		return BSR;
	}

	
	//normalises the BSR by dividing by the sum of all border territory BSR's
	private double calculateNBSR(Territory t) {
		//System.out.println("calculating NBSR for territory " +t.getName()+ ", owner is " +me.getUserName());
		
		List<Territory> border = getBorderTerritories();
		
		int sum = 0;
		
		for(int j = 0; j < border.size(); j++) {
			sum += calculateBSR(getBorderTerritories().get(j));
		}

		NBSR = calculateBSR(t)/sum;
		
		//System.out.println("NBSR is " +NBSR+ " for territory " + t.getName());
		return NBSR;
	}
	
	
	//takes in a territory and checks if it is an enemy territory
	//returns true if yes, false otherwise
	private Boolean isEnemyTerritory(Territory t) {
		if(t.getOwner() != me) return true;
		
		else return false;
	}
	
	//checks if a particular territory is a border territory
	private Boolean isBorderTerritory(Territory t) {
		Map<String, Territory> adjacent = t.getAdjacentTerritories();
		
		for(String j : adjacent.keySet()) {
			if(adjacent.get(j).getOwner() != me) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/*this method returns a list containing pairs of potential territories to attack, 
	 * together with the territory they are attacked from 
	 */
	private List<Pair<Territory, Territory>> getAttackable() {
		System.out.println("getting list of attackable territories for " +me.getUserName());
		
		List<Pair<Territory, Territory>> attackable = new ArrayList<Pair<Territory, Territory>>();
		List<Territory> border  = getBorderTerritories();
		
		//for each border territory, check if no. of armies present is > 1, if so return possible enemy territories to attack
		for(int i = 0; i < border.size(); i++) {
			Map<String, Territory> adjacent = border.get(i).getAdjacentTerritories();
		
			if(border.get(i).getNoOfArmiesPresent() > 1) {
				for(String j : adjacent.keySet()) {
					
					/*check if current adjacent territory is an enemy territory and add to possible attacks 
					 *if no. of armies in my territory is at least 1 more than no. of armies in enemy territory
					 */
					if((isEnemyTerritory(adjacent.get(j)) == true) && ((border.get(i).getNoOfArmiesPresent() - adjacent.get(j).getNoOfArmiesPresent()) >= 1)) {
						Pair<Territory, Territory> attack = new Pair<Territory, Territory>(adjacent.get(j), border.get(i));
						attackable.add(attack);
					}
				}
			}
		}
		
		for(int k = 0; k < attackable.size(); k++) {
			//System.out.println("attackable territory is " +attackable.get(k).getAttackable());
			//System.out.println("pair territory is " +attackable.get(k).getTerritory());
			//System.out.println("number of attackable territories is " +attackable.size());
		}
		return attackable;
	}
	
	
	/*get all border territories owned
	 *calculate NBSR for all territories
	 *reinforce territory every territory with NBSR over a certain value 
	 */
	public void reinforce() {
		System.out.println("Started Reinforce method for " +me.getUserName());
		
		double accept = 0.04;
		List<Territory> reinforce = new ArrayList<Territory>();
		List<Territory> border = getBorderTerritories();
		
		//System.out.println("border territories in reinforce method are: " +border);
		
		for(int i = 0; i < border.size(); i++) {
			if(calculateNBSR(border.get(i)) > accept) {
				
				reinforce.add(border.get(i));
			}
		}
		
		Territory first = reinforce.get(0);
		
		for(int k = 0; k < reinforce.size(); k++) {
			if(calculateNBSR(reinforce.get(k)) > calculateNBSR(first)) {
				first = reinforce.get(k);
			}
		}
        int timesToLoop = me.getNoOfArmiesToPlace();
        for(int j = 0; j < timesToLoop; j++) {
			messageFactory.sendReinforceMessage(first, me);
            try {
                System.out.println("sleeping1");
                Thread.sleep(3000);
                System.out.println("wakingup1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eventHandler.refreshScreen();
            System.out.println("reinforcing " +first.getName());
		}

		//messageFactory.sendEndTurnMessage(me);
	}
	
	/*if attackable list is not empty,decides which enemy territory to attack randomly 
	 * and attacks until either total no. of my armies is too low, or attackable list is empty
	 */
	public void attack() {
		int threshold = 10;
		Random gen = new Random();
		int count = 0;
		
		while(me.getTotalNoOfArmies() > threshold && count < 5) {
		System.out.println(count);
		System.out.println("Starting Attack method for " +me.getUserName());
		
		//calculate attackable territories
		List<Pair<Territory, Territory>> attackable = getAttackable();
		System.out.println("number of attackable territories is: " +attackable.size());
		
		
		if(attackable.size() > 0) {
			int j = gen.nextInt(attackable.size());
			System.out.println("random number is " +j);
			
			Territory attack = attackable.get(j).getAttackable();
			Territory attackFrom = attackable.get(j).getTerritory();
			System.out.println("territory to attack is " +attack.getName());
			
			//attack Territory attack from Territory attackFrom
			if(attackFrom.getNoOfArmiesPresent() > 3) {
				messageFactory.sendAttackMessage(attackFrom, attack, me, attack.getOwner(), 3);
				System.out.println("Attacking" +attack.getName()+ " from " +attackFrom.getName()+ " with 3 armies");
				
				
			}
			
			else if(attackFrom.getNoOfArmiesPresent() == 3){
				messageFactory.sendAttackMessage(attackFrom, attack, me, attack.getOwner(), 2);
				System.out.println("Attacking" + attack.getName() + " from " + attackFrom.getName() + " with 2 armies");
			}
			else {
				//moveIn = false;
			}
			
			//wait!
			//waitForUpdate();
			
			boolean keepAttacking = (!moveIn && attackFrom.getNoOfArmiesPresent() > 2 && (attackFrom.getNoOfArmiesPresent() - attack.getNoOfArmiesPresent()) >= 1);
			boolean attackAgain = (!moveIn && me.getTotalNoOfArmies() > threshold);
			
			//if attack is not defeated and attackFrom has enough armies, keep attacking the Territory 
			if(keepAttacking == true) {
				if(attackFrom.getNoOfArmiesPresent() > 3) {
					messageFactory.sendAttackMessage(attackFrom, attack, me, attack.getOwner(), 3);
					System.out.println("Attacking" + attack.getName() + " from " + attackFrom.getName() + " with 3 armies");
				}
				else if(attackFrom.getNoOfArmiesPresent() == 3) {
					messageFactory.sendAttackMessage(attackFrom, attack, me, attack.getOwner(), 2);
					System.out.println("Attacking" + attack.getName() + " from " + attackFrom.getName() + " with 2 armies");
				}
				
				//call a method waitForUpdate to wait until game object has been updated, then carry on
				//waitForUpdate();
			}
			else {
				//moveIn = false;
				//count++;
			}
			//if attackFrom does not have enough armies, but attack is not defeated and Bob has enough 
			//total armies, re-run the attack method to find somewhere new to attack from
			//else if(attackAgain == true) {
				//System.out.println("attacking again ");
				//attack();
			//}
			
			int movesFrom = attackFrom.getNoOfArmiesPresent();
			
			//if attack has been defeated, decide where to move armies depending on whether attack 
			//and attackFrom are border territories
			if(moveIn) {
				if(isBorderTerritory(attackFrom) == false && isBorderTerritory(attack) == true) {
					//move all armies into attack
					
						messageFactory.sendMoveIntoTerritoryMessage(movesFrom - 1, attackFrom, attack, me);
						System.out.println("Moving armies into " + attack.getName());
						moveIn = false;
					
					
					//call waitForUpdate method
					//waitForUpdate();
				}
				else if (isBorderTerritory(attackFrom) == true && isBorderTerritory(attack) == true) {
					//move half into attackFrom and half into attack
					
						messageFactory.sendMoveIntoTerritoryMessage(movesFrom / 2, attackFrom, attack, me);
						System.out.println("Moving armies into " + attack.getName());
						moveIn = false;
					
					
					
					//call waitForUpdate method
					//waitForUpdate();
				}
				else {
					messageFactory.sendMoveIntoTerritoryMessage(1, attackFrom, attack, me);
					moveIn = false;
				}
				
				
			}
			else {
				moveIn = false;
				count++;
				
			}
			
				
		}
		else {
			System.out.println("No territories to attack");
			moveIn = false;
			count++;             
			//break;
		}
		}
		
		moveIn = false;
		
		messageFactory.sendEndAttackMessage(me);
		System.out.println("Sent end of attack message");
		
	}
	
	
	//sets moveIn to true if agent is in MOVE_IN phase
	public  void setMoveIn() {
		this.moveIn = true;
	}
	
	
	
	/*for every territory owned which is not a border territory,
	 * calculate NBSR and add to fortifyFrom list if less than threshold
	 */  
	private List<Territory> fortifyFrom() {
		boolean nBorder = false;
		double threshold = 0.1;
		
		List<Territory> owned = me.getTerritoriesOwned();
		List<Territory> fortifyFrom = new ArrayList<Territory>();
		
		for(int i = 0; i < owned.size(); i++) {
			Map<String, Territory> adjacent = owned.get(i).getAdjacentTerritories();
			
			for(String j : adjacent.keySet()) {
				if(adjacent.get(j).getOwner() == me) nBorder = true;
					
				else if(adjacent.get(j).getOwner() != me) nBorder = false;
					
			}
			
			if(nBorder == true && (calculateNBSR(owned.get(i)) < threshold)) {
				fortifyFrom.add(owned.get(i));
			}
		}
		
		return fortifyFrom;
		
		
		
		
	}
	
	/* get all border territories and calculate NBSR, if NBSR is greater
	 * than threshold, add to fortifyTo
	 */
	/*private List<Territory> fortifyTo() {
		double threshold = 0.04;
		
		List<Territory> fortifyTo = new ArrayList<Territory>();
		List<Territory> border = getBorderTerritories();
		
		for(int i = 0; i < border.size(); i++) {
			if(calculateNBSR(border.get(i)) > threshold) {
				fortifyTo.add(border.get(i));
			}
		}
		
		return fortifyTo;
	}*/
	
	
	//method which chooses the territory with the greatest number of armies to fortify from, 
	//and the border territory with the least number of armies to fortify
	public void fortify() {
		System.out.println("Starting Fortify method for " +me.getUserName());
		
		
		List<Territory> fortifyFrom = fortifyFrom();
		//List<Territory> fortifyTo = fortifyTo();
		
		
		//find territory with most armies to fortify from
		if(fortifyFrom.size() > 0) {
			Territory from = fortifyFrom.get(0);
			//Territory to = fortifyTo.get(0);
			
			for(int i = 0; i < fortifyFrom.size(); i++) {
				if(fortifyFrom.get(i).getNoOfArmiesPresent() > from.getNoOfArmiesPresent()) {
					from = fortifyFrom.get(i);
				}
			}
			
			List<Territory> fortifyable = from.getFortifyableTerritories();
			Territory fortify = fortifyable.get(0);
			double nbsr = calculateNBSR(fortifyable.get(0));
			
			for(int k = 0; k < fortifyable.size(); k++) {
				if(calculateNBSR(fortifyable.get(k)) > nbsr) {
					fortify = fortifyable.get(k);
				}
			}
			
			//for(int j = 0; j < fortifyTo.size(); j++) {
				//if(fortifyTo.get(j).getNoOfArmiesPresent() < to.getNoOfArmiesPresent()) {
					//to = fortifyTo.get(j);
				//}
			//}
			
			messageFactory.sendFortifyMessage(me, from, fortify, from.getNoOfArmiesPresent(), fortify.getNoOfArmiesPresent());
			System.out.println("Fortifying " + fortify.getName());
		}
		else {
			System.out.println("no territories to fortify");
		}
		
		messageFactory.sendEndTurnMessage(me);
		System.out.println("ending fortify method");
		
		//return;
	}
	
	
	
	
	
	
	
	//inner class which creates a pair of items (used in attackable method to connect territory 
	//being attacked with the territory it is attacked from) 
	private class Pair<A, T> {
		private A attackable;
		private T territory;
		
		
		public Pair(A attackable, T territory) {
			this.attackable = attackable;
			this.territory = territory;
			
		}
		
		public A getAttackable() {
			return attackable;
		}
		
		public T getTerritory() {
			return territory;
		}

	}

    public Player getMe() {
        return me;
    }
}
