package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.answers.mainactionsanswer.UseMarketAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class UseMarketAnswerHandlerTest {
    private LocalMulti localMulti;
    private LocalSingle localSingle;
    private LocalMarket localMarket;
    private ArrayList<TreeMap<Resource,Integer>> resCombinations;

    @Before
    public void setUp(){
        try {
            localSingle= AnswerHandlerTestHelper.getGameInReadyState();
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }

        try {
            localMulti=AnswerHandlerTestHelper.getGameInReadyState(1);
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }

        localMarket=AnswerHandlerTestHelper.getALocalMarket();

        resCombinations=AnswerHandlerTestHelper.getAResCombinations();

    }

    @Test
    public void testHandleAnswerSingle(){
        UseMarketAnswer serverAnswer=new UseMarketAnswer(0,1,resCombinations,localMarket);

        UseMarketAnswerHandler handler=new UseMarketAnswerHandler(serverAnswer);
        handler.handleAnswer(localSingle);

        assertEquals(resCombinations,localSingle.getLocalMarket().getResCombinations());
        assertTrue(localMarket.getMarbleMatrix().equals(localSingle.getLocalMarket().getMarbleMatrix()));
        assertEquals(localMarket.getFreeMarble(),localSingle.getLocalMarket().getFreeMarble());
        assertTrue(localSingle.getLocalTurn().isMainActionOccurred());
        assertTrue(localSingle.getLocalTurn().isMarketActivated());
        assertFalse(localSingle.getLocalTurn().isProductionsActivated());
    }

    @Test
    public void testHandleAnswerMulti(){
        UseMarketAnswer serverAnswer=new UseMarketAnswer(0,2,resCombinations,localMarket);

        UseMarketAnswerHandler handler=new UseMarketAnswerHandler(serverAnswer);
        handler.handleAnswer(localMulti);

        assertEquals(resCombinations,localMulti.getLocalMarket().getResCombinations());
        assertTrue(localMarket.getMarbleMatrix().equals(localMulti.getLocalMarket().getMarbleMatrix()));
        assertEquals(localMarket.getFreeMarble(),localMulti.getLocalMarket().getFreeMarble());
        assertTrue(localMulti.getLocalTurn().isMainActionOccurred());
        assertTrue(localMulti.getLocalTurn().isMarketActivated());
        assertFalse(localMulti.getLocalTurn().isProductionsActivated());
    }

}