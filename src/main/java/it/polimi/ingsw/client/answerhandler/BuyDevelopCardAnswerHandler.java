package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.mainactionsanswer.BuyDevelopCardAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuyDevelopCardAnswerHandler extends AnswerHandler{

    private static final Logger logger = LogManager.getLogger(BuyDevelopCardAnswer.class);

    public BuyDevelopCardAnswerHandler(BuyDevelopCardAnswer answer) {
        super(answer);
    }


    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        BuyDevelopCardAnswer serverAnswer=(BuyDevelopCardAnswer) getAnswer();
        LocalPlayer player=localGame.getPlayerById(serverAnswer.getPlayerId());
        LocalBoard localBoard= player.getLocalBoard();

        localGame.getLocalTurn().setMainActionOccurred(true);

        //update the slot
        localBoard.setDevelopCards(serverAnswer.getLocalBoard().getDevelopCards());

        //update normal depots
        localBoard.setResInNormalDepot(serverAnswer.getLocalBoard().getResInNormalDepot());

        localBoard.notifyObserver();

        //update leader depots
        LocalCard toUpdate,updated;
        for(int i=0;i<localBoard.getLeaderCards().size();i++){
            toUpdate=localBoard.getLeaderCards().get(i);
            updated=serverAnswer.getLocalBoard().getLeaderCards().get(i);

            if(toUpdate instanceof LocalDepotLeader)
                ((LocalDepotLeader) toUpdate).setNumberOfRes(((LocalDepotLeader) updated).getNumberOfRes());//this method already calls notifyObserver()

        }

        //update development grid
        localGame.setLocalDevelopmentGrid(serverAnswer.getLocalGrid());
        localGame.getLocalDevelopmentGrid().notifyObserver();

    }
}
