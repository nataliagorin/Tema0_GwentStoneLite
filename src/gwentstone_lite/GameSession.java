package gwentstone_lite;

import fileio.ActionsInput;
import fileio.GameInput;
import fileio.StartGameInput;
import gwentstone_lite.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameSession {
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

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<Card>> board) {

    }

    public StartGameInput getSetupGame() {
        return setupGame;
    }

    public void setSetupGame(StartGameInput setupGame) {
        this.setupGame = setupGame;
    }

    public ArrayList<ActionsInput> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ActionsInput> actions) {
        this.actions = actions;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getManaPerRound() {
        return manaPerRound;
    }

    public void setManaPerRound(int manaPerRound) {
        this.manaPerRound = manaPerRound;
    }

    public boolean isPlayerOneEndTurn() {
        return playerOneEndTurn;
    }

    public void setPlayerOneEndTurn(boolean playerOneEndTurn) {
        this.playerOneEndTurn = playerOneEndTurn;
    }

    public boolean isPlayerTwoEndTurn() {
        return playerTwoEndTurn;
    }

    public void setPlayerTwoEndTurn(boolean playerTwoEndTurn) {
        this.playerTwoEndTurn = playerTwoEndTurn;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public GameCommand getActionPerformer() {
        return actionPerformer;
    }

    public void setActionPerformer(GameCommand actionPerformer) {
        this.actionPerformer = actionPerformer;
    }

    public void getPlayerTurn(ActionsInput action) {
    }

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
