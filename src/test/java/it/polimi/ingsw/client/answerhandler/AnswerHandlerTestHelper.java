package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.exceptions.LocalModelException;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Ignore;

import java.util.ArrayList;

/**
 * helper class to test the answer handlers
 */
@Ignore
public class AnswerHandlerTestHelper {

    /**
     *creates a single player
     * @param multiPlayer
     */
    public static void doCreateGameMulti(LocalMulti multiPlayer,int gameId, int playerId,String playerName){

        CreateGameAnswerHandler handler=new CreateGameAnswerHandler(new CreateGameAnswer(gameId,playerId,playerName));
        handler.handleAnswer(multiPlayer);
    }

    public static LocalSingle getLocalSingle() throws ModelException, UnexpectedControllerException {
        SinglePlayer singlePlayer;
        try{
            singlePlayer=new SinglePlayer(new Player("aniello", 1));
    }catch(IllegalArgumentException | EmptyDeckException | WrongColorDeckException | InvalidArgumentException  | WrongLevelDeckException e){
        throw new ModelException("something went wrong");
    }
        singlePlayer.distributeLeader();

        LocalSingle localSingle= ConverterToLocalModel.convert(singlePlayer, 1, 0);

        return localSingle;
    }

    /**
     * creates a 3 player multiplayer local game
     * @return
     * @throws UnexpectedControllerException
     * @throws InvalidArgumentException
     * @throws WrongLevelDeckException
     * @throws WrongColorDeckException
     * @throws EmptyDeckException
     * @throws PlayersOutOfBoundException
     */
    public static LocalMulti getLocalMulti(int playerIdRequiring) throws ModelException, UnexpectedControllerException {
        MultiPlayer multiPlayer;
        try{
            multiPlayer=new MultiPlayer(new ArrayList<>(){{
                add(new Player("aniello", 1));
                add(new Player("carpa", 2));
                add(new Player("inno", 3));
            }});
        }catch(IllegalArgumentException | EmptyDeckException | WrongColorDeckException | InvalidArgumentException | PlayersOutOfBoundException | WrongLevelDeckException e){
            throw new ModelException("something went wrong");
        }

        multiPlayer.distributeLeader();

        LocalMulti localMulti= null;

            localMulti = ConverterToLocalModel.convert(multiPlayer, playerIdRequiring, 0);



        return localMulti;
    }

}
