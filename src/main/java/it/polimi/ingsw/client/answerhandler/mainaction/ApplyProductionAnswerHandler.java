package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalProductionLeader;
import it.polimi.ingsw.messages.answers.mainactionsanswer.ApplyProductionAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ApplyProductionAnswerHandler extends AnswerHandler {

    private static final Logger logger = LogManager.getLogger(ApplyProductionAnswer.class);

    public ApplyProductionAnswerHandler(ApplyProductionAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        ApplyProductionAnswer serverAnswer = (ApplyProductionAnswer) getAnswer();
        LocalPlayer localPlayer;
        LocalBoard localBoard;

        localGame.getLocalTurn().setMainActionOccurred(true);
        localGame.getLocalTurn().setProductionsActivated(true);

        localPlayer = localGame.getPlayerById(serverAnswer.getPlayerId());
        localBoard = localPlayer.getLocalBoard();

        updateBoard(localBoard, serverAnswer);

    }


    private void updateBoard(LocalBoard localBoard, ApplyProductionAnswer serverAnswer) {

        //update the normal depots
        localBoard.setResInNormalDepot(serverAnswer.getResInNormalDepots());

        //update the leader depots
        localBoard.updateLeaderDepots(serverAnswer.getLeaderDepots());

        //update the production
        int whichProd = serverAnswer.getWhichProdSlot();
        switch (whichProd) {
            case 0:
                localBoard.getBaseProduction().setResToFlush(serverAnswer.getResToFlush());
                break;
            case 1:
            case 2:
            case 3: {
                ArrayList<LocalDevelopCard> localDevelopSlot = localBoard.getDevelopCards().get(whichProd - 1);
                localDevelopSlot.get(localDevelopSlot.size() - 1).setResToFlush(serverAnswer.getResToFlush());
                break;
            }
            case 4:
            case 5: {
                if (localBoard.getLeaderCards().get(whichProd - 4) instanceof LocalProductionLeader) {
                    LocalProductionLeader toUpdate = (LocalProductionLeader) localBoard.getLeaderCards().get(whichProd - 4);
                    toUpdate.setResToFlush(serverAnswer.getResToFlush());
                } else
                    logger.error("something wrong happened: illegal which prod parameter in + " + logger.getName()+ " the leader at that position is not a production leader");
            break;
            }
            default:
                logger.error("something wrong happened: illegal which prod parameter in + " + logger.getName());
                break;
        }

        localBoard.notifyObserver();
    }
}
