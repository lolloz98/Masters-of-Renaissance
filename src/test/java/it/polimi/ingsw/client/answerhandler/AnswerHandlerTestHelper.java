package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import org.junit.Ignore;

/**
 * helper class to test the answer handlers
 */
@Ignore
public class AnswerHandlerTestHelper {

    /**
     *
     * @param multiPlayer
     */
    public static void doCreateGameMulti(LocalMulti multiPlayer,int gameId, int playerId,String playerName){

        CreateGameAnswerHandler handler=new CreateGameAnswerHandler(new CreateGameAnswer(gameId,playerId,playerName));
        handler.handleAnswer(multiPlayer);
    }

}
