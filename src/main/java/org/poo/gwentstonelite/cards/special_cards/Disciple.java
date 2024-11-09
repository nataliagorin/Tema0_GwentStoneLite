package org.poo.gwentstonelite.cards.special_cards;

import org.poo.fileio.ActionsInput;
import org.poo.gwentstonelite.GameSession;
import org.poo.gwentstonelite.GwentStoneLite;
import org.poo.gwentstonelite.cards.Card;

public final class Disciple extends Card {
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
