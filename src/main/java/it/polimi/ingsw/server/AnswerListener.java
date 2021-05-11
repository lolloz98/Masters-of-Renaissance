package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.answers.Answer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Class that handles the sending of the answers to the client
 */
public class AnswerListener {
    private static final Logger logger = LogManager.getLogger(AnswerListener.class);

    private int playerId = -1;
    private final ObjectOutputStream oStream;

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        if(playerId == -1) logger.error("the playerId was not set on this answer listener");
        return playerId;
    }

    public AnswerListener(ObjectOutputStream oStream){
        this.oStream = oStream;
    }

    public void sendAnswer(Answer answer){
        try {
            oStream.writeObject(answer);
        } catch (IOException e) {
            logger.error("error while sending an answer: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
