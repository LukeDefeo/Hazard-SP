package com.defeo.luke.hazardsp.GameEngine;

/**
 * Created with IntelliJ IDEA.
 * Author: Luke
 * Date: 01/11/2012
 * Time: 15:17
 */
public class BonusCard {
    public enum CardType {INFANTRY, CAVALRY, ARTILLARY}
    private CardType cardType;

    public BonusCard() {
        double randomNumber = Math.random();
        if (randomNumber < 0.3333)
            cardType = CardType.INFANTRY;
        if((randomNumber <=0.3333) && (randomNumber < 0.6666))
            cardType= CardType.CAVALRY;
        if(randomNumber>=0.6666)
            cardType = CardType.ARTILLARY;

    }

    public CardType getCardType() {
        return cardType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BonusCard bonusCard = (BonusCard) o;

        if (cardType != bonusCard.cardType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cardType != null ? cardType.hashCode() : 0;
    }
}
