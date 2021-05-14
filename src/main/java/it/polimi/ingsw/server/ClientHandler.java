package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.ErrorAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.GameStatusMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
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

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    private final ControllerManager controllerManager = ControllerManager.getInstance();

    private final Socket client;
    private ObjectOutputStream oStream;
    private ObjectInputStream iStream;

    private AnswerListener answerListener;

    public ClientHandler(Socket client) {
        this.client = client;
        // todo: add reference to controller/view
    }

    private void handleClientConnection() throws IOException {
        try {
            while (true) {
                ClientMessage clientMessage = (ClientMessage) iStream.readObject();
                logger.debug("class of message: " + clientMessage.getClass());
                logger.debug("input from client: " + clientMessage);
                // TODO modify this
                handleMessage(clientMessage);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            logger.error("invalid stream from client, connection closed");
        }
    }

    private synchronized void handleMessage(ClientMessage clientMessage) throws IOException {
        try {
            Object parsedMessage = ParserServer.parseRequest(clientMessage);
            if(parsedMessage instanceof CreateGameMessageController){
                // if here -> controllerActions not created yet, thus create it and then
                Answer answer = ((CreateGameMessageController) parsedMessage).doAction(answerListener);
                answerListener.sendAnswer(answer);
            } else if (parsedMessage instanceof PreGameCreationMessageController) {
                    controllerManager.getControllerFromMap(((PreGameCreationMessageController) parsedMessage).getClientMessage().getGameId())
                            .doPreGameAction((PreGameCreationMessageController) parsedMessage, answerListener);
            } else if(parsedMessage instanceof GameStatusMessageController){
                controllerManager.getControllerFromMap(((GameStatusMessageController) parsedMessage).getClientMessage().getGameId())
                        .doGetStatusAction((GameStatusMessageController) parsedMessage);
            } else if (parsedMessage instanceof ClientMessageController) {
                controllerManager.getControllerFromMap(((ClientMessageController) parsedMessage).getClientMessage().getGameId())
                        .doAction((ClientMessageController) parsedMessage);

            } else throw new ControllerException("Error occurred during the handling of the request");
        } catch (ControllerException e) {
            logger.error("something went wrong, name of exception: " + e.getClass().getSimpleName() + "\n associated message: " + e.getMessage());
            answerListener.sendAnswer(new ErrorAnswer(clientMessage.getGameId(), clientMessage.getPlayerId(), e.getMessage()));
        }
    }

    @Override
    public void run() {
        try {
            oStream = new ObjectOutputStream(client.getOutputStream());
            iStream = new ObjectInputStream(client.getInputStream());
            answerListener = new AnswerListener(oStream);
        } catch (IOException e) {
            logger.error("Can't open the connection to " + client.getInetAddress());
            closeConnection();
            return;
        }

        logger.info("Connected to " + client.getInetAddress());

        try {
            handleClientConnection();
        } catch (IOException e) {
            logger.warn("client " + client.getInetAddress() + " connection dropped");
        }

        closeConnection();
    }

    public void closeConnection() {
        try {
            // fixme: for now, if a client drop the connection the game is cancelled
            if(answerListener.getGameId() != -1){
                try {
                    ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(answerListener.getGameId());
                    ca.destroyGame("A player lost the connection", answerListener.getPlayerId(), true);
                } catch (NoSuchControllerException e) {
                    logger.warn("trying to destroy an already destroyed game");
                }
            }
            client.close();
        } catch (IOException e) {
            logger.warn("Error happened while closing a connection: " + e.getMessage());
        }
    }
}
