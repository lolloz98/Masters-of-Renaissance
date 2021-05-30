package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateDiscountLeaderAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateProductionLeaderAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActivateDiscountLeaderAnswerHandlerTest {
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
        LocalDevelopmentGrid randomGrid=AnswerHandlerTestHelper.getADevelopGrid();
        LocalCard card=localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(0);
        LocalLeaderCard leaderCard=(LocalLeaderCard) card;
        leaderCard.setActive(true);

        ActivateDiscountLeaderAnswer serverAnswer=new ActivateDiscountLeaderAnswer(0,1,leaderCard,randomGrid);
        ActivateDiscountLeaderAnswerHandler handler=new ActivateDiscountLeaderAnswerHandler(serverAnswer);
        handler.handleAnswer(localMulti);

        assertEquals(randomGrid,localMulti.getLocalDevelopmentGrid());

        assertTrue(((LocalLeaderCard)localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(0)).isActive());

    }

}