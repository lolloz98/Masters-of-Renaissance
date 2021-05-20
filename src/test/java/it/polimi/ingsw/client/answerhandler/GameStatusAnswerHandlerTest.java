package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GameStatusAnswerHandlerTest  {
    private LocalMulti multiplayer;
    private LocalSingle singleplayer;
    LocalSingle answerGameSingle;

    @Before
    public void setUp() throws Exception {
        multiplayer = new LocalMulti();
        singleplayer= new LocalSingle();
        answerGameSingle =new LocalSingle(0,new LocalDevelopmentGrid(),new LocalMarket(),new LocalTurnSingle(),LocalGameState.PREP_LEADERS,new LocalTrack(),new LocalPlayer(2, "aniello", new LocalBoard()));
    }

    @Test
    public void testHandleAnswerSingle(){

        GameStatusAnswer serverAnswer=new GameStatusAnswer(0, 1, 1, answerGameSingle);
        GameStatusAnswerHandler handler=new GameStatusAnswerHandler(serverAnswer);

        handler.handleAnswer(singleplayer);

        assertEquals(LocalGameState.PREP_LEADERS,singleplayer.getState());
        assertEquals(1,singleplayer.getLocalPlayers().size());
        assertFalse(singleplayer.getLocalTurn().isMarketActivated());
        assertFalse(singleplayer.getLocalTurn().isMainActionOccurred());
        assertFalse(singleplayer.getLocalTurn().isProductionsActivated());
        assertEquals(0,singleplayer.getLorenzoTrack().getFaithTrackScore() );
        assertEquals(0,singleplayer.getGameId() );
        assertEquals(serverAnswer.getGame().getLocalMarket(),singleplayer.getLocalMarket() );
        assertEquals(serverAnswer.getGame().getLocalDevelopmentGrid(), singleplayer.getLocalDevelopmentGrid());
    }

    @Test
    public void testHandleAnswerMulti(){
        //todo
    }

}