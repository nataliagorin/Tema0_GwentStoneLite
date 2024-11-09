package org.poo.gwentstonelite;

import org.poo.fileio.ActionsInput;
import org.poo.fileio.Coordinates;
import org.poo.gwentstonelite.cards.Card;

import java.util.ArrayList;

public final class GameCommand {
    private org.poo.gwentstonelite.GameSession game;
    public GameCommand(final org.poo.gwentstonelite.GameSession game) {
        this.game = game;
    }

    /**
     * Performs an action based on the specified command in the ActionsInput.
     * This method handles various commands, including debugging,
     * statistics retrieval, card actions and ending the player's turn.
     * The appropriate method is called based on the command
     * provided in the action.
     *
     * @param action the ActionsInput object containing the details of the action to be performed.
     *               The command within the action determines the type of action,
     *               such as getting player stats performing a card action,
     *               or ending the player's turn.
     *
     * @throws IllegalArgumentException if the command
     * in the action is not recognized or is invalid.
     */
    public void performAction(final ActionsInput action) {
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

            default -> throw new IllegalArgumentException("Invalid command.");
        }
    }

    /**
     * Uses the hero's ability for the current player, if possible.
     * This method checks if the player has enough mana
     * and if the hero has already attacked during the turn.
     * If the conditions are met, the hero's ability is triggered.
     * Otherwise, an appropriate error message is generated.
     * The method uses the current player's hero card and
     * performs the ability, taking into account the following conditions:
     *      the player must have enough mana to use the hero's ability.
     *      the hero has not already attacked during the turn.
     * If any of these conditions are not met,
     * an error message is generated and outputted.
     *
     * @param action the ActionsInput object containing
     *              the details of the action to be performed,
     *              including the command to use the hero's ability.
     */
    public void useHeroAbility(final ActionsInput action) {
        org.poo.gwentstonelite.cards.Card hero;
        org.poo.gwentstonelite.Player player;
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
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().useHeroAbilityError(errorMessage, action);
        }
    }

    /**
     * Executes an attack from a card on the opponent's hero, if possible.
     * This method performs the necessary checks to
     * ensure the attack can be carried out, such as:
     *      ensuring the attacking card is not frozen.
     *      checking that the attacking card has not already attacked during the current turn.
     *      verifying that the attack is targeting a valid card type
     *          ('Tank') based on the position of the attacking card.
     * If any condition fails, an appropriate error message is
     * generated. If the attack is valid, the attack is executed.
     *
     * @param action the ActionsInput object containing the details
     *              of the action, including the coordinates
     *              of the attacker and the target.
     */
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
                if (action.getCardAttacker().getX() == org.poo.gwentstonelite.GwentStoneLite.ROW0
                        || action.getCardAttacker().getX() == org.poo.gwentstonelite.GwentStoneLite.ROW1) {
                    if (checkTanks(org.poo.gwentstonelite.GwentStoneLite.ROW2)) {
                        errorMessage = "Attacked card is not of type 'Tank'.";
                    } else {
                        attacker.useAttack(game, action);
                    }
                } else {
                    if (checkTanks(org.poo.gwentstonelite.GwentStoneLite.ROW1)) {
                        errorMessage = "Attacked card is not of type 'Tank'.";
                    } else {
                        attacker.useAttack(game, action);
                    }
                }
            }
        }

        if (!errorMessage.equals("null")) {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    /**
     * Executes the ability of a card on the board, if possible.
     * This method performs the necessary checks to ensure the card can use its ability:
     * - Ensuring the card is not frozen.
     * - Verifying that the card has not already attacked during the current turn.
     * If the card is eligible to use its ability, the ability is executed.
     * If any condition fails, an appropriate error message is generated.
     * @param action the ActionsInput object containing the
     *               details of the action, including the
     *               coordinates of the card using the ability.
     */
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
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    /**
     * Executes an attack from one card to another on the board, if the conditions are met.
     * This method performs several checks to ensure the attack can be executed:
     * - The attacking card must belong to the current player's side (ROW0/ROW1).
     * - The attacked card must belong to the enemy player's side (ROW2/ROW3).
     * - The attacking card must not have already attacked during the current turn.
     * - The attacking card must not be frozen.
     * - If the target is a 'Tank', the attack is allowed only if
     *      the attacked card is also a 'Tank' (depending on the row).
     * If any of these conditions fail, an appropriate error message is generated.
     * If the attack is valid, the attack is executed and the target's health is reduced.
     * @param action the ActionsInput object containing the coordinates of
     *               the attacking card and the attacked card,
     *               along with other action details.
     */
    public void cardUsesAttack(final ActionsInput action) {
        Coordinates cardAttackerCoord = action.getCardAttacker();
        Coordinates cardAttackedCoord = action.getCardAttacked();

        Card cardAttacker = game.getBoard().
                get(cardAttackerCoord.getX()).get(cardAttackerCoord.getY());
        Card cardAttacked = game.getBoard().
                get(cardAttackedCoord.getX()).get(cardAttackedCoord.getY());

        String errorMessage = "null";

        if (cardAttackerCoord.getX() == org.poo.gwentstonelite.GwentStoneLite.ROW0
                || cardAttackerCoord.getX() == org.poo.gwentstonelite.GwentStoneLite.ROW1) {
            if (cardAttackedCoord.getX() != org.poo.gwentstonelite.GwentStoneLite.ROW2
                    && cardAttackedCoord.getX() != org.poo.gwentstonelite.GwentStoneLite.ROW3) {
                errorMessage = "Attacked card does not belong to the enemy.";
            } else {
                if (cardAttacker.hasAttacked()) {
                    errorMessage = "Attacker card has already attacked this turn.";
                } else {
                    if (cardAttacker.isFrozen()) {
                        errorMessage = "Attacker card is frozen.";
                    } else {
                        if (checkTanks(org.poo.gwentstonelite.GwentStoneLite.ROW2)) {
                            if (!org.poo.gwentstonelite.GwentStoneLite.getCardActions().isTank(cardAttacked)) {
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
            if (cardAttackedCoord.getX() != org.poo.gwentstonelite.GwentStoneLite.ROW0
                    && cardAttackedCoord.getX() != org.poo.gwentstonelite.GwentStoneLite.ROW1) {
                errorMessage = "Attacked card does not belong to the enemy.";
            } else {
                if (cardAttacker.hasAttacked()) {
                    errorMessage = "Attacker card has already attacked this turn.";
                } else {
                    if (cardAttacker.isFrozen()) {
                        errorMessage = "Attacker card is frozen.";
                    } else {
                        if (checkTanks(org.poo.gwentstonelite.GwentStoneLite.ROW1)) {
                            if (!org.poo.gwentstonelite.GwentStoneLite.getCardActions().isTank(cardAttacked)) {
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
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    /**
     * Checks if there are any 'Tank' cards in the specified row of the game board.
     * A 'Tank' card is a card that has a special defensive role,
     * typically with high health or specific abilities.
     * This method iterates through all cards in the
     * given row and checks if any of them is of type 'Tank'.
     * @param row the row index on the game board to check for 'Tank' cards.
     *            The row corresponds to the player's and opponent's rows on the board.
     * @return true if there is at least one 'Tank' card in the
     * specified row and code false otherwise.
     */
    public boolean checkTanks(final int row) {
        ArrayList<ArrayList<Card>> board = game.getBoard();

        for (Card card : board.get(row)) {
            if (org.poo.gwentstonelite.GwentStoneLite.getCardActions().isTank(card)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Places a card from the player's hand onto the game board,
     * provided the player has enough mana and the target row is not full.
     * This method checks if the player has sufficient mana to place the card
     * and if the row where the card is to be placed has space.
     * If any condition is not met, an error message is generated and no action is taken.
     * @param action The ActionsInput containing the details of the card placement action,
     *              including the index of the card in the player's hand.
     */
    public void placeCard(final ActionsInput action) {
        org.poo.gwentstonelite.Player player;
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

            if (game.getBoard().get(row).size() == org.poo.gwentstonelite.GwentStoneLite.ROWFULL) {
                errorMessage = "Cannot place card on table since row is full.";
            } else {
                game.getBoard().get(row).add(player.getHandCards().remove(action.getHandIdx()));
                player.setMana(player.getMana() - card.getMana());
            }
        }

        if (!errorMessage.equals("null")) {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().createPlaceCardError(errorMessage, action);
        }
    }

    /**
     * Determines the appropriate row on the game board for a given card
     * based on the current player's turn and the card's position.
     * The method checks whether the card is positioned in the
     * "front" or "back" row, and then places it in the corresponding row on the board.
     * If the current player is Player 1, the card will be placed in either row 2
     * or row 3 depending on whether it is a "front" or "back" card.
     * If the current player is Player 2, the card will be placed
     * in either row 0 or row 1 based on the same condition.
     *
     * @param card The Card whose row placement needs to be determined.
     * @return The row index where the card should be placed
     * on the board.
     */

    public int getRow(final Card card) {
        if (game.getPlayerTurn() == 1) {
            if (org.poo.gwentstonelite.GwentStoneLite.getCardActions().checkRow(card).equals("front")) {
                return org.poo.gwentstonelite.GwentStoneLite.ROW2;
            } else {
                return org.poo.gwentstonelite.GwentStoneLite.ROW3;
            }
        } else {
            if (org.poo.gwentstonelite.GwentStoneLite.getCardActions().checkRow(card).equals("front")) {
                return org.poo.gwentstonelite.GwentStoneLite.ROW1;
            } else {
                return org.poo.gwentstonelite.GwentStoneLite.ROW0;
            }
        }
    }

    /**
     * Retrieves the number of wins for the specified player
     * (Player 1 or Player 2) and outputs the result.
     *
     * This method checks the command provided in
     * the ActionsInput and determines whether to get
     * the wins for Player 1 or Player 2.
     * It then calls the output creator to display the
     * number of wins for the selected player.
     *
     * @param action The ActionsInput object that contains
     *               the command to get the player's win count. The command should be either
     *               "getPlayerOneWins" or "getPlayerTwoWins".
     */
    public void getPlayerWins(final ActionsInput action) {
        org.poo.gwentstonelite.Player player;

        if (action.getCommand().equals("getPlayerOneWins")) {
            player = game.getPlayerOne();
        } else {
            player = game.getPlayerTwo();
        }

        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().playerWinsOutput(player, action);
    }

    /**
     * Outputs the total number of games played in the current session.
     * @param action The ActionsInput object containing the command for retrieving
     *               the total games played statistic.
     */
    public void getTotalGamesPlayed(final ActionsInput action) {
        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().totalGamesOutput(action);
    }

    /**
     * Outputs the list of frozen cards currently on the game board.
     * @param action The ActionsInput object that specifies the command
     *               to retrieve the list of frozen cards on the board.
     */
    public void getFrozenCardsOnTable(final ActionsInput action) {
        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().frozenCardsOutput(game.getBoard(), action);
    }

    /**
     * Outputs the current mana level of the specified player.
     * This method retrieves the mana level for the player specified in the ActionsInput
     * and sends this information to the output creator for display. The player is determined
     * by the index provided in the action input.
     *
     * @param action The ActionsInput object containing the player index
     *               to identify which player's mana to retrieve and display.
     */
    public void getPlayerMana(final ActionsInput action) {
        org.poo.gwentstonelite.Player player;

        if (action.getPlayerIdx() == 1) {
            player = game.getPlayerOne();
        } else {
            player = game.getPlayerTwo();
        }

        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().playerManaOutput(player, action);
    }

    /**
     * Retrieves and outputs the card located at a specified position on the game board.
     *
     * This method checks if there is a card at the
     * position indicated by the coordinates
     * in the ActionsInput. If a card is found, it
     * is retrieved and sent to the output
     * creator along with the action details.
     * If no card is present at the specified position,
     * an error message is sent instead.
     *
     * @param action The ActionsInput object containing the coordinates (x, y)
     *               of the board position to check for a card.
     */
    public void getCardAtPosition(final ActionsInput action) {
        Card card = null;
        String errorMessage = "null";

        if (game.getBoard().get(action.getX()).size() <= action.getY()) {
            errorMessage = "No card available at that position.";
        } else {
            card = game.getBoard().get(action.getX()).get(action.getY());
        }

        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().cardAtPositionOutput(card, action, errorMessage);
    }

    /**
     * Retrieves and outputs the hero card of the specified player.
     * This method identifies the player based on the player index provided in the
     * ActionsInput object. It retrieves the hero card for the selected player
     * and sends it to the output creator along with the action details.
     *
     * @param action The ActionsInput object containing the player index (1 or 2)
     *               to specify which player's hero card to retrieve.
     */
    public void getPlayerHero(final ActionsInput action) {
        if (action.getPlayerIdx() == 1) {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().
                    playerHeroOutput(game.getPlayerOne().getHeroCard(), action);
        } else {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().
                    playerHeroOutput(game.getPlayerTwo().getHeroCard(), action);
        }
    }

    /**
     * Retrieves and outputs all cards currently on the game board.
     * This method accesses the game board and sends its current state, which includes
     * all cards on the table, to the output creator for display along with the action details.
     *
     * @param action The ActionsInput object containing action-specific information
     *               to assist with processing the output.
     */
    public void getCardsOnTable(final ActionsInput action) {
        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().boardOutput(game.getBoard(), action);
    }


    /**
     * Retrieves and outputs the deck of a specified player.
     * This method fetches the deck of the player indicated by the player index
     * in the provided action and sends the deck information to the output creator
     * for display.
     *
     * @param action The ActionsInput object containing the index of the player
     *               whose deck is to be retrieved and action-specific details
     *               for processing.
     */
    public void getPlayerDeck(final ActionsInput action) {
        if (action.getPlayerIdx() == 1) {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().
                    cardsOutput(game.getPlayerOne().getCardsToPlay(), action);
        } else {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().
                    cardsOutput(game.getPlayerTwo().getCardsToPlay(), action);
        }
    }

    /**
     * Retrieves and outputs the current player's turn.
     * @param action The ActionsInput object containing details needed
     *               for output processing.
     */
    public void getPlayerTurn(final ActionsInput action) {
        org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().playerTurnOutput(game.getPlayerTurn(), action);
    }

    /**
     * Ends the current player's turn and prepares the game state for the next player.
     * This method performs several actions based on which player's turn it is:
     *   - Marks the current player's turn as ended.
     *   - Resets the status of all cards on the player's board rows, including
     *       removing any frozen or attacked status from cards.
     *   - Resets the player's hero card status.
     *   - Switches the turn to the other player.
     * @param action The ActionsInput object containing data associated with
     *               the action, though it is not directly used in this method.
     */
    public void endPlayerTurn(final ActionsInput action) {
        if (game.getPlayerTurn() == 1) {
            game.setPlayerOneEndTurn(true);
            org.poo.gwentstonelite.GwentStoneLite.getCardActions().
                    resetCardsFromBoard(game.getBoard(), org.poo.gwentstonelite.GwentStoneLite.ROW2,
                            org.poo.gwentstonelite.GwentStoneLite.ROW3);
            org.poo.gwentstonelite.GwentStoneLite.getCardActions().resetHero(game.getPlayerOne());
            game.setPlayerTurn(2);
        } else {
            game.setPlayerTwoEndTurn(true);
            org.poo.gwentstonelite.GwentStoneLite.getCardActions().
                    resetCardsFromBoard(game.getBoard(), org.poo.gwentstonelite.GwentStoneLite.ROW0,
                            org.poo.gwentstonelite.GwentStoneLite.ROW1);
            org.poo.gwentstonelite.GwentStoneLite.getCardActions().resetHero(game.getPlayerTwo());
            game.setPlayerTurn(1);
        }
    }


    /**
     * Retrieves the cards currently in the specified player's hand and outputs the information.
     * This method checks the player index provided in the action parameter
     * to determine which player's hand to retrieve. It then uses the output creator to
     * display the list of cards in that player's hand.
     *
     * @param action The ActionsInput object containing details about the action,
     *               including the index of the player whose hand is to be retrieved.
     */
    public void getCardsInHand(final ActionsInput action) {
        if (action.getPlayerIdx() == 1) {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().
                    cardsOutput(game.getPlayerOne().getHandCards(), action);
        } else {
            org.poo.gwentstonelite.GwentStoneLite.getOutputCreator().
                    cardsOutput(game.getPlayerTwo().getHandCards(), action);
        }
    }
}
