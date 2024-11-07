package gwentstone_lite.cards.special_cards;

import fileio.ActionsInput;
import gwentstone_lite.GameSession;
import gwentstone_lite.GwentStoneLite;
import gwentstone_lite.cards.Card;

public class Disciple extends Card {
    public Disciple(final Card card) {
        super(card);
    }

    @Override
    public void useAbility(final GameSession game, final ActionsInput action) {
        String errorMessage = "Attacked card does not belong to the current player.";
        Card attacked = game.getBoard().get(action.getCardAttacked().getX()).
                get(action.getCardAttacked().getY());

        if (action.getCardAttacker().getX() == GwentStoneLite.ROW0) {
            if (action.getCardAttacked().getX() == GwentStoneLite.ROW0
                    || action.getCardAttacked().getX() == GwentStoneLite.ROW1) {
                attacked.setHealth(attacked.getHealth() + 2);
                setAttacked(true);
            } else {
                GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
            }
        } else {
            if (action.getCardAttacked().getX() == GwentStoneLite.ROW2
                    || action.getCardAttacked().getX() == GwentStoneLite.ROW3) {
                attacked.setHealth(attacked.getHealth() + 2);
                setAttacked(true);
            } else {
                GwentStoneLite.getOutputCreator().cardUsesAttackError(errorMessage, action);
            }
        }
    }
}
