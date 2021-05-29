package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushProductionResAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FlushProductionResAnswerHandlerTest {
    private LocalMulti localMulti;

    @Before
    public void setUp(){
        try {
            localMulti= AnswerHandlerTestHelper.getGameInReadyState(1);
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }

    }

    @Test
    public void testHandleAnswer(){
        ArrayList<LocalTrack>localTracks=AnswerHandlerTestHelper.getLocalTracks(localMulti);
        FlushProductionResAnswer serverAnswer=new FlushProductionResAnswer(0,1,AnswerHandlerTestHelper.getResInStrongbox(),localTracks);
        FlushProductionResAnswerHandler handler=new FlushProductionResAnswerHandler(serverAnswer);
        handler.handleAnswer(localMulti);

        assertFalse(localMulti.getLocalTurn().isProductionsActivated());

        LocalBoard localBoard=localMulti.getMainPlayer().getLocalBoard();

        //assertions on the player track
        for(int i=0;i<localMulti.getLocalPlayers().size();i++) {
            assertEquals(localTracks.get(i).getFaithTrackScore(), localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFaithTrackScore());
            assertTrue(localTracks.get(i).getFiguresState().equals(localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFiguresState()));
        }

        //assertion on the strongbox
        assertEquals(AnswerHandlerTestHelper.getResInStrongbox(),localBoard.getResInStrongBox());

        //assertion on the history
        assertEquals("You used a development",localMulti.getLocalTurn().getHistoryObservable().getHistory().get(localMulti.getLocalTurn().getHistoryObservable().getHistory().size()-1));
    }
}