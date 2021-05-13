package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStatusAnswerHandler extends AnswerHandler{
    private static final Logger logger = LogManager.getLogger(GameStatusAnswerHandler.class);

    public GameStatusAnswerHandler(GameStatusAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        GameStatusAnswer gameStatusAnswer = (GameStatusAnswer) getAnswer();
        if(localGame instanceof LocalMulti){
            LocalMulti localMulti = (LocalMulti) localGame;
            if(gameStatusAnswer.getGame() instanceof LocalMulti) {
                localMulti.setLocalTurn(((LocalMulti) gameStatusAnswer.getGame()).getLocalTurn());
                localMulti.setLocalPlayers(((LocalMulti) gameStatusAnswer.getGame()).getLocalPlayers());
                localMulti.setMainPlayerId(((LocalMulti) gameStatusAnswer.getGame()).getMainPlayerId());
                localMulti.setState(LocalGameState.PREP_RESOURCES); // fixme only works when creating a game, not for reloading an ongoing game
            } else {
                logger.error("Game type mismatch");
            }
        } else if (localGame instanceof LocalSingle){
            LocalSingle localSingle = (LocalSingle) localGame;
            if(gameStatusAnswer.getGame() instanceof LocalSingle) {
                localSingle.setLocalTurn(((LocalSingle) gameStatusAnswer.getGame()).getLocalTurn());
                localSingle.setMainPlayer(((LocalSingle) gameStatusAnswer.getGame()).getMainPlayer());
                localSingle.setLorenzoTrack(((LocalSingle) gameStatusAnswer.getGame()).getLorenzoTrack());
                localSingle.setState(LocalGameState.READY);
            } else {
                logger.error("Game type mismatch");
            }
        }
        localGame.setGameId(gameStatusAnswer.getGame().getGameId());
        localGame.setLocalMarket(gameStatusAnswer.getGame().getLocalMarket());
        localGame.setLocalDevelopmentGrid(gameStatusAnswer.getGame().getLocalDevelopmentGrid());
    }
}
