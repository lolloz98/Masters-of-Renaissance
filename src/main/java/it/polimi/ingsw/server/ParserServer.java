package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Parser;
import it.polimi.ingsw.messages.ParserException;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Parses Message to MessageController
 */
public final class ParserServer {
    private static final Logger logger = LogManager.getLogger(ParserServer.class);

    public static Object parseRequest(ClientMessage clientMessage) throws ControllerException{
        // Get all classes in the package with controller Requests
        String packageName = "it.polimi.ingsw.server.controller.messagesctr";
        String suffix = "Controller";
        try {
            return Parser.parse(clientMessage, packageName, suffix);
        } catch (ParserException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}
