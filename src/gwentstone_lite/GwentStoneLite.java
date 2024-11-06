package gwentstone_lite;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;

public class GwentStoneLite {
    public static final int ROW0 = 0;
    public static final int ROW1 = 1;
    public static final int ROW2 = 2;
    public static final int ROW3 = 3;
    public static final int ROWFULL = 5;
    private Input input;
    private ArrayNode output;
    private Player playerOne;
    private Player playerTwo;
    private GameSession gameSession;
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

    public void startGwentStoneLite() {
        playerOne.setDecks(cardActions.changeDecks(input.getPlayerOneDecks().getDecks()));
        playerTwo.setDecks(cardActions.changeDecks(input.getPlayerTwoDecks().getDecks()));
        playerOne.setGamesWon(0);
        playerTwo.setGamesWon(0);

        for (GameInput game : input.getGames()) {
            gameSession = new GameSession(playerOne, playerTwo, game);
            gameSession.prepareGame(cardActions);
            gameSession.startGame();
        }
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public static int getGamesPlayed() {
        return gamesPlayed;
    }

    public static void setGamesPlayed(int gamesPlayed) {
        GwentStoneLite.gamesPlayed = gamesPlayed;
    }

    public static Action getCardActions() {
        return cardActions;
    }

    public static void setCardActions(Action cardActions) {
        GwentStoneLite.cardActions = cardActions;
    }

    public static OutputBuilder getOutputCreator() {
        return outputCreator;
    }

    public static void setOutputCreator(OutputBuilder outputCreator) {
        GwentStoneLite.outputCreator = outputCreator;
    }
}
