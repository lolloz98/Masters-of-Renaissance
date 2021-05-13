package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateGameAnswerHandler extends AnswerHandler{
    private static final Logger logger = LogManager.getLogger(CreateGameAnswerHandler.class);

    public CreateGameAnswerHandler(CreateGameAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        if (localGame instanceof LocalMulti){
            LocalMulti localMulti = (LocalMulti) localGame;
            int id = getAnswer().getPlayerId();
            String name = ((CreateGameAnswer) getAnswer()).getName();
            localMulti.setMainPlayerId(id);
            localMulti.addLocalPlayer(new LocalPlayer(id, name, new LocalBoard()));
        }
        else{
            logger.error("create game answer received by singlePlayer game");
        }
        localGame.setState(LocalGameState.WAITINGPLAYERS);
    }
}
