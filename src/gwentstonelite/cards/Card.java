package gwentstonelite.cards;

import fileio.ActionsInput;
import fileio.CardInput;
import gwentstonelite.GameSession;
import gwentstonelite.GwentStoneLite;

import java.util.ArrayList;

public class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean frozen;
    private boolean attacked;

    public Card() {
    }

    public Card(final Card card) {
        this.mana = card.mana;
        this.attackDamage = card.attackDamage;
        this.health = card.health;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
        this.frozen = card.frozen;
        this.attacked = card.attacked;
    }

    public Card(final CardInput card) {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.frozen = false;
        this.attacked = false;
    }


    public final int getMana() {
        return mana;
    }

    /**
     * Sets the mana cost for the card.
     * This method assigns the specified mana value to the card.
     *
     * @param mana the mana cost to be set for the card.
     *             The value should be a positive integer
     *             representing the mana cost.
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Sets the attack damage for the card.
     * This method assigns the specified attack damage value to the card.
     *
     * @param attackDamage the attack damage to be set for the card.
     *                     The value should be a positive integer
     *                     representing the card's attack damage.
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public final int getHealth() {
        return health;
    }

    /**
     * Sets the health value for the card.
     * This method assigns the specified health value to the card.
     *
     * @param health the health value to be set for the card.
     *               The value should be a positive integer
     *               representing the card's health.
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description for the card.
     * This method assigns the specified description text to the card.
     *
     * @param description the description text to be set for the card.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Sets the colors associated with the card.
     * This method assigns a list of colors to the card.
     *
     * @param colors the list of colors to be set for the card.
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the card.
     * This method assigns the specified name to the card.
     *
     * @param name the name to be set for the card.
     *             This value should be a string representing the card's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    public final boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets the frozen state of the card.
     * This method assigns a boolean value to indicate whether the card is frozen.
     *
     * @param frozen a boolean value indicating whether the card is frozen.
     *               true if the card is frozen, false otherwise.
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Returns whether the card has attacked.
     * This method checks the current state of the card
     * to determine if it has already attacked during the turn.
     *
     * @return true if the card has attacked, fals otherwise.
     */
    public final boolean hasAttacked() {
        return attacked;
    }

    /**
     * Sets the attacked state of the card.
     * This method assigns a boolean value to indicate whether the card has attacked.
     *
     * @param attacked a boolean value indicating whether the card has attacked.
     *                 true if the card has attacked, false otherwise.
     */
    public void setAttacked(final boolean attacked) {
        this.attacked = attacked;
    }


    /**
     * Executes an attack using the card's attack damage. The attack can either target
     * another card or the enemy's hero, depending on the action performed.
     * If the action is to attack another card, the method
     * reduces the health of the targeted card by the attack damage.
     * If the targeted card's health drops to zero or below,
     * the card is removed from the board.
     * If the action targets a hero, the hero's health is reduced
     * by the attacking card's damage, and the attacker is marked as having attacked.
     * If a hero's health reaches zero or below, the game ends,
     * and the player who killed the enemy hero is declared the winner.
     *
     * @param game the GameSession object that holds the current state of the game,
     *             including the board and players.
     * @param action the ActionsInput object containing details
     *               of the action, including the target card and attacker.
     */
    public void useAttack(final GameSession game, final ActionsInput action) {
        if (action.getCommand().equals("cardUsesAttack")) {
            Card cardAttacked = game.getBoard().
                    get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY());

            cardAttacked.setHealth(cardAttacked.getHealth() - attackDamage);

            if (cardAttacked.getHealth() <= 0) {
                game.getBoard().get(action.getCardAttacked()
                        .getX()).remove(action.getCardAttacked().getY());
            }

            attacked = true;
        } else {
            Card hero = null;
            Card attacker = game.getBoard().
                    get(action.getCardAttacker().getX())
                    .get(action.getCardAttacker().getY());

            if (game.getPlayerTurn() == 1) {
                hero = game.getPlayerTwo().getHeroCard();
            } else {
                hero = game.getPlayerOne().getHeroCard();
            }

            hero.setHealth(hero.getHealth() - attacker.getAttackDamage());
            attacker.setAttacked(true);

            if (hero.getHealth() <= 0) {
                if (game.getPlayerTurn() == 1) {
                    GwentStoneLite.getOutputCreator().
                            gameEnded("Player one killed the enemy hero.");
                    game.getPlayerOne().
                            setGamesWon(game.getPlayerOne().getGamesWon() + 1);
                } else {
                    GwentStoneLite.getOutputCreator().
                            gameEnded("Player two killed the enemy hero.");
                    game.getPlayerTwo().
                            setGamesWon(game.getPlayerTwo().getGamesWon() + 1);
                }

                game.setGameEnded(true);
            }
        }
    }

    /**
     * @param game the GameSession object that holds the current state of the game.
     * @param action the actionsInput object containing details
     *              about the action being performed such as the
     *               card using the ability and any targets involved.
     */
    public void useAbility(final GameSession game, final ActionsInput action) {
    }
}
