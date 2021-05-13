package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnMultiAnswer;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.controller.states.WaitingForPlayersState;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.cards.leader.Requirement;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.TurnMulti;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.messages.requests.BeginningResourceDistributionMessage;
import it.polimi.ingsw.server.model.utility.PairId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

public class ControllerActionsMulti extends ControllerActions<MultiPlayer> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsMulti.class);

    private final PairId<Integer, ArrayList<Player>> numberAndPlayers;

    public ControllerActionsMulti(int id, AnswerListener answerListener, int numberOfPlayers, Player player) {
        super(null, id, answerListener);
        this.numberAndPlayers = new PairId<>(numberOfPlayers, new ArrayList<>(){{
            add(player);
        }});
        setGameState(new WaitingForPlayersState());
    }

    /**
     * method that changes the state of the game: from waitingState to prepareGameState
     * and prepares the game to be played
     */
    public synchronized void toPrepareGameState() throws UnexpectedControllerException {
        try {
            game.distributeLeader();
            distributeBeginningRes();
        } catch (EmptyDeckException e) {
            throw new UnexpectedControllerException("The deck of leader is empty before having distributed the cards to the players");
        }
        setGameState(new PrepareGameState());
    }

    @Override
    public synchronized boolean checkToGamePlayState() {
        for(Player p: game.getPlayers()){
            Board board=p.getBoard();
            if(!(board.getInitialRes()==0) || board.getLeaderCards().size()!=2)
                return false;
        }
        return true;
    }

    @Override
    public synchronized ArrayList<LocalTrack> getFaithTracks() throws ControllerException {
        ArrayList<LocalTrack> localTracks=new ArrayList<>();

        for(Player p:game.getPlayers()){
            LocalTrack localTrack=ConverterToLocalModel.convert(p.getBoard().getFaithtrack());
            localTracks.add(localTrack);
        }
        return localTracks;
    }

    /**
     * removes the effect of all the leader cards of the current player
     * @throws ControllerException
     */
    @Override
    public synchronized void removeLeadersEffect() throws ControllerException {
        for(LeaderCard<? extends Requirement> lc: game.getTurn().getCurrentPlayer().getBoard().getLeaderCards()){
            try {
                lc.removeEffect(game);
            } catch (NoSuchResourceException e) {
                throw new UnexpectedControllerException(e.getMessage());
            }
        }
    }

    @Override
    public synchronized void applyLeadersEffect() throws ControllerException {
        for(LeaderCard<? extends Requirement> lc: game.getTurn().getCurrentPlayer().getBoard().getLeaderCards()){

            try {
                lc.applyEffect(game);
            } catch (AlreadyAppliedLeaderCardException | AlreadyPresentLeaderResException | AlreadyAppliedDiscountForResException | TooManyLeaderResourcesException e) {
                throw new UnexpectedControllerException(e.getMessage());
            }
        }
    }



    public synchronized void setGame(MultiPlayer game) throws UnexpectedControllerException {
        this.game = game;
        toPrepareGameState();
    }

    public PairId<Integer, ArrayList<Player>> getNumberAndPlayers() {
        return numberAndPlayers;
    }

    /**
     * method that distributes to the players the resources at the beginning of the match
     */
    private synchronized void distributeBeginningRes() {
        if(game == null) logger.error("in action controller the game is null, but trying to distribute res");

        ArrayList<Player> players = game.getPlayers();
        int numberOfPlayers = players.size();
        // the first player (number 0), does not get any resources
        for (int i = 1; i < numberOfPlayers; i++) {
            Board b = players.get(i).getBoard();
            switch (i) {
                case 1:
                    b.setInitialRes(1);
                    break;
                case 2:
                    b.setInitialRes(1);
                    try {
                        b.moveOnFaithPath(1, game);
                    } catch (InvalidStepsException | FigureAlreadyActivatedException | FigureAlreadyDiscardedException | EndAlreadyReachedException e) {
                        logger.error("something unexpected happened in distributeBeginningRes");
                    }
                    break;
                case 3:
                    b.setInitialRes(2);
                    try {
                        b.moveOnFaithPath(1, game);
                    } catch (InvalidStepsException | EndAlreadyReachedException | FigureAlreadyDiscardedException | FigureAlreadyActivatedException e) {
                        logger.error("something unexpected happened in distributeBeginningRes");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
