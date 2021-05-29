package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.answers.mainactionsanswer.BuyDevelopCardAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BuyDevelopCardAnswerHandlerTest {
    private LocalMulti localMulti;

    @Before
    public void setUp(){

        try{
            localMulti=AnswerHandlerTestHelper.getGameInReadyState(1);
        }catch(ModelException|UnexpectedControllerException e){
            fail();
        }
    }

    @Test
    public void testHandleAnswer(){
        LocalDevelopmentGrid grid=new LocalDevelopmentGrid();
        LocalBoard localBoard= null;
        try {
            localBoard = AnswerHandlerTestHelper.getALocalBoard();
        } catch (ModelException e) {
            fail();
        }
        BuyDevelopCardAnswer serverAnswer=new BuyDevelopCardAnswer(0,1,localBoard,grid,0);
        BuyDevelopCardAnswerHandler handler=new BuyDevelopCardAnswerHandler(serverAnswer);
        handler.handleAnswer(localMulti);

        //assertion on grid
        assertEquals(grid,localMulti.getLocalDevelopmentGrid());
        //assertions on board
        assertEquals(localBoard.getDevelopCards(),localMulti.getMainPlayer().getLocalBoard().getDevelopCards());
        assertEquals(localBoard.getResInNormalDepot(),localMulti.getMainPlayer().getLocalBoard().getResInNormalDepot());
        //assertion on history
        assertEquals("You bought a development card",localMulti.getLocalTurn().getHistoryObservable().getHistory().get(localMulti.getLocalTurn().getHistoryObservable().getHistory().size()-1));
    }
}