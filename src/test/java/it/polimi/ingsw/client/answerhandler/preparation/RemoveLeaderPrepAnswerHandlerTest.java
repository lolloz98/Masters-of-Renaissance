package it.polimi.ingsw.client.answerhandler.preparation;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class RemoveLeaderPrepAnswerHandlerTest extends TestCase {
    private LocalSingle localSingle;
    private LocalMulti localMulti;

    @Before
    public void setUp(){
        CollectionsHelper.setTest();

        try {
            localMulti= AnswerHandlerTestHelper.getLocalMulti(2);
            //the main player has id2 and has the following leaderIds: 55,54,59,53
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }

        try{
            localSingle=AnswerHandlerTestHelper.getLocalSingle();
            //the main player has the following leaderIds:49,56,61,58
        }catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }
    }

    @Test
    public void testHandleAnswerSingle() {
        ArrayList<LocalCard> toDiscard=new ArrayList<>();

        RemoveLeaderPrepAnswer serverAnswer=new RemoveLeaderPrepAnswer(0, 1, new ArrayList<>(){{ add(61);add(58);}}, LocalGameState.PREP_RESOURCES);

        for (LocalCard card : localSingle.getMainPlayer().getLocalBoard().getLeaderCards()) {
            if (serverAnswer.getRemovedLeaderIds().contains(card.getId())) {
                toDiscard.add(card);
            }
        }

        RemoveLeaderPrepAnswerHandler handler=new RemoveLeaderPrepAnswerHandler(serverAnswer);

        handler.handleAnswer(localSingle);

        LocalPlayer mainPlayer=localSingle.getMainPlayer();

        assertEquals(2,localSingle.getMainPlayer().getLocalBoard().getLeaderCards().size());

        for(LocalCard discarded:toDiscard){
            assertFalse(mainPlayer.getLocalBoard().getLeaderCards().contains(discarded));
        }

    }

    @Test
    public void testHandleAnswerMulti(){

        ArrayList<LocalCard> toDiscard=new ArrayList<>();

        RemoveLeaderPrepAnswer serverAnswer=new RemoveLeaderPrepAnswer(0, 2, new ArrayList<>(){{ add(55);add(59);}}, LocalGameState.PREP_LEADERS);

        for (LocalCard card : localMulti.getMainPlayer().getLocalBoard().getLeaderCards()) {
            if (serverAnswer.getRemovedLeaderIds().contains(card.getId())) {
                toDiscard.add(card);
            }
        }

        RemoveLeaderPrepAnswerHandler handler=new RemoveLeaderPrepAnswerHandler(serverAnswer);

        handler.handleAnswer(localMulti);

        LocalPlayer mainPlayer=localMulti.getMainPlayer();

        assertEquals(2,localMulti.getMainPlayer().getLocalBoard().getLeaderCards().size());

        for(LocalCard discarded:toDiscard){
            assertFalse(mainPlayer.getLocalBoard().getLeaderCards().contains(discarded));
        }

        serverAnswer=new RemoveLeaderPrepAnswer(0, 1, new ArrayList<>(){{ add(55);add(59);}}, LocalGameState.PREP_LEADERS);

        handler=new RemoveLeaderPrepAnswerHandler(serverAnswer);

        handler.handleAnswer(localMulti);

        assertEquals(2,localMulti.getPlayerById(1).getLocalBoard().getLeaderCards().size());
    }
}