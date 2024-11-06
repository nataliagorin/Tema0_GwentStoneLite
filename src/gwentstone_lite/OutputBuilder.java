package gwentstone_lite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import gwentstone_lite.cards.Card;

import java.util.ArrayList;

public class OutputBuilder {
    private ArrayNode output;
    private ObjectMapper objMapper;

    public OutputBuilder(final ArrayNode output) {
        this.output = output;
        this.objMapper = new ObjectMapper();
    }


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

    public void playerTurnOutput(final int playerTurn, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("output", playerTurn);

        output.add(objNode);
    }

    public void playerHeroOutput(final Card heroCard, final ActionsInput action) {
        ObjectNode objNode = objMapper.createObjectNode();

        objNode.put("command", action.getCommand());
        objNode.put("playerIdx", action.getPlayerIdx());
        objNode.set("output", createHeroObject(heroCard));

        output.add(objNode);
    }

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
}
