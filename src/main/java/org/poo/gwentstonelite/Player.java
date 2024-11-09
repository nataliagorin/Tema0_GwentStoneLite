package org.poo.gwentstonelite;

import org.poo.gwentstonelite.cards.Card;

import java.util.ArrayList;

public final class Player {
    private ArrayList<ArrayList<Card>> decks;
    private ArrayList<Card> handCards;
    private ArrayList<Card> cardsToPlay;
    private Card heroCard;
    private int gamesWon;
    private int mana;

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(final ArrayList<Card> handCards) {
        this.handCards = handCards;
    }

    public ArrayList<Card> getCardsToPlay() {
        return cardsToPlay;
    }

    public void setCardsToPlay(final ArrayList<Card> cardsToPlay) {
        this.cardsToPlay = cardsToPlay;
    }

    public Card getHeroCard() {
        return heroCard;
    }

    public void setHeroCard(final Card heroCard) {
        this.heroCard = heroCard;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(final int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }
}
