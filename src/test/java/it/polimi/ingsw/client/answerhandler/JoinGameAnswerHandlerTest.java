package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;

public class JoinGameAnswerHandlerTest extends TestCase {
    private LocalMulti multiPlayer;

    @Before
    public void setUp(){
        multiPlayer=new LocalMulti();
    }

    public void testHandleAnswer() {
        int gameId=2,creatorId=3;
        String creatorName="tullio";
        AnswerHandlerTestHelper.doCreateGameMulti(multiPlayer, gameId, creatorId, creatorName);

        JoinGameAnswer serverAnswer=new JoinGameAnswer(2, 4,
                new ArrayList<>(){{
            add(creatorId);
            add(4);
        }},
                new ArrayList<>(){{
            add(creatorName);
            add("daniele");
        }});

        for(int i=0;i<multiPlayer.getLocalPlayers().size();i++){
            assertEquals((int)serverAnswer.getPlayerIds().get(i),multiPlayer.getLocalPlayers().get(i).getId());
            assertEquals(serverAnswer.getPlayerNames().get(i),multiPlayer.getLocalPlayers().get(i).getName());
        }

        assertEquals(2, multiPlayer.getGameId());
        assertEquals(LocalGameState.WAITINGPLAYERS,multiPlayer.getState());
    }
}