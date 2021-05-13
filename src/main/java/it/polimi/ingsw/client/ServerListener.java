package it.polimi.ingsw.client;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.answerhandler.exceptions.HandlerException;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.ParserException;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.ErrorAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.ParserServer;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerListener implements Runnable{
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final Socket server;
    private ObjectInputStream iStream;
    private LocalGame localGame;

    @Override
    public void run() {
        try {
            iStream = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            logger.error("Can't open the connection to " + server.getInetAddress());
            closeConnection();
            return;
        }
        logger.info("Connected to " + server.getInetAddress());
        try {
            handleServerConnection();
        } catch (IOException e) {
            logger.warn("server " + server.getInetAddress() + " connection dropped");
        }
        closeConnection();
    }

    public ServerListener(Socket server, LocalGame localGame) {
        this.localGame = localGame;
        this.server = server;
    }

    public void closeConnection() {
        try {
            server.close();
        } catch (IOException e) {
            logger.warn("Error happened while closing a connection: " + e.getMessage());
        }
    }

    private void handleServerConnection() throws IOException {
        try {
            while (true) {
                Answer answer = (Answer) iStream.readObject();
                logger.debug("class of message: " + answer.getClass());
                logger.debug("input from client: " + answer);
                handleAnswer(answer);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            logger.error("invalid stream from client, connection closed");
        }
    }

    private synchronized void handleAnswer(Answer answer) throws IOException {
        try {
            Object parsedAnswer = ParserClient.parseAnswer(answer);
            if (parsedAnswer instanceof AnswerHandler){
                ((AnswerHandler)parsedAnswer).handleAnswer(this.localGame);
            } else throw new HandlerException("Error occurred during the handling of the answer");
    } catch (HandlerException | ParserException e) {
            logger.error("something went wrong, name of exception: " + e.getClass().getSimpleName() + "\n associated message: " + e.getMessage());
        }
    }
}