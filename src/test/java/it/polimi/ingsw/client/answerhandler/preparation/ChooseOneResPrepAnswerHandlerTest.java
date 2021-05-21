package it.polimi.ingsw.client.answerhandler.preparation;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChooseOneResPrepAnswerHandlerTest {
    private LocalMulti localMulti;

    @Before
    public void setUp(){
        try {
            localMulti= AnswerHandlerTestHelper.doRemoveLeadersPrepActionOnMulti(1);
        } catch (ModelException e) {
            fail();
        } catch (UnexpectedControllerException e) {
            fail();
        }
    }

    @Test
    public void testHandleAnswer(){
        ChooseOneResPrepAnswer serverAnswer=new ChooseOneResPrepAnswer(0, 1, Resource.GOLD, LocalGameState.PREP_RESOURCES);
        ChooseOneResPrepAnswerHandler handler=new ChooseOneResPrepAnswerHandler(serverAnswer);

        handler.handleAnswer(localMulti);

        assertEquals(new TreeMap<>(){{
            put(Resource.GOLD,1);
        }},localMulti.getPlayerById(1).getLocalBoard().getResInNormalDepot());

        for(LocalPlayer player:localMulti.getLocalPlayers()){
            if(player.getId()!=1)
                assertEquals(new TreeMap<>(), player.getLocalBoard().getResInNormalDepot());
        }
    }

}