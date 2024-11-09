package gwentstonelite.cards.heroes;

import fileio.ActionsInput;
import fileio.CardInput;
import gwentstonelite.GameSession;
import gwentstonelite.GwentStoneLite;
import gwentstonelite.cards.Card;

import java.util.ArrayList;

public final class GeneralKocioraw extends Card {
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
     * Executes the ability of the General Kocioraw card, which increases the
     * attack damage to all cards in the affected row by 1.
     * After the ability is executed, the card is marked as having attacked.
     * The player's mana is reduced by the mana cost of the ability.
     *
     * @param game The current game session, which provides access to the board
     *             and player information.
     * @param action The action containing details about the affected row
     *               where the ability will take place.
     *               The action includes the row number that will be affected by the ability.
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
