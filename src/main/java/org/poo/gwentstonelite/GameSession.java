package org.poo.gwentstonelite;

import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.StartGameInput;
import org.poo.gwentstonelite.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class GameSession {
    public static final int MAXHEALTH = 30;
    public static final int MAXMANA = 10;
    public static final int MAXROWS = 4;
    private Player playerOne;
    private Player playerTwo;
    private ArrayList<ArrayList<Card>> board;
    private StartGameInput setupGame;
    private ArrayList<ActionsInput> actions;
    private int playerTurn;
    private int manaPerRound;
    private boolean playerOneEndTurn;
    private boolean playerTwoEndTurn;
    private boolean gameEnded;
    private GameCommand actionPerformer;

    public GameSession(final Player playerOne, final Player playerTwo, final GameInput game) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.board = new ArrayList<>();
        for (int i = 0; i < MAXROWS; i++) {
            board.add(new ArrayList<>());
        }

        this.setupGame = game.getStartGame();
        this.actions = game.getActions();
        this.actionPerformer = new GameCommand(this);
    }

    /**
     * Prepares the game by setting up players, their heroes, and initial game conditions.
     * This method is responsible for initializing game parameters at the start of the game,
     * including setting the starting player, mana, health, and the initial
     * cards in each player's deck.
     * It also assigns each player a hero card with full health and
     * shuffles their deck of cards based on the
     * provided shuffle seed. The players' hands are initialized as empty at the start.
     *
     * @param cardActions The Action object that is responsible for
     *                    setting up the cards for each player.
     */
    public void prepareGame(final Action cardActions) {
        playerTurn = setupGame.getStartingPlayer();
        manaPerRound = 0;
        playerOneEndTurn = false;
        playerTwoEndTurn = false;
        gameEnded = false;

        cardActions.setHeroForPlayer(playerOne, setupGame.getPlayerOneHero());
        playerOne.getHeroCard().setHealth(MAXHEALTH);

        cardActions.setHeroForPlayer(playerTwo, setupGame.getPlayerTwoHero());
        playerTwo.getHeroCard().setHealth(MAXHEALTH);

        playerOne.setMana(0);
        playerTwo.setMana(0);

        playerOne.setCardsToPlay(shuffleCards(playerOne.getDecks().
                get(setupGame.getPlayerOneDeckIdx()), setupGame.getShuffleSeed()));
        playerTwo.setCardsToPlay(shuffleCards(playerTwo.getDecks().
                get(setupGame.getPlayerTwoDeckIdx()), setupGame.getShuffleSeed()));

        playerOne.setHandCards(new ArrayList<>());
        playerTwo.setHandCards(new ArrayList<>());
    }

    /**
     * Shuffles the given deck of cards using a specified seed for randomization.
     * This method creates a new shuffled deck by adding cards from the original deck
     * and then shuffling them using the provided shuffle seed. The shuffle process ensures
     * a random order of the cards in the deck.
     *
     * @param deck The ArrayList of Card objects representing the deck to be shuffled.
     * @param shuffleSeed The seed value used to initialize the random number
     *                    generator for shuffling.
     *                    This allows for reproducible shuffling when using the same seed value.
     * @return An ArrayList of Card objects representing the shuffled deck.
     */
    public ArrayList<Card> shuffleCards(final ArrayList<Card> deck, final int shuffleSeed) {
        ArrayList<Card> shuffledDeck = new ArrayList<>();

        for (Card card : deck) {
            GwentStoneLite.getCardActions().addCard(card, shuffledDeck);
        }

        Collections.shuffle(shuffledDeck, new Random(shuffleSeed));
        return shuffledDeck;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public ArrayList<ActionsInput> getActions() {
        return actions;
    }

    public void setActions(final ArrayList<ActionsInput> actions) {
        this.actions = actions;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(final int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setPlayerOneEndTurn(final boolean playerOneEndTurn) {
        this.playerOneEndTurn = playerOneEndTurn;
    }

    public void setPlayerTwoEndTurn(final boolean playerTwoEndTurn) {
        this.playerTwoEndTurn = playerTwoEndTurn;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(final boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Starts the game by initializing the first round and performing
     * actions for each player until the game ends.
     * This method increments the total games played counter, sets up the game for the first round,
     * and then processes each action in the list of ActionsInput. After each action is performed,
     * it checks if both players have ended their turns and, if so, moves to the next round.
     */
    public void startGame() {
        GwentStoneLite.setGamesPlayed(GwentStoneLite.getGamesPlayed() + 1);
        nextRound();

        for (ActionsInput action : actions) {

            actionPerformer.performAction(action);
            if (playerOneEndTurn && playerTwoEndTurn) {
                nextRound();
            }
        }
    }

    /**
     * Advances the game to the next round by updating mana and drawing cards for each player.
     * In this method, the following actions occur:
     *     - Increments the mana available to each player for the round,
     *     up to the maximum mana limit.
     *     - Each player receives a card from their deck (if available)
     *     to add to their hand.
     *     - Resets the end-of-turn flags for both players.
     * This method ensures that both players are ready for the next round of actions.
     */
    public void nextRound() {
        if (manaPerRound < MAXMANA) {
            manaPerRound++;
        }

        playerOne.setMana(playerOne.getMana() + manaPerRound);
        playerTwo.setMana(playerTwo.getMana() + manaPerRound);

        if (!playerOne.getCardsToPlay().isEmpty()) {
            playerOne.getHandCards().add(playerOne.getCardsToPlay().remove(0));
        }

        if (!playerTwo.getCardsToPlay().isEmpty()) {
            playerTwo.getHandCards().add(playerTwo.getCardsToPlay().remove(0));
        }

        playerOneEndTurn = false;
        playerTwoEndTurn = false;
    }
}
