package gwentstone_lite.cards.heroes;

import fileio.ActionsInput;
import fileio.CardInput;
import gwentstone_lite.GameSession;
import gwentstone_lite.GwentStoneLite;
import gwentstone_lite.cards.Card;

import java.util.ArrayList;

public class EmpressThorina extends Card {
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
     *
     * @param game
     * @param action
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
