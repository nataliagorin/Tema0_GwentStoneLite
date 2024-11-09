package org.poo.gwentstonelite;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;

public final class GwentStoneLite {
    public static final int ROW0 = 0;
    public static final int ROW1 = 1;
    public static final int ROW2 = 2;
    public static final int ROW3 = 3;
    public static final int ROWFULL = 5;
    private Input input;
    private ArrayNode output;
    private Player playerOne;
    private Player playerTwo;
    private org.poo.gwentstonelite.GameSession gameSession;
    private static int gamesPlayed;
    private static Action cardActions;
    private static OutputBuilder outputCreator;

    public GwentStoneLite(final Input input, final ArrayNode output) {
        this.input = input;
        this.output = output;
        this.playerOne = new Player();
        this.playerTwo = new Player();
        gamesPlayed = 0;
        cardActions = new Action();
        outputCreator = new OutputBuilder(output);
    }

    /**
     * Initializes and starts a new game session of GwentStoneLite.
     * This method prepares the game by setting up the decks for both players,
     * initializing their game state, and starting the game sessions
     * as defined in the input configuration.
     * The steps performed by this method include:
     *     - Sets the decks for player one and player two using the provided deck configuration.
     *     - Resets the games won counter for both players.
     *     - Iterates through the list of game sessions, creating and starting
     *          each game session by preparing it
     *          and then beginning the game.
     */
    public void startGwentStoneLite() {
        playerOne.setDecks(cardActions.changeDecks(input.getPlayerOneDecks().getDecks()));
        playerTwo.setDecks(cardActions.changeDecks(input.getPlayerTwoDecks().getDecks()));
        playerOne.setGamesWon(0);
        playerTwo.setGamesWon(0);

        for (GameInput game : input.getGames()) {
            gameSession = new org.poo.gwentstonelite.GameSession(playerOne, playerTwo, game);
            gameSession.prepareGame(cardActions);
            gameSession.startGame();
        }
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    public static int getGamesPlayed() {
        return gamesPlayed;
    }

    public static void setGamesPlayed(final int gamesPlayed) {
        GwentStoneLite.gamesPlayed = gamesPlayed;
    }

    public static Action getCardActions() {
        return cardActions;
    }

    public static OutputBuilder getOutputCreator() {
        return outputCreator;
    }
}
