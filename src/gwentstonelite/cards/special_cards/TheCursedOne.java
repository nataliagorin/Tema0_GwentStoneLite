package gwentstonelite.cards.special_cards;

import fileio.ActionsInput;
import gwentstonelite.GameSession;
import gwentstonelite.GwentStoneLite;
import gwentstonelite.cards.Card;

public final class TheCursedOne extends Card {
    public TheCursedOne(final Card card) {
        super(card);
    }

    @Override
    public void useAbility(final GameSession game, final ActionsInput action) {
        String errorMessage = "null";
        Card cardAttacked = game.getBoard().get(action.getCardAttacked().getX()).
                get(action.getCardAttacked().getY());

        if (action.getCardAttacker().getX() == 0) {
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
     * Checks if there are any "Tank" cards present on a specific row of the game board.
     *
     * @param game The current game session, providing access to
     *             the game board and the cards in play.
     * @param row The index of the row on the game board to check for "Tank" cards.
     * @return true if there is at least one "Tank" card on
     * the specified row, false otherwise.
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
     * Executes the ability of The Cursed One card. The Cursed One
     * swaps the attack damage and health of the attacked card.
     * If the attacked card's health becomes zero after the swap,
     * the attacked card is removed from the board.
     *
     * @param game The current game session, providing access to the game board and game state.
     * @param action The action input containing details about the card that
     *               was attacked and the coordinates of the attacked card.
     */
    public void ability(final GameSession game, final ActionsInput action) {
        Card cardAttacked = game.getBoard().get(action.getCardAttacked().getX()).
                get(action.getCardAttacked().getY());
        int attackedHealth = cardAttacked.getHealth();
        int attackedAttack = cardAttacked.getAttackDamage();

        cardAttacked.setHealth(attackedAttack);
        cardAttacked.setAttackDamage(attackedHealth);

        if (cardAttacked.getHealth() == 0) {
            game.getBoard().get(action.getCardAttacked().getX()).
                    remove(action.getCardAttacked().getY());
        }

        setAttacked(true);
    }
}
