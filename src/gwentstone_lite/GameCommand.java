package gwentstone_lite;

import fileio.ActionsInput;

public class GameCommand {
    private GameSession game;
    public GameCommand(final GameSession game) {
        this.game = game;
    }

    public void performAction(final ActionsInput action) {
        String error = "null";
        switch (action.getCommand()) {
            // Debugging commands
            case "getCardsInHand" -> getCardsInHand(action);
            case "getPlayerDeck" -> getPlayerDeck(action);
            case "getCardsOnTable" -> getCardsOnTable(action);
            case "getPlayerTurn" -> getPlayerTurn(action);
            case "getPlayerHero" -> getPlayerHero(action);
            case "getCardAtPosition" -> getCardAtPosition(action);
            case "getPlayerMana" -> getPlayerMana(action);
            case "getFrozenCardsOnTable" -> getFrozenCardsOnTable(action);

            // Statistics commands
            case "getTotalGamesPlayed" -> getTotalGamesPlayed(action);
            case "getPlayerOneWins", "getPlayerTwoWins" -> getPlayerWins(action);

            // Cards commands
            case "placeCard" -> placeCard(action);
            case "cardUsesAttack" -> cardUsesAttack(action);
            case "cardUsesAbility" -> cardUsesAbility(action);
            case "useAttackHero" -> useAttackHero(action);
            case "useHeroAbility" -> useHeroAbility(action);
            case "useEnvironmentCard" -> useEnvironmentCard(action);

            // End Turn command
            case "endPlayerTurn" -> endPlayerTurn(action);
        }
    }

    private void useEnvironmentCard(ActionsInput action) {
    }

    private void useHeroAbility(ActionsInput action) {
    }

    private void useAttackHero(ActionsInput action) {
    }

    private void cardUsesAbility(ActionsInput action) {
    }

    private void cardUsesAttack(ActionsInput action) {
    }

    private void placeCard(ActionsInput action) {
    }

    private void getPlayerWins(ActionsInput action) {
    }

    private void getTotalGamesPlayed(ActionsInput action) {
    }

    private void getFrozenCardsOnTable(ActionsInput action) {
    }

    private void getPlayerMana(ActionsInput action) {
    }

    private void getCardAtPosition(ActionsInput action) {
    }

    public void getPlayerHero(final ActionsInput action) {
        if (action.getPlayerIdx() == 1) {
            GwentStoneLite.getOutputCreator().
                    playerHeroOutput(game.getPlayerOne().getHeroCard(), action);
        } else {
            GwentStoneLite.getOutputCreator().
                    playerHeroOutput(game.getPlayerTwo().getHeroCard(), action);
        }
    }

    private void getCardsOnTable(ActionsInput action) {
    }

    public void getPlayerDeck(final ActionsInput action) {
        if (action.getPlayerIdx() == 1) {
            GwentStoneLite.getOutputCreator().cardsOutput(game.getPlayerOne().getCardsToPlay(), action);
        } else {
            GwentStoneLite.getOutputCreator().cardsOutput(game.getPlayerTwo().getCardsToPlay(), action);
        }
    }

    public void getPlayerTurn(final ActionsInput action) {
        GwentStoneLite.getOutputCreator().playerTurnOutput(game.getPlayerTurn(), action);
    }

    public void endPlayerTurn(final ActionsInput action) {
        if (game.getPlayerTurn() == 1) {
            game.setPlayerOneEndTurn(true);
            GwentStoneLite.getCardActions().
                    resetCardsFromBoard(game.getBoard(), GwentStoneLite.ROW2, GwentStoneLite.ROW3);
            GwentStoneLite.getCardActions().resetHero(game.getPlayerOne());
            game.setPlayerTurn(2);
        } else {
            game.setPlayerTwoEndTurn(true);
            GwentStoneLite.getCardActions().
                    resetCardsFromBoard(game.getBoard(), GwentStoneLite.ROW0, GwentStoneLite.ROW1);
            GwentStoneLite.getCardActions().resetHero(game.getPlayerTwo());
            game.setPlayerTurn(1);
        }
    }



    public void getCardsInHand(final ActionsInput action) {
        if (action.getPlayerIdx() == 1) {
            GwentStoneLite.getOutputCreator().cardsOutput(game.getPlayerOne().getHandCards(), action);
        } else {
            GwentStoneLite.getOutputCreator().cardsOutput(game.getPlayerTwo().getHandCards(), action);
        }
    }
}
