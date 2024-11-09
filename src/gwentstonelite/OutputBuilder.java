package gwentstonelite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import gwentstonelite.cards.Card;

import java.util.ArrayList;

public final class OutputBuilder {
    private ArrayNode output;
    private ObjectMapper objMapper;

    public OutputBuilder(final ArrayNode output) {
        this.output = output;
        this.objMapper = new ObjectMapper();
    }

    /**
     * Creates and adds a JSON object to the output representing a list of cards.
     * This method creates a JSON structure that includes the command
     * and player index from the action,
     * followed by an array of card objects representing the provided list of cards.
     *
     * @param cards The list of cards to be output, typically from a player's
     *              hand, deck, or cards on the table.
     * @param action The action object containing the command and player index information.
     */
    public void cardsOutput(final ArrayList<Card> cards, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("playerIdx", action.getPlayerIdx());

        ArrayNode arrayNode = objMapper.createArrayNode();
        for (Card card : cards) {
           arrayNode.add(createCardObject(card));
        }
        objNode.set("output", arrayNode);

        output.add(objNode);
    }

    /**
     * Creates a JSON object representing a card with its properties.
     * This method converts a `Card` object into a JSON object,
     * including its mana, attack damage, health,
     * description, colors, and name. The returned JSON object can be used
     * to represent the card in a serialized format for display or further processing.
     *
     * @param card The `Card` object to be converted into a JSON object.
     * @return A JSON object representing the properties of the provided card.
     */
    public ObjectNode createCardObject(final Card card) {
        ObjectNode cardObject = objMapper.createObjectNode();

        cardObject.put("mana", card.getMana());
        cardObject.put("attackDamage", card.getAttackDamage());
        cardObject.put("health", card.getHealth());
        cardObject.put("description", card.getDescription());

        ArrayNode colors = objMapper.createArrayNode();
        for (String color : card.getColors()) {
            colors.add(color);
        }
        cardObject.set("colors", colors);

        cardObject.put("name", card.getName());

        return cardObject;
    }

    /**
     * Creates a JSON object to represent the current player's turn.
     * This method generates a JSON object containing the current player's
     * turn number and returns it in a serialized format. The output is intended to
     * communicate which player is currently active in the game.
     *
     * @param playerTurn The current player's turn number.
     * @param action The ActionsInput object containing the command for the output.
     */
    public void playerTurnOutput(final int playerTurn, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("output", playerTurn);

        output.add(objNode);
    }

    /**
     * Creates a JSON object to represent the player's hero card.
     * This method generates a JSON object containing the player's hero card details,
     * including its attributes and returns it in a serialized format. The output is
     * intended to communicate the hero card's state for the specified player.
     *
     * @param heroCard The hero card to be represented in the output.
     * @param action The ActionsInput object containing the command and player index.
     */
    public void playerHeroOutput(final Card heroCard, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("playerIdx", action.getPlayerIdx());
        objNode.set("output", createHeroObject(heroCard));

        output.add(objNode);
    }

    /**
     * Creates a JSON object representing the hero card's attributes.
     * This method generates a JSON object containing the essential attributes
     * of the provided hero card, including its mana, description, colors, name, and health.
     * The generated object is designed to be serialized into JSON format
     * for communication or storage.
     *
     * @param heroCard The hero card whose attributes are being represented in the output.
     * @return A JSON object containing the hero card's attributes, ready for serialization.
     */
    public ObjectNode createHeroObject(final Card heroCard) {
        ObjectNode heroObject = objMapper.createObjectNode();

        heroObject.put("mana", heroCard.getMana());
        heroObject.put("description", heroCard.getDescription());

        ArrayNode colors = objMapper.createArrayNode();
        for (String color : heroCard.getColors()) {
            colors.add(color);
        }
        heroObject.set("colors", colors);

        heroObject.put("name", heroCard.getName());
        heroObject.put("health", heroCard.getHealth());

        return heroObject;
    }

    /**
     * Creates a JSON object representing the mana of a player.
     * This method generates a JSON object that contains the current
     * mana of the player specified by the action input.
     * The generated JSON object is used to communicate the player's mana status in the game.
     *
     * @param player The player whose mana is to be included in the output.
     * @param action The action input that contains the command and player index.
     */
    public void playerManaOutput(final Player player, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("playerIdx", action.getPlayerIdx());
        objNode.put("output", player.getMana());

        output.add(objNode);
    }

    /**
     * Creates a JSON object representing the current state of the game board.
     * This method generates a JSON object that contains the current arrangement
     * of cards on the game board. It includes all rows of the board, with
     * each row represented as an array of card objects.
     *
     * @param board The current game board represented as a 2D list of Card objects.
     * @param action The action input containing the command associated
     *               with the board output request.
     */
    public void boardOutput(final ArrayList<ArrayList<Card>> board, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());

        ArrayNode bigArray = objMapper.createArrayNode();

        for (ArrayList<Card> row : board) {
            ArrayNode arrayNode = objMapper.createArrayNode();

            for (Card card : row) {
                arrayNode.add(createCardObject(card));
            }

            bigArray.add(arrayNode);
        }
        objNode.set("output", bigArray);

