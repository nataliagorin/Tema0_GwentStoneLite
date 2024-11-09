package gwentstonelite.cards.heroes;

import fileio.ActionsInput;
import fileio.CardInput;
import gwentstonelite.GameSession;
import gwentstonelite.GwentStoneLite;
import gwentstonelite.cards.Card;

import java.util.ArrayList;

public final class EmpressThorina extends Card {
    public EmpressThorina(final CardInput card) {
        super(card);
    }


    @Override
    public void useAbility(final GameSession game, final ActionsInput action) {
        if (game.getPlayerTurn() == 1) {
            if (action.getAffectedRow() == GwentStoneLite.ROW0
                    || action.getAffectedRow() == GwentStoneLite.ROW1) {
                ability(game, action);
            } else {
                GwentStoneLite.getOutputCreator().
                        useHeroAbilityError("Selected row does not belong to the enemy.", action);
            }
        } else {
            if (action.getAffectedRow() == GwentStoneLite.ROW2
                    || action.getAffectedRow() == GwentStoneLite.ROW3) {
                ability(game, action);
            } else {
                GwentStoneLite.getOutputCreator().
                        useHeroAbilityError("Selected row does not belong to the enemy.", action);
            }
        }
    }


    /**
     * Executes the ability of Empress Thorina, targeting the card with the highest
     * health in the affected row.
     * This method removes the card with the highest health from the
     * specified row and reduces the
     * player's mana by the mana cost of the ability. The method also
     * marks the ability as having been used by setting the attacked flag to true.
     *
     * @param game The current game session, which provides access
     *             to the board and player information.
     * @param action The action containing the details about which row
     *               to affect and the card's ability.
     */
    public void ability(final GameSession game, final ActionsInput action) {
        int y = -1;
        int maxHealth = 0;
        ArrayList<Card> cards = game.getBoard().get(action.getAffectedRow());

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getHealth() > maxHealth) {
                y = i;
                maxHealth = cards.get(i).getHealth();
            }
        }

        cards.remove(y);

        setAttacked(true);

        if (game.getPlayerTurn() == 1) {
            game.getPlayerOne().setMana(game.getPlayerOne().getMana() - getMana());
        } else {
            game.getPlayerTwo().setMana(game.getPlayerTwo().getMana() - getMana());
        }
    }
}
