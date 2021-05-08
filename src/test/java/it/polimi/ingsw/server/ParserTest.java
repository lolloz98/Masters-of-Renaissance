package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    Object object;
    ClientMessage clientMessage;

    public Object check(ClientMessage clientMessage){
        try {
            Object object = Parser.parse(clientMessage);
            assertEquals(clientMessage.getClass().getSimpleName() + "Controller", object.getClass().getSimpleName());
            return object;
        } catch (ControllerException e) {
            fail();
        }
        return null;
    }

    @Test
    public void parseTest() {
        clientMessage = new CreateGameMessage(1, "lollo");
        object = check(clientMessage);
        assertEquals(clientMessage, ((CreateGameMessageController)object).getClientMessage());

        clientMessage = new JoinGameMessage(1, "lollo");
        object = check(clientMessage);
        assertEquals(clientMessage, ((PreGameCreationMessageController)object).getClientMessage());
    }
}