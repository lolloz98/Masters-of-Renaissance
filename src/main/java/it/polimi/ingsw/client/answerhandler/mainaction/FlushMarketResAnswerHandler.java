package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FlushMarketResAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(FlushMarketResAnswerHandler.class);

    public FlushMarketResAnswerHandler(FlushMarketResAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FlushMarketResAnswer serverAnswer=(FlushMarketResAnswer) getAnswer();
        LocalBoard localBoard;

        //update the turn
        localGame.getLocalTurn().setMarketActivated(false);

        //reset market
        localGame.getLocalMarket().resetCombinations();

        //update the faith tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        //update normal depots
        localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();
        localBoard.setResInNormalDepot(serverAnswer.getResInNormalDeposit());

        //update leader depots
        localBoard.updateLeaderDepots(serverAnswer.getLocalDepotLeaders());

        //update lorenzo track
        if(localGame instanceof LocalSingle){
            LocalTrack lorenzoTrack= serverAnswer.getLorenzoTrack();
            if(lorenzoTrack==null)
                logger.error("the localGame is instance of localSingle and it is passed a null lorenzo track to update in "+ logger.getName());
            ((LocalSingle)localGame).setLorenzoTrack(lorenzoTrack);
        }

        // update history
        if(localGame instanceof LocalMulti){
            LocalMulti localMulti = (LocalMulti) localGame;
            String actionDescription;
            if(serverAnswer.getPlayerId() == localMulti.getMainPlayerId()){
                actionDescription = "You used the market";
            } else {
                actionDescription = localMulti.getPlayerById(serverAnswer.getPlayerId()).getName() + " used the market";
            }
            localMulti.getLocalTurn().getHistory().add(actionDescription);
        }

        localBoard.notifyObservers();
    }
}
