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

    @Test
    public void parse() {
        ClientMessage clientMessage = new CreateGameMessage(1, "lollo");

        try {
            Object object = Parser.parse(clientMessage);
            assertEquals("CreateGameMessageController", object.getClass().getSimpleName());
            assertEquals(clientMessage, ((CreateGameMessageController)object).getClientMessage());
        } catch (ControllerException e) {
            fail();
        }

        clientMessage = new JoinGameMessage(1, "lollo");

        try {
            Object object = Parser.parse(clientMessage);
            assertEquals("JoinGameMessageController", object.getClass().getSimpleName());
            assertEquals(clientMessage, ((PreGameCreationMessageController)object).getClientMessage());
        } catch (ControllerException e) {
            fail();
        }
    }
}