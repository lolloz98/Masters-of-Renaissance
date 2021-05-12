package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.requests.*;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.cards.leader.RequirementResource;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.Depot;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ParserServerTest {

    Object object;
    ClientMessage clientMessage;

    public Object check(ClientMessage clientMessage){
        try {
            Object object = ParserServer.parseRequest(clientMessage);
            assertEquals(clientMessage.getClass().getSimpleName() + "Controller", object.getClass().getSimpleName());
            return object;
        } catch (ControllerException e) {
            fail();
        }
        return null;
    }

    @Test
    public void parseTest() throws ModelException {
        clientMessage = new CreateGameMessage(1, "lollo");
        object = check(clientMessage);
        assertEquals(clientMessage, ((CreateGameMessageController)object).getClientMessage());

        clientMessage = new JoinGameMessage(1, "lollo");
        object = check(clientMessage);
        assertEquals(clientMessage, ((PreGameCreationMessageController)object).getClientMessage());

        clientMessage = new ChooseOneResPrepMessage(1, 2, Resource.GOLD);
        object = check(clientMessage);
        assertEquals(clientMessage, ((ClientMessageController)object).getClientMessage());

        clientMessage = new RemoveLeaderPrepMessage(1,2, new ArrayList<LeaderCard<?>>(){{
            add(new DepotLeaderCard(1,new RequirementResource(Resource.GOLD), new Depot(Resource.GOLD), 2));
        }});
        object = check(clientMessage);
        assertEquals(clientMessage, ((ClientMessageController)object).getClientMessage());

        clientMessage = new ActivateLeaderMessage(1,2,4);
        object = check(clientMessage);
        assertEquals(clientMessage, ((ClientMessageController)object).getClientMessage());

        clientMessage = new DiscardLeaderMessage(1,2,4);
        object = check(clientMessage);
        assertEquals(clientMessage, ((ClientMessageController)object).getClientMessage());

    }
}