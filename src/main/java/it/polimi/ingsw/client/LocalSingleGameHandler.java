package it.polimi.ingsw.client;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.messages.ParserException;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.ErrorAnswer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.server.ParserServer;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.exception.WrongColorDeckException;
import it.polimi.ingsw.server.model.exception.WrongLevelDeckException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GameHandler for local single player games.
 */
public class LocalSingleGameHandler extends GameHandler {
    private static final Logger logger = LogManager.getLogger(LocalSingleGameHandler.class);
    ControllerActionsSingleLocal controllerActionsSingleLocal;

    @Override
    public void dealWithMessage(ClientMessage message) {
        new Thread(() -> {
            handleRequestAndAnswer(message);
        }).start();
    }

    private synchronized void handleRequestAndAnswer(ClientMessage message){
        Answer answer = handleRequest(message);
        handleAnswer(answer);
    }

    private synchronized Answer handleRequest(ClientMessage message){
        Object parsedMessage = null;
        try {
            parsedMessage = ParserServer.parseRequest(message);
        } catch (ControllerException e) {
            logger.error("Error while parsing the client request: " + e);
        }

        try{
            if(parsedMessage instanceof CreateGameMessageController){
                // if here -> controllerActions not created yet, thus create it and then
                try {
                    SinglePlayer singlePlayer = new SinglePlayer(new Player(((CreateGameMessage) message).getUserName(), 0));
                    controllerActionsSingleLocal = new ControllerActionsSingleLocal(singlePlayer, 0);
                    return new GameStatusAnswer(0, 0, 0, ConverterToLocalModel.convert(singlePlayer, 0, 0));
                } catch (EmptyDeckException | WrongColorDeckException | WrongLevelDeckException | InvalidArgumentException e) {
                    logger.error("Error occurred while instantiating new player: " + e);
                }
            } else if (parsedMessage instanceof PreGameCreationMessageController) {
                logger.error("PreGameACreationMessage even though we are in a singlePlayer game");
            } else if(parsedMessage instanceof ClientMessageController){
                return ((ClientMessageController) parsedMessage).doAction(controllerActionsSingleLocal);
            }
        }catch (ControllerException e){
            return new ErrorAnswer(0, 0, e.getMessage());
        }
        logger.error("No action found to do with message: " + message);
        return new ErrorAnswer(0, 0, "Something went wrong, please try again the action.");
    }

    private synchronized void handleAnswer(Answer answer){
        Object parsedAnswer = null;
        try {
             parsedAnswer = ParserClient.parseAnswer(answer);
        } catch (ParserException e) {
            logger.error("Error while parsing the client request: " + e);
        }
        if(parsedAnswer instanceof AnswerHandler){
            ((AnswerHandler) parsedAnswer).handleAnswerSync(localGame);
        }else{
            logger.error("No handler found to do with action: " + answer);
        }
    }

    // We do not need to do anything here
    @Override
    public void run() {

    }
}
