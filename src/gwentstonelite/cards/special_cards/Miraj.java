package gwentstonelite.cards.special_cards;

import fileio.ActionsInput;
import gwentstonelite.GameSession;
import gwentstonelite.GwentStoneLite;
import gwentstonelite.cards.Card;

public final class Miraj extends Card {
    public Miraj(final Card card) {
        super(card);
    }

    @Override
    public void useAbility(final GameSession game, final ActionsInput action) {
        String errorMessage = "null";
        Card cardAttacked = game.getBoard().get(action.getCardAttacked().getX()).
                get(action.getCardAttacked().getY());

        if (action.getCardAttacker().getX() == 1) {
            if (action.getCardAttacked().getX() == GwentStoneLite.ROW2
                    || action.getCardAttacked().getX() == GwentStoneLite.ROW3) {
                if (checkTanksOnRow(game, GwentStoneLite.ROW2)) {
                    if (GwentStoneLite.getCardActions().isTank(cardAttacked)) {
                        ability(game, action);
                    } else {
                        errorMessage = "Attacked card is not of type 'Tank'.";
                    }
                } else {
                    ability(game, action);
                }
            } else {
                errorMessage = "Attacked card does not belong to the enemy.";
            }
        } else {
            if (action.getCardAttacked().getX() == GwentStoneLite.ROW0
                    || action.getCardAttacked().getX() == GwentStoneLite.ROW1) {
                if (checkTanksOnRow(game, GwentStoneLite.ROW1)) {
                    if (GwentStoneLite.getCardActions().isTank(cardAttacked)) {
                        ability(game, action);
                    } else {
                        errorMessage = "Attacked card is not of type 'Tank'.";
                    }
                } else {
                    ability(game, action);
                }
            } else {
                errorMessage = "Attacked card does not belong to the enemy.";
            }
        }

        if (!errorMessage.equals("null")) {
            GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
        }
    }

    /**
     * Checks if there are any tank cards present on the specified row of the game board.
     * A tank card is determined using the `isTank` method, which checks
     * specific card properties.
     *
     * @param game The current game session, providing access to the game
     *             board and player information.
     * @param row The row index of the game board to check for tank cards.
     * @return true if there is at least one tank card on the specified row;
     *          false otherwise.
     */
    public boolean checkTanksOnRow(final GameSession game, final int row) {
        for (Card card : game.getBoard().get(row)) {
            if (GwentStoneLite.getCardActions().isTank(card)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Executes the ability of Miraj, which swaps the health between
     * the attacking card and the card being attacked.
     * The health of the attacker is set to the health of the attacked card,
     * and the health of the attacked card is set to the health of the attacking card.
     *
     * @param game The current game session, which provides access to the game board
     *             and the cards in play.
     * @param action The action details, which include the position of the card being
     *               attacked and the card attacking.
     */
    public void ability(final GameSession game, final ActionsInput action) {
        Card cardAttacked = game.getBoard().get(action.getCardAttacked().getX()).
                get(action.getCardAttacked().getY());
        int attackerHeath = getHealth();
        int attackedHealth = cardAttacked.getHealth();

        cardAttacked.setHealth(attackerHeath);
        setHealth(attackedHealth);

        setAttacked(true);
    }
}
