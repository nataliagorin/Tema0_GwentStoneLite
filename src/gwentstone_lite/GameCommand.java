package gwentstone_lite;

import fileio.ActionsInput;
import fileio.Coordinates;
import gwentstone_lite.cards.Card;

import java.util.ArrayList;

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

            // End Turn command
            case "endPlayerTurn" -> endPlayerTurn(action);
        }
    }

    public void useHeroAbility(final ActionsInput action) {
        Card hero = null;
        Player player = null;
        String errorMessage = "null";

        if (game.getPlayerTurn() == 1) {
            hero = game.getPlayerOne().getHeroCard();
            player = game.getPlayerOne();
        } else {
            hero = game.getPlayerTwo().getHeroCard();
            player = game.getPlayerTwo();
        }

        if (player.getMana() < hero.getMana()) {
            errorMessage = "Not enough mana to use hero's ability.";
        } else {
            if (hero.hasAttacked()) {
                errorMessage = "Hero has already attacked this turn.";
            } else {
                if (game.getPlayerTurn() == 1) {
                    game.getPlayerOne().getHeroCard().useAbility(game, action);
                } else {
                    game.getPlayerTwo().getHeroCard().useAbility(game, action);
                }
            }
        }

        if (!errorMessage.equals("null")) {
            GwentStoneLite.getOutputCreator().useHeroAbilityError(errorMessage, action);
        }
    }

    public void useAttackHero(final ActionsInput action) {
        Card attacker = game.getBoard().get(action.getCardAttacker().getX()).
                get(action.getCardAttacker().getY());
        String errorMessage = "null";

        if (attacker.isFrozen()) {
            errorMessage = "Attacker card is frozen.";
        } else {
            if (attacker.hasAttacked()) {
                errorMessage = "Attacker card has already attacked this turn.";
            } else {
                if (action.getCardAttacker().getX() == GwentStoneLite.ROW0
                        || action.getCardAttacker().getX() == GwentStoneLite.ROW1) {
                    if (checkTanks(GwentStoneLite.ROW2)) {
                        errorMessage = "Attacked card is not of type 'Tank'.";
                    } else {
                        attacker.useAttack(game, action);
                    }
                } else {
                    if (checkTanks(GwentStoneLite.ROW1)) {
                        errorMessage = "Attacked card is not of type 'Tank'.";
                    } else {
                        attacker.useAttack(game, action);
                    }
                }
            }
        }

        if (!errorMessage.equals("null")) {
            GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    public void cardUsesAbility(final ActionsInput action) {
        Card attacker = game.getBoard().get(action.getCardAttacker().getX()).
                get(action.getCardAttacker().getY());
        String errorMessage = "null";

        if (attacker.isFrozen()) {
            errorMessage = "Attacker card is frozen.";
        } else {
            if (attacker.hasAttacked()) {
                errorMessage = "Attacker card has already attacked this turn.";
            } else {
                game.getBoard().get(action.getCardAttacker().getX()).
                        get(action.getCardAttacker().getY()).useAbility(game, action);
            }
        }

        if (!errorMessage.equals("null")) {
            GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    public void cardUsesAttack(final ActionsInput action) {
        Coordinates cardAttackerCoord = action.getCardAttacker();
        Coordinates cardAttackedCoord = action.getCardAttacked();

        Card cardAttacker = game.getBoard().
                get(cardAttackerCoord.getX()).get(cardAttackerCoord.getY());
        Card cardAttacked = game.getBoard().
                get(cardAttackedCoord.getX()).get(cardAttackedCoord.getY());

        String errorMessage = "null";

        if (cardAttackerCoord.getX() == GwentStoneLite.ROW0
                || cardAttackerCoord.getX() == GwentStoneLite.ROW1) {
            if (cardAttackedCoord.getX() != GwentStoneLite.ROW2
                    && cardAttackedCoord.getX() != GwentStoneLite.ROW3) {
                errorMessage = "Attacked card does not belong to the enemy.";
            } else {
                if (cardAttacker.hasAttacked()) {
                    errorMessage = "Attacker card has already attacked this turn.";
                } else {
                    if (cardAttacker.isFrozen()) {
                        errorMessage = "Attacker card is frozen.";
                    } else {
                        if (checkTanks(GwentStoneLite.ROW2)) {
                            if (!GwentStoneLite.getCardActions().isTank(cardAttacked)) {
                                errorMessage = "Attacked card is not of type 'Tank'.";
                            } else {
                                cardAttacker.useAttack(game, action);
                            }
                        } else {
                            cardAttacker.useAttack(game, action);
                        }
                    }
                }
            }
        } else {
            if (cardAttackedCoord.getX() != GwentStoneLite.ROW0
                    && cardAttackedCoord.getX() != GwentStoneLite.ROW1) {
                errorMessage = "Attacked card does not belong to the enemy.";
            } else {
                if (cardAttacker.hasAttacked()) {
                    errorMessage = "Attacker card has already attacked this turn.";
                } else {
                    if (cardAttacker.isFrozen()) {
                        errorMessage = "Attacker card is frozen.";
                    } else {
                        if (checkTanks(GwentStoneLite.ROW1)) {
                            if (!GwentStoneLite.getCardActions().isTank(cardAttacked)) {
                                errorMessage = "Attacked card is not of type 'Tank'.";
                            } else {
                                cardAttacker.useAttack(game, action);
                            }
                        } else {
                            cardAttacker.useAttack(game, action);
                        }
                    }
                }
            }
        }

        if (!errorMessage.equals("null")) {
            GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    public boolean checkTanks(final int row) {
        ArrayList<ArrayList<Card>> board = game.getBoard();

        for (Card card : board.get(row)) {
            if (GwentStoneLite.getCardActions().isTank(card)) {
                return true;
            }
        }

        return false;
    }

    public void placeCard(final ActionsInput action) {
        Player player;
        Card card;
        String errorMessage = "null";
        int row;

        if (game.getPlayerTurn() == 1) {
            player = game.getPlayerOne();
        } else {
            player = game.getPlayerTwo();
        }

        card = player.getHandCards().get(action.getHandIdx());


        if (player.getMana() < card.getMana()) {
            errorMessage = "Not enough mana to place card on table.";
        } else {
            row = getRow(card);

            if (game.getBoard().get(row).size() == GwentStoneLite.ROWFULL) {
                errorMessage = "Cannot place card on table since row is full.";
            } else {
                game.getBoard().get(row).add(player.getHandCards().remove(action.getHandIdx()));
                player.setMana(player.getMana() - card.getMana());
            }
        }

        if (!errorMessage.equals("null")) {
            GwentStoneLite.getOutputCreator().createPlaceCardError(errorMessage, action);
        }
    }

    public int getRow(final Card card) {
        if (game.getPlayerTurn() == 1) {
            if (GwentStoneLite.getCardActions().checkRow(card).equals("front")) {
                return GwentStoneLite.ROW2;
            } else {
                return GwentStoneLite.ROW3;
            }
        } else {
            if (GwentStoneLite.getCardActions().checkRow(card).equals("front")) {
                return GwentStoneLite.ROW1;
            } else {
                return GwentStoneLite.ROW0;
            }
        }
    }

    public void getPlayerWins(final ActionsInput action) {
        Player player;

        if (action.getCommand().equals("getPlayerOneWins")) {
            player = game.getPlayerOne();
        } else {
            player = game.getPlayerTwo();
        }

        GwentStoneLite.getOutputCreator().playerWinsOutput(player, action);
    }

    public void getTotalGamesPlayed(final ActionsInput action) {
        GwentStoneLite.getOutputCreator().totalGamesOutput(action);
    }

    public void getFrozenCardsOnTable(final ActionsInput action) {
        GwentStoneLite.getOutputCreator().frozenCardsOutput(game.getBoard(), action);
    }

    public void getPlayerMana(final ActionsInput action) {
        Player player;

        if (action.getPlayerIdx() == 1) {
            player = game.getPlayerOne();
        } else {
            player = game.getPlayerTwo();
        }

        GwentStoneLite.getOutputCreator().playerManaOutput(player, action);
    }

    public void getCardAtPosition(final ActionsInput action) {
        Card card = null;
        String errorMessage = "null";

        if (game.getBoard().get(action.getX()).size() <= action.getY()) {
            errorMessage = "No card available at that position.";
        } else {
            card = game.getBoard().get(action.getX()).get(action.getY());
        }

        GwentStoneLite.getOutputCreator().cardAtPositionOutput(card, action, errorMessage);
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

    public void getCardsOnTable(final ActionsInput action) {
        GwentStoneLite.getOutputCreator().boardOutput(game.getBoard(), action);
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
