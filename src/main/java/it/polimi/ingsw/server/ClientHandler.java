package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.messages.answers.ErrorAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.BeforeControllerActionsMessageController;
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
                try {
                    handleMessage(clientMessage);
                } catch (ControllerException e) {
                    // todo send back an error message to the client
                    logger.error("something went wrong, name of exception: " + e.getClass().getSimpleName() + "\n associated message: " + e.getMessage());
                    oStream.writeObject(new ErrorAnswer(e.getMessage()));
                }
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            logger.error("invalid stream from client, connection closed");
        }
    }

    private void handleMessage(ClientMessage clientMessage) throws ControllerException, IOException {
        Object parsedMessage = Parser.parse(clientMessage);
        if (parsedMessage instanceof BeforeControllerActionsMessageController) {
            Answer answer = ((BeforeControllerActionsMessageController) parsedMessage).doAction();
            oStream.writeObject(answer);

        } else if (parsedMessage instanceof ClientMessageController) {
            // todo: handle the answers when the model gets updated
            controllerManager.getControllerFromMap(((ClientMessageController) parsedMessage).getClientMessage().getGameId())
                    .doAction((ClientMessageController)parsedMessage);

        } else throw new ControllerException("Error occurred during the handling of the request");
    }

    @Override
    public void run() {
        try {
            oStream = new ObjectOutputStream(client.getOutputStream());
            iStream = new ObjectInputStream(client.getInputStream());
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
            client.close();
        } catch (IOException e) {
            logger.warn("Error happened while closing a connection: " + e.getMessage());
        }
    }
}
