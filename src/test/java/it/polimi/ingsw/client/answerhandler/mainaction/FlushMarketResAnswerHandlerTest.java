package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

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

    @Test
    public void testHandleAnswerSingle(){
        //add a depot leader in the main player board
        int leaderId=0;
        localSingle.getMainPlayer().getLocalBoard().getLeaderCards().add(new LocalDepotLeader(leaderId,1,true,false,Resource.GOLD,Resource.GOLD,5));

        AnswerHandlerTestHelper.doUseMarketAction(localSingle);
        ArrayList<LocalTrack> localTracks=AnswerHandlerTestHelper.getLocalTracks(localSingle);

        TreeMap<Resource,Integer> resInNormalDeposit=AnswerHandlerTestHelper.getResInDepot();

        ArrayList<LocalDepotLeader> localDepotLeaders=new ArrayList<>(){{
            add(new LocalDepotLeader(leaderId,3,true,false,Resource.GOLD,Resource.SHIELD,3));
        }};
        localDepotLeaders.get(0).setNumberOfRes(2);

        LocalTrack lorenzoTrack=AnswerHandlerTestHelper.getARandomTrack();

        FlushMarketResAnswer serverAnswer=new FlushMarketResAnswer(0,1,localTracks,resInNormalDeposit,localDepotLeaders,lorenzoTrack);

        FlushMarketResAnswerHandler handler=new FlushMarketResAnswerHandler(serverAnswer);

        handler.handleAnswer(localSingle);

        //assertions on the player track
        assertEquals(localTracks.get(0).getFaithTrackScore(),localSingle.getMainPlayer().getLocalBoard().getLocalTrack().getFaithTrackScore());
        assertTrue(localTracks.get(0).getFiguresState().equals(localSingle.getMainPlayer().getLocalBoard().getLocalTrack().getFiguresState()));

        //assertion on lorenzo track
        assertEquals(lorenzoTrack.getFaithTrackScore(),localSingle.getLorenzoTrack().getFaithTrackScore());
        assertTrue(lorenzoTrack.getFiguresState().equals(localSingle.getLorenzoTrack().getFiguresState()));

        //assertion on player depot
        assertEquals(resInNormalDeposit,localSingle.getMainPlayer().getLocalBoard().getResInNormalDepot());

        //assertion on leader depots
        LocalDepotLeader leader=null;
        for(int i=0;i<localSingle.getMainPlayer().getLocalBoard().getLeaderCards().size();i++){
            if(localSingle.getMainPlayer().getLocalBoard().getLeaderCards().get(i).getId()==leaderId) {
                leader = (LocalDepotLeader) localSingle.getMainPlayer().getLocalBoard().getLeaderCards().get(i);
                break;
            }
        }

        assertEquals(localDepotLeaders.get(0).getNumberOfRes(),leader.getNumberOfRes());
    }

    @Test
    public void testHandleAnswerMulti(){
        //add a depot leader in the main player board
        localMulti.getMainPlayer().getLocalBoard().getLeaderCards().add(new LocalDepotLeader(0,1,true,false,Resource.GOLD,Resource.GOLD,5));
        int leaderId=0;

        AnswerHandlerTestHelper.doUseMarketAction(localMulti);
        ArrayList<LocalTrack> localTracks=AnswerHandlerTestHelper.getLocalTracks(localMulti);

        TreeMap<Resource,Integer> resInNormalDeposit=new TreeMap<Resource, Integer>(){{
            put(Resource.SERVANT,1);
            put(Resource.SHIELD,1);
        }};

        ArrayList<LocalDepotLeader> localDepotLeaders=new ArrayList<>(){{
            add(new LocalDepotLeader(leaderId,3,true,false,Resource.GOLD,Resource.SHIELD,3));
        }};
        localDepotLeaders.get(0).setNumberOfRes(2);

        FlushMarketResAnswer serverAnswer=new FlushMarketResAnswer(0,1,localTracks,resInNormalDeposit,localDepotLeaders,null);

        FlushMarketResAnswerHandler handler=new FlushMarketResAnswerHandler(serverAnswer);

        handler.handleAnswer(localMulti);

        //assertions on the player track
        for(int i=0;i<localMulti.getLocalPlayers().size();i++) {
            assertEquals(localTracks.get(i).getFaithTrackScore(), localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFaithTrackScore());
            assertTrue(localTracks.get(i).getFiguresState().equals(localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFiguresState()));
        }

        //assertion on player depot
        assertEquals(resInNormalDeposit,localMulti.getMainPlayer().getLocalBoard().getResInNormalDepot());

        //assertion on leader depots
        LocalDepotLeader leader=null;
        for(int i=0;i<localMulti.getMainPlayer().getLocalBoard().getLeaderCards().size();i++){
            if(localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i).getId()==leaderId) {
                leader = (LocalDepotLeader) localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i);
                break;
            }
        }

        assertEquals(localDepotLeaders.get(0).getNumberOfRes(),leader.getNumberOfRes());

    }

}