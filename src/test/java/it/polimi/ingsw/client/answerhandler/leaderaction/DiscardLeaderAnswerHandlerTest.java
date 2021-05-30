package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandlerTestHelper;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalConcealedCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.ModelException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DiscardLeaderAnswerHandlerTest {
    public LocalMulti localMulti;

    @Before
    public void setUp(){

        try{
            localMulti= AnswerHandlerTestHelper.getGameInReadyState(1);
        }catch(ModelException | UnexpectedControllerException e){
            fail();
        }
    }

    @Test
    //test on the main player
    public void testHandleAnswer1(){
        LocalCard toDiscard=localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(0);
        ArrayList<LocalTrack> tracks=AnswerHandlerTestHelper.getLocalTracks(localMulti);
        ArrayList<LocalCard> originalCards=localMulti.getMainPlayer().getLocalBoard().getLeaderCards();
        DiscardLeaderAnswer serverAnswer= new DiscardLeaderAnswer(0,1,toDiscard,tracks,null);
        DiscardLeaderAnswerHandler handler=new DiscardLeaderAnswerHandler(serverAnswer);
        handler.handleAnswer(localMulti);

        LocalBoard localBoard=localMulti.getMainPlayer().getLocalBoard();
        //the leader card is discarded
        assertTrue(((LocalLeaderCard)localBoard.getLeaderCards().get(0)).isDiscarded());
        //the other leader cards are not modified
        for(int i=1;i<localBoard.getLeaderCards().size();i++){
            assertTrue(localBoard.getLeaderCards().get(i).equals(originalCards.get(i)));
            assertTrue(localBoard.getLeaderCards().get(i).getId()==originalCards.get(i).getId());
            assertEquals(((LocalLeaderCard)localBoard.getLeaderCards().get(i)).isDiscarded(),((LocalLeaderCard)originalCards.get(i)).isDiscarded());
        }
        //assertion on the tracks
        for(int i=0;i<localMulti.getLocalPlayers().size();i++) {
            assertEquals(tracks.get(i).getFaithTrackScore(), localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFaithTrackScore());
            assertTrue(tracks.get(i).getFiguresState().equals(localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFiguresState()));
        }
    }

    @Test
    //test not on the main player
    public void testHandleAnswer2(){
        LocalCard toDiscard=localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(0);//not important what card is passed in this case
        ArrayList<LocalTrack> tracks=AnswerHandlerTestHelper.getLocalTracks(localMulti);
        ArrayList<LocalCard> originalCards=localMulti.getPlayerById(2).getLocalBoard().getLeaderCards();
        DiscardLeaderAnswer serverAnswer= new DiscardLeaderAnswer(0,2,toDiscard,tracks,null);
        DiscardLeaderAnswerHandler handler=new DiscardLeaderAnswerHandler(serverAnswer);
        handler.handleAnswer(localMulti);

        LocalBoard localBoard=localMulti.getPlayerById(2).getLocalBoard();
        //the leader card is discarded
        assertTrue(((LocalConcealedCard)localBoard.getLeaderCards().get(0)).isDiscarded());
        //the other leader cards are not modified
        for(int i=1;i<localBoard.getLeaderCards().size();i++){
            assertEquals(localBoard.getLeaderCards().get(i), originalCards.get(i));
            assertEquals(localBoard.getLeaderCards().get(i).getId(), originalCards.get(i).getId());
            assertEquals(((LocalConcealedCard)localBoard.getLeaderCards().get(i)).isDiscarded(),((LocalConcealedCard)originalCards.get(i)).isDiscarded());
        }
        //assertion on the tracks
        for(int i=0;i<localMulti.getLocalPlayers().size();i++) {
            assertEquals(tracks.get(i).getFaithTrackScore(), localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFaithTrackScore());
            assertTrue(tracks.get(i).getFiguresState().equals(localMulti.getLocalPlayers().get(i).getLocalBoard().getLocalTrack().getFiguresState()));
        }

    }

}