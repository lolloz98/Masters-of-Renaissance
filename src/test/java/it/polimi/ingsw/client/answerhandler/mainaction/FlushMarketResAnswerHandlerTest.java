package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class FlushMarketResAnswerHandlerTest {
    private LocalSingle localSingle;
    private LocalMulti localMulti;

    @Before
    public void setUp(){
        try {
            localSingle= AnswerHandlerTestHelper.getGameInReadyState();
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }

        try{
            localMulti=AnswerHandlerTestHelper.getGameInReadyState(1);
        }catch(ModelException|UnexpectedControllerException e){
            fail();
        }
    }

    @Before
    public void testHandleAnswerSingle(){
        AnswerHandlerTestHelper.doUseMarketAction(localSingle);
        ArrayList<LocalTrack> localTracks=AnswerHandlerTestHelper.getLocalTracks(localSingle);

        //FlushMarketResAnswer serverAnswer=new FlushMarketResAnswer(0,1,localTracks,)
    }

    @Before
    public void testHandleAnswerMulti(){
        AnswerHandlerTestHelper.doUseMarketAction(localMulti);

        //FlushMarketResAnswer serverAnswer=new FlushMarketResAnswer()
    }

}