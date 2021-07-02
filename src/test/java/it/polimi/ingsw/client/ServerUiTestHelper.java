package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.ParserServer;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.JoinGameMessageController;
import it.polimi.ingsw.server.model.cards.leader.MarbleLeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * server used to test UI, it can be used to start the game in a specific status. It works only for online games
 */
@Ignore
public class ServerUiTestHelper extends Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private final static int PORT = 16509;

    public static int getPort() {
        return PORT;
    }

    public static void main(String[] args) {
        ControllerManager.getInstance();
        ServerSocket socket;
        try {
            // WE SET TO TEST THIS, SO WE CAN REPEAT WHAT HAPPENS WITHOUT RANDOMNESS GIVING PROBLEMS
            CollectionsHelper.setTest();

            socket = new ServerSocket(PORT);
            logger.info("Server listening on port: " + PORT);
        } catch (IOException e) {
            logger.error("Cannot find a port: " + e.getMessage());
            System.exit(1);
            return;
        }
        run_server(socket);
    }

    public static void run_server(ServerSocket socket) {
        while (true) {
            try {
                Socket client = socket.accept();
                ClientHandlerUiTestHelper clientHandler = new ClientHandlerUiTestHelper(client);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                logger.error("Connection dropped: " + e.getMessage());
            }
        }
    }
}

class ClientHandlerUiTestHelper extends ClientHandler {
    private static final Logger logger = LogManager.getLogger(ClientHandlerUiTestHelper.class);

    public ClientHandlerUiTestHelper(Socket client) {
        super(client);
    }

    @Override
    protected synchronized void handleMessage(ClientMessage clientMessage) throws IOException {
        super.handleMessage(clientMessage);
        Object parsedMessage = null;
        try {
            // CAREFUL: THIS IS JUST FOR TESTING THE UI, IF MULTIPLE MESSAGES ARRIVE WHILE INSIDE AN IF, THERE COULD BE SYNCHRONISATION PROBLEMS
            parsedMessage = ParserServer.parseRequest(clientMessage);
            if (parsedMessage instanceof CreateGameMessageController && ((CreateGameMessage) clientMessage).getPlayersNumber() == 1) {
                // we have created a new single player game
                logger.warn("Hard changing status of game");
                ControllerActionsServerSingle ca = (ControllerActionsServerSingle) controllerManager.getControllerFromMap(answerListener.getGameId());

                // TO TRY DIFFERENT CONFIGURATION OF THE GAME CHANGE THIS METHOD
                ManipulateGameUiTestHelper.setStateOfGame11(answerListener.getGameId(), ca.getGame());

                answerListener.sendAnswer(AnswerFactory.createGameStatusAnswer(ca.getGameId(), answerListener.getPlayerId(), answerListener.getPlayerId(), ca.getGame()));
            } else if ((parsedMessage instanceof JoinGameMessageController) && controllerManager.getControllerFromMap(clientMessage.getGameId()).getGame() != null) {
                // we have created a new multiplayer game
                logger.warn("Hard changing status of game");
                ControllerActionsServerMulti ca = (ControllerActionsServerMulti) controllerManager.getControllerFromMap(clientMessage.getGameId());

                // TO TRY DIFFERENT CONFIGURATION OF THE GAME CHANGE THIS METHOD
                ManipulateGameUiTestHelper.setStateOfGame11(answerListener.getGameId(), ca.getGame());

                ca.sendGameStatusToAll(clientMessage.getGameId(), clientMessage.getPlayerId());
            }
        } catch (ControllerException e) {
            logger.error("cannot parse message or change the state " + e);
        } catch (ModelException e) {
            logger.error("problem changing the state " + e);
        }
    }
}
