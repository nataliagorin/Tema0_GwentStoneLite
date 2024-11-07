package gwentstone_lite.cards.special_cards;

import fileio.ActionsInput;
import gwentstone_lite.GameSession;
import gwentstone_lite.GwentStoneLite;
import gwentstone_lite.cards.Card;

public class TheCursedOne extends Card {
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
     *
     * @param game
     * @param row
     * @return
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
     *
     * @param game
     * @param action
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
