package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateGameAnswerHandlerTest {
    private LocalMulti multiPlayer;

    @Before
    public void setUp(){
        multiPlayer=new LocalMulti();
    }

    @Test
    public void HandleAnswerMultiTest(){

        int gameId=2,playerId=3;
        CreateGameAnswerHandler handler=new CreateGameAnswerHandler(new CreateGameAnswer(gameId,playerId,"tullio"));
        handler.handleAnswer(multiPlayer);

        assertEquals("tullio", multiPlayer.getMainPlayer().getName());
        assertEquals(playerId, multiPlayer.getMainPlayer().getId());
        assertEquals(LocalGameState.WAITINGPLAYERS,multiPlayer.getState() );
        assertEquals(1, multiPlayer.getLocalPlayers().size());
        assertEquals(gameId, multiPlayer.getGameId());


    }


}






