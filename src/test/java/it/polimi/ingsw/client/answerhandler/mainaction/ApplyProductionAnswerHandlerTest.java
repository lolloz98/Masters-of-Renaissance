package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.messages.answers.mainactionsanswer.ApplyProductionAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ApplyProductionAnswerHandlerTest {
    private LocalSingle localSingle;

    @Before
    public void setUp(){
        try {
            localSingle= AnswerHandlerTestHelper.getGameInReadyState();
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }

    }

    @Test
    public void testHandleAnswer(){
        //add a production in the game
        LocalDevelopCard card=localSingle.getLocalDevelopmentGrid().getTopDevelopCards()[1][1];
        AnswerHandlerTestHelper.addADevelopCard(card,0,localSingle);

        //add a depot leader in the main player board
        AnswerHandlerTestHelper.addADepotLeader(localSingle);

        LocalDepotLeader leaderModified=AnswerHandlerTestHelper.getADepotLeader();

        ApplyProductionAnswer serverAnswer=new ApplyProductionAnswer(0,1,AnswerHandlerTestHelper.getResToFlush(),AnswerHandlerTestHelper.getResInDepot(),new ArrayList<>(){{add(leaderModified);}},0);
        ApplyProductionAnswerHandler handler=new ApplyProductionAnswerHandler(serverAnswer);
        handler.handleAnswer(localSingle);

        LocalBoard localBoard=localSingle.getMainPlayer().getLocalBoard();

        assertEquals(AnswerHandlerTestHelper.getResToFlush(),localBoard.getBaseProduction().getResToFlush());
        assertEquals(AnswerHandlerTestHelper.getResInDepot(),localBoard.getResInNormalDepot());
        assertEquals(AnswerHandlerTestHelper.getADepotLeader().getNumberOfRes(),((LocalDepotLeader)localBoard.getLeaderCards().get(localBoard.getLeaderCards().size()-1)).getNumberOfRes());

    }

}