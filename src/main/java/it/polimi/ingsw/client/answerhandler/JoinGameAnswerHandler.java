package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class JoinGameAnswerHandler extends AnswerHandler{
    private static final Logger logger = LogManager.getLogger(JoinGameAnswerHandler.class);

    public JoinGameAnswerHandler(JoinGameAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        ArrayList<LocalPlayer> localPlayers = new ArrayList<>();
        JoinGameAnswer joinGameAnswer = ((JoinGameAnswer)getAnswer());
        if (localGame instanceof LocalMulti){
            for(int i = 0; i<joinGameAnswer.getPlayerIds().size(); i++){
                localPlayers.add(new LocalPlayer(joinGameAnswer.getPlayerIds().get(i), joinGameAnswer.getPlayerNames().get(i), new LocalBoard()));
            }
            LocalMulti localMulti = (LocalMulti) localGame;
            localMulti.setLocalPlayers(localPlayers);
            localMulti.setGameId(joinGameAnswer.getGameId());
            localMulti.setState(LocalGameState.WAITINGPLAYERS);
            localMulti.notifyObservers();
        }
        else{
            logger.error("create game answer received by singlePlayer game");
        }
    }
}
