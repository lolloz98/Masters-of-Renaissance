package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.answers.mainactionsanswer.UseMarketAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class UseMarketAnswerHandlerTest {
    private LocalMulti localMulti;
    private LocalSingle localSingle;
    private Resource[][] marbleMatrix;

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

        marbleMatrix = new Resource[][]{
                {Resource.FAITH, Resource.GOLD, Resource.FAITH, Resource.GOLD},
                {Resource.FAITH, Resource.GOLD, Resource.FAITH, Resource.GOLD},
                {Resource.FAITH, Resource.GOLD, Resource.FAITH, Resource.GOLD},
        };
    }

    @Test
    public void testHandleAnswerSingle(){

        //todo
        //UseMarketAnswer serverAnswer=new UseMarketAnswer(0,1,marbleMatrix,)
    }

}