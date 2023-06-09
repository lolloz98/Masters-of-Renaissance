package it.polimi.ingsw.client;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.answerhandler.exception.HandlerException;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.messages.ParserException;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * GameHandler for remote games (both single player and multi player)
 * class to handle communication to server.
 * It notifies its observer if the connection is closed.
 */
public class ServerListener extends GameHandler {
    private static final Logger logger = LogManager.getLogger(ServerListener.class);
    private final Socket server;
    private ObjectInputStream iStream;
    private ObjectOutputStream output;
    private final String address;
    private final int port;

    /**
     * Notify the observer when and if the connection to the server is established (then reset to null)
     */
    private Observer isListeningForAnswers = null;

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        try {
            handleServerConnection();
        } catch (IOException e) {
            logger.warn("server " + server.getInetAddress() + " connection dropped");
        }
        notifyObservers();
        closeConnection();
        logger.debug("ending run method of serverListener (closing thread)");
    }

    public ServerListener(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
        server = new Socket(address, port);
        output = new ObjectOutputStream(server.getOutputStream());
        iStream = new ObjectInputStream(server.getInputStream());
        isListeningForAnswers = null;
        logger.info("Connected to " + server.getInetAddress());
    }

    /**
     * Used if we need to send a message (then receive an answer) right after the connection (thus we have no time for the run method to start)
     */
    public ServerListener(String address, int port, Observer isListeningForAnswers) throws IOException {
        this.address = address;
        this.port = port;
        server = new Socket(address, port);
        output = new ObjectOutputStream(server.getOutputStream());
        iStream = new ObjectInputStream(server.getInputStream());
        this.isListeningForAnswers = isListeningForAnswers;
        logger.info("Connected to " + server.getInetAddress());
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
            logger.info("client listening for answers");
            if (isListeningForAnswers != null) {
                isListeningForAnswers.notifyUpdate();
                isListeningForAnswers = null;
            }
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

    private synchronized void handleAnswer(Answer answer) {
        try {
            Object parsedAnswer = ParserClient.parseAnswer(answer);
            logger.debug("Parsed message");
            if (parsedAnswer instanceof AnswerHandler) {
                ((AnswerHandler) parsedAnswer).handleAnswerSync(this.localGame);
            } else throw new HandlerException("Error occurred during the handling of the answer");
        } catch (HandlerException | ParserException e) {
            logger.error("something went wrong, name of exception: " + e.getClass().getSimpleName() + "\n associated message: " + e.getMessage());
        }
    }

    public void dealWithMessage(ClientMessage message) throws IOException {
        output.writeObject(message);
    }

    public Socket getServer() {
        return server;
    }
}
