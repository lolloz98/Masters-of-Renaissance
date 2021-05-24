package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.FigureStateHelperTest;
import it.polimi.ingsw.client.answerhandler.mainaction.UseMarketAnswerHandler;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.UseMarketAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MarbleDispenserTester;
import it.polimi.ingsw.server.model.game.MarketTray;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

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
        CollectionsHelper.setTest();
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
        CollectionsHelper.setTest();
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

    /**
     *
     * @return a configuration of the local normal depots
     */
    public static TreeMap<Resource,Integer> getResInDepot(){
        return new TreeMap<Resource, Integer>(){{
            put(Resource.SERVANT,1);
            put(Resource.SHIELD,1);
        }};
    }

    /**
     *after this method the first player owns the following leaders: {LocalProductionLeader->id: 61};{LocalMarbleLeader->id:58}
     * the second player owns the following leaders:
     * the third player owns the following leaders:
     * @param whichPlayerId the id of the player requiring the localgame
     * @return the localGame of the first player with all the leaders discarded
     */
    public static LocalMulti doRemoveLeadersPrepActionOnMulti(int whichPlayerId) throws ModelException, UnexpectedControllerException {
        LocalMulti localMulti=getLocalMulti(whichPlayerId);


        for(int i=0;i<localMulti.getLocalPlayers().size();i++){
            localMulti.getLocalPlayers().get(i).getLocalBoard().getLeaderCards().remove(0);
            localMulti.getLocalPlayers().get(i).getLocalBoard().getLeaderCards().remove(0);
        }

        localMulti.setState(LocalGameState.PREP_RESOURCES);

        return localMulti;
    }

    /**
     * after this method the main player owns the following leaders: {LocalProductionLeader->id: 61};{LocalMarbleLeader->id:58}
     * @return
     * @throws ModelException
     * @throws UnexpectedControllerException
     */
    public static LocalSingle doRemoveLeadersActionOnSingle() throws ModelException, UnexpectedControllerException {
        LocalSingle localSingle=getLocalSingle();

        localSingle.getMainPlayer().getLocalBoard().getLeaderCards().remove(0);
        localSingle.getMainPlayer().getLocalBoard().getLeaderCards().remove(0);

        localSingle.setState(LocalGameState.PREP_RESOURCES);

        return localSingle;
    }

    /**
     *
     * @param playerRequiring
     * @return a multiplayer in the playing state
     */
    public static LocalMulti getGameInReadyState(int playerRequiring) throws ModelException, UnexpectedControllerException {
        LocalMulti localMulti=doRemoveLeadersPrepActionOnMulti(playerRequiring);

        localMulti.setState(LocalGameState.READY);

        return localMulti;
    }

    /**
     *
     * @return a singleplayer in the playing state
     */
    public static LocalSingle getGameInReadyState() throws ModelException, UnexpectedControllerException {
        LocalSingle localSingle=doRemoveLeadersActionOnSingle();

        localSingle.setState(LocalGameState.READY);

        return  localSingle;

    }

    /**
     * generates a determinate instance of the local market
     * @return
     */
    public static LocalMarket getALocalMarket(){
        MarketTray market=new MarketTray(new MarbleDispenserTester());
        LocalMarket localMarket=ConverterToLocalModel.convert(market);
        return localMarket;
    }

    /**
     *
     * @return a combination of market resources to flush
     */
    public static ArrayList<TreeMap<Resource,Integer>> getAResCombinations(){
        return new ArrayList<>(){{
            add(new TreeMap<>(){{
                put(Resource.GOLD,1);
                put(Resource.SHIELD,2);
            }});
            add(new TreeMap<>(){{
                put(Resource.SERVANT,3);
                put(Resource.FAITH,1);
            }});
        }};
    }

    /**
     * helper method for the FlushMarketResourcesAnswerhandler test
     * does the market action on the game passed
     * @param localGame
     */
    public static void doUseMarketAction(LocalGame<?> localGame){
        LocalMarket localMarket=getALocalMarket();
        ArrayList<TreeMap<Resource,Integer>> resCombinations=getAResCombinations();
        UseMarketAnswer serverAnswer=new UseMarketAnswer(0,1,resCombinations,localMarket);

        UseMarketAnswerHandler handler=new UseMarketAnswerHandler(serverAnswer);
        handler.handleAnswer(localGame);
    }


    /**
     *
     * @return a random instance of the tracks of the players in the game
     */
    public static ArrayList<LocalTrack> getLocalTracks(LocalGame<?> localGame){
        ArrayList<LocalTrack> tracks=new ArrayList<>();
        LocalTrack track;

        for(int i=0; i<localGame.getLocalPlayers().size();i++) {
            track = getARandomTrack();
            tracks.add(track);
        }

        return tracks;
    }

    /**
     *
     * @return a random track
     */
    public static LocalTrack getARandomTrack(){
        LocalTrack track;

        track=new LocalTrack();
        track.setFaithTrackScore(new Random().nextInt(24));
        for(int i=0;i<3;i++) {
            track.setFigureState(0, FigureStateHelperTest.randomFigureState());
        }

        return track;
    }
}
