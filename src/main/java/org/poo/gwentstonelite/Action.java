package org.poo.gwentstonelite;

import org.poo.fileio.CardInput;
import org.poo.gwentstonelite.cards.Card;
import org.poo.gwentstonelite.cards.heroes.EmpressThorina;
import org.poo.gwentstonelite.cards.heroes.GeneralKocioraw;
import org.poo.gwentstonelite.cards.heroes.KingMudface;
import org.poo.gwentstonelite.cards.heroes.LordRoyce;
import org.poo.gwentstonelite.cards.minions.Berserker;
import org.poo.gwentstonelite.cards.minions.Goliath;
import org.poo.gwentstonelite.cards.minions.Sentinel;
import org.poo.gwentstonelite.cards.minions.Warden;
import org.poo.gwentstonelite.cards.special_cards.Disciple;
import org.poo.gwentstonelite.cards.special_cards.Miraj;
import org.poo.gwentstonelite.cards.special_cards.TheCursedOne;
import org.poo.gwentstonelite.cards.special_cards.TheRipper;

import java.util.ArrayList;

public final class Action {

    /**
     * Converts a list of decks containing CardInput objects into
     * a new list of decks containing Card objects.
     *
     * @param decks the list of decks to be converted. This parameter is final,
     *              meaning the original list cannot be modified within the method.
     * @return a new list of decks, where each deck is an ArrayList of Card objects.
     */
    public ArrayList<ArrayList<Card>> changeDecks(final ArrayList<ArrayList<CardInput>> decks) {
        ArrayList<ArrayList<Card>> newDecks = new ArrayList<>();
        Card newCard;

        for (ArrayList<CardInput> deck : decks) {
            ArrayList<Card> newDeck = new ArrayList<>();

            for (CardInput card : deck) {
                newCard = new Card(card);
                addCard(newCard, newDeck);
            }

            newDecks.add(newDeck);
        }

        return newDecks;
    }

    /**
     * Adds a specific type of Card to the provided deck based on the card's name.
     * Depending on the card's name, this method creates an instance of
     * the corresponding subclass and adds it to the deck.
     *
     * @param card the Card object containing information about the card to be added.
     *             The type of card added to the deck is determined by this card's name.
     * @param deck the ArrayList of card objects representing the deck where
     *             the new card will be added.
     */
    public void addCard(final Card card, final ArrayList<Card> deck) {
        switch (card.getName()) {
            case "Sentinel" :
                deck.add(new Sentinel(card));
                break;
            case "Berserker":
                deck.add(new Berserker(card));
                break;
            case "Goliath":
                deck.add(new Goliath(card));
                break;
            case "Warden":
                deck.add(new Warden(card));
                break;
            case "Miraj":
                deck.add(new Miraj(card));
                break;
            case "Disciple":
                deck.add(new Disciple(card));
                break;
            case "The Ripper":
                deck.add(new TheRipper(card));
                break;
            case "The Cursed One":
                deck.add(new TheCursedOne(card));
                break;

            default:
        }
    }

    /**
     * Assigns a hero card to the specified player based on the hero's name.
     * This method creates an instance of the appropriate hero subclass
     * and sets it as the player's hero card.
     *
     * @param player the Player to whom the hero card will be assigned.
     * @param hero   the CardInput containing the name of the hero to be assigned.
     *               The hero card is determined by this name, and the
     *               corresponding hero subclass instance is created and set for the player.
     */
    public void setHeroForPlayer(final org.poo.gwentstonelite.Player player, final CardInput hero) {
        switch (hero.getName()) {
            case "Lord Royce":
                player.setHeroCard(new LordRoyce(hero));
                break;
            case "Empress Thorina":
                player.setHeroCard(new EmpressThorina(hero));
                break;
            case "King Mudface":
                player.setHeroCard(new KingMudface(hero));
                break;
            case "General Kocioraw":
                player.setHeroCard(new GeneralKocioraw(hero));
                break;
            default:
        }
    }

    /**
     * Resets the state of all cards on the specified rows of the game board.
     * This method sets the `frozen` and `attacked` status of each card on the given rows to false.
     *
     * @param board the ArrayList of ArrayList<Card> representing
     *              the game board, where each inner list
     *              represents a row of cards.
     * @param row1  the index of the first row to reset on the board.
     * @param row2  the index of the second row to reset on the board.
     */
    public void resetCardsFromBoard(final ArrayList<ArrayList<Card>> board,
                                    final int row1, final int row2) {

        for (Card card : board.get(row1)) {
            card.setFrozen(false);
            card.setAttacked(false);
        }

        for (Card card : board.get(row2)) {
            card.setFrozen(false);
            card.setAttacked(false);
        }

    }

    /**
     * Resets the state of the player's hero card
     * by setting its `frozen` and `attacked` status to false.
     *
     * @param player the Player whose hero card will be reset.
     */
    public void resetHero(final org.poo.gwentstonelite.Player player) {
        player.getHeroCard().setFrozen(false);
        player.getHeroCard().setAttacked(false);
    }


    /**
     * Determines the row type ("front" or "back") for a given card based on its name.
     * If the card is one of "The Ripper", "Miraj", "Goliath", or "Warden",
     * it is assigned to the "front" row;
     * otherwise, it is assigned to the "back" row.
     *
     * @param card the Card whose row type is to be determined.
     * @return a sting representing the row type:
     *      "front" for specified card names,
     *      "back" otherwise.
     */
    public String checkRow(final Card card) {
        return switch (card.getName()) {
            case "The Ripper", "Miraj", "Goliath", "Warden" -> "front";
            default -> "back";
        };
    }

    /**
     * Determines whether a given card is classified as a "tank" based on its name.
     * A card is considered a tank if its name is "Goliath" or "Warden".
     *
     * @param card the Card to be checked.
     * @return true if the card is a tank,
     *         false otherwise.
     */
    public boolean isTank(final Card card) {
        return switch (card.getName()) {
            case "Goliath", "Warden" -> true;
            default -> false;
        };
    }
}