        output.add(objNode);
    }

    /**
     * Creates a JSON object to represent an error message when a player
     * attempts to place a card on the table.
     * This method generates a JSON object containing details about the error
     * encountered when a player tries to place a card on the table.
     * The JSON includes the command associated with the action, the index of the card
     * in the player's hand, and the error message explaining why the action failed.
     *
     * @param errorMessage The error message explaining why the card placement failed.
     * @param action The action input containing the details of the card placement attempt.
     */
    public void createPlaceCardError(final String errorMessage, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("handIdx", action.getHandIdx());
        objNode.put("error", errorMessage);

        output.add(objNode);
    }

    /**
     * Creates a JSON object to represent the result of retrieving
     * a card at a specific position on the board,
     * including any error message if applicable.
     *
     * @param card The card found at the specified position on the board,
     *            or null if no card is found.
     * @param action The action input containing the details of the position being queried.
     * @param errorMessage A string containing an error message, or null if no error occurred.
     */
    public void cardAtPositionOutput(final Card card, final ActionsInput action,
                                     final String errorMessage) {

        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("x", action.getX());
        objNode.put("y", action.getY());

        if (errorMessage.equals("null")) {
            objNode.set("output", createCardObject(card));
        } else {
            objNode.put("output", errorMessage);
        }

        output.add(objNode);
    }

    /**
     * Creates a JSON response indicating the end of the game.
     * This method generates a JSON object containing a message that signals
     * the end of the game, typically indicating the winner or the reason for
     * the game's termination.
     *
     * @param message A string message describing the outcome of the game.
     */
    public void gameEnded(final String message) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("gameEnded", message);

        output.add(objNode);
    }

    /**
     * Generates a JSON response when an error occurs during a card attack or ability usage.
     * This method constructs a JSON object that includes the command that triggered the error,
     * the coordinates of the attacker and the attacked card, and the error message
     * explaining the reason for the failure.
     *
     * @param errorMessage The error message explaining why the attack or ability usage failed.
     *                     This message is included in the output to notify the user of the issue.
     * @param action The action that triggered the error, which contains the relevant details
     *               such as the command, attacker card coordinates, and attacked card
     *               coordinates.
     *               The command and coordinates of the involved cards
     *               are included in the JSON response.
     */
    public void cardUsesAttackError(final String errorMessage, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());

        ObjectNode attacker = objMapper.createObjectNode();
        attacker.put("x", action.getCardAttacker().getX());
        attacker.put("y", action.getCardAttacker().getY());
        objNode.set("cardAttacker", attacker);

        if (action.getCommand().equals("cardUsesAttack")
                || action.getCommand().equals("cardUsesAbility")) {
            ObjectNode attacked = objMapper.createObjectNode();
            attacked.put("x", action.getCardAttacked().getX());
            attacked.put("y", action.getCardAttacked().getY());
            objNode.set("cardAttacked", attacked);
        }

        objNode.put("error", errorMessage);

        output.add(objNode);
    }

    /**
     * Creates a JSON response indicating an error that occurred while using a hero's ability.
     * This method generates a JSON object containing an error message when the player
     * attempts to use a hero's ability but encounters an issue.
     *
     * @param errorMessage A string describing the error that occurred.
     * @param action The action that triggered the error, containing information about
     *               the affected row and other relevant details.
     */
    public void useHeroAbilityError(final String errorMessage, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("affectedRow", action.getAffectedRow());
        objNode.put("error", errorMessage);

        output.add(objNode);
    }

    /**
     * Generates a JSON response that lists all the frozen cards on the game board.
     * This method iterates over the board, checks for frozen cards, and adds them
     * to a JSON array that is then included in the response.
     *
     * @param board The current state of the game board, represented as a 2D list of cards.
     *              Each row on the board contains a list of cards.
     * @param action The action that triggered the request for frozen cards,
     *              providing details such as the command.
     */
    public void frozenCardsOutput(final ArrayList<ArrayList<Card>> board,
                                  final ActionsInput action) {

        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());

        ArrayNode arrayNode = objMapper.createArrayNode();
        for (ArrayList<Card> row : board) {
            for (Card card : row) {
                if (card.isFrozen()) {
                    arrayNode.add(createCardObject(card));
                }
            }
        }
        objNode.set("output", arrayNode);

        output.add(objNode);
    }

    /**
     * Generates a JSON response that includes the total number of games played.
     * This method retrieves the total games played from the static GwentStoneLite class
     * and constructs a JSON object containing this information along
     * with the command from the action.
     *
     * @param action The action that triggered the request for the total number of games played.
     *
     */
    public void totalGamesOutput(final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("output", GwentStoneLite.getGamesPlayed());

        output.add(objNode);
    }

    /**
     * Generates a JSON response that includes the number of games won by a specific player.
     * This method retrieves the number of games won by the player and constructs a JSON object
     * containing this information along with the command from the action.
     *
     * @param player The player whose number of games won is being reported.
     *               The `getGamesWon()` method is used to fetch the number of games they have won.
     * @param action The action that triggered the request for the player's win count.
     *               The command from this action is included in the output JSON.
     */
    public void playerWinsOutput(final Player player, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("output", player.getGamesWon());

        output.add(objNode);
    }


}
