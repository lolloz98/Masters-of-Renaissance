package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Answer Handler that handles the join of a player in a multi-player game.
 */
public class JoinGameAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(JoinGameAnswerHandler.class);

    public JoinGameAnswerHandler(JoinGameAnswer answer) {
        super(answer);
    }

    /**
     * method that updates the players in the local game.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        ArrayList<LocalPlayer> localPlayers = new ArrayList<>();
        JoinGameAnswer joinGameAnswer = ((JoinGameAnswer) getAnswer());
        if (localGame instanceof LocalMulti) {
            for (int i = 0; i < joinGameAnswer.getPlayerIds().size(); i++) {
                localPlayers.add(new LocalPlayer(joinGameAnswer.getPlayerIds().get(i), joinGameAnswer.getPlayerNames().get(i), new LocalBoard()));
            }
            LocalMulti localMulti = (LocalMulti) localGame;
            localMulti.setLocalPlayers(localPlayers);
            localMulti.setGameId(joinGameAnswer.getGameId());
            localMulti.setState(LocalGameState.WAITING_PLAYERS);
            localMulti.notifyObservers();
        } else {//in the single player no one must join
            logger.error("create game answer received by singlePlayer game");
        }
    }
}
