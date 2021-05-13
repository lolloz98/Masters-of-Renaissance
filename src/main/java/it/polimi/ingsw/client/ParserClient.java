package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.Parser;
import it.polimi.ingsw.messages.ParserException;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.ParserServer;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParserClient {
    private static final Logger logger = LogManager.getLogger(ParserServer.class);

    public static Object parseAnswer(Answer answer) throws ParserException {
        // todo: change package name and answer
        String packageName = "it.polimi.ingsw.client.answerhandler";
        String suffix = "Handler";
        return Parser.parse(answer, packageName, suffix);
    }
}
