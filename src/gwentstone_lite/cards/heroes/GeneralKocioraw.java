package gwentstone_lite.cards.heroes;

import fileio.ActionsInput;
import fileio.CardInput;
import gwentstone_lite.GameSession;
import gwentstone_lite.GwentStoneLite;
import gwentstone_lite.cards.Card;

import java.util.ArrayList;

public class GeneralKocioraw extends Card {
    public GeneralKocioraw(final CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(final GameSession game, final ActionsInput action) {
        if (game.getPlayerTurn() == 1) {
            if (action.getAffectedRow() == GwentStoneLite.ROW2
                    || action.getAffectedRow() == GwentStoneLite.ROW3) {
                ability(game, action);
            } else {
                GwentStoneLite.getOutputCreator().useHeroAbilityError(
                        "Selected row does not belong to the current player.", action);
            }
        } else {
            if (action.getAffectedRow() == GwentStoneLite.ROW0
                    || action.getAffectedRow() == GwentStoneLite.ROW1) {
                ability(game, action);
            } else {
                GwentStoneLite.getOutputCreator().useHeroAbilityError(
                        "Selected row does not belong to the current player.", action);
            }
        }
    }

    /**
     *
     * @param game
     * @param action
     */
    public void ability(final GameSession game, final ActionsInput action) {
        ArrayList<Card> cards = game.getBoard().get(action.getAffectedRow());

        for (Card card : cards) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }

        setAttacked(true);

        if (game.getPlayerTurn() == 1) {
            game.getPlayerOne().setMana(game.getPlayerOne().getMana() - getMana());
        } else {
            game.getPlayerTwo().setMana(game.getPlayerTwo().getMana() - getMana());
        }
    }
}
