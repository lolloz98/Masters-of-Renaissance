package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.cards.leader.Requirement;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.PairId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ControllerActionsServerMulti extends ControllerActionsServer<MultiPlayer> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsServerMulti.class);

    private final PairId<Integer, ArrayList<Player>> numberAndPlayers;

    public ControllerActionsServerMulti(int id, AnswerListener answerListener, int numberOfPlayers, Player player) {
        super(null, id, answerListener);
        this.numberAndPlayers = new PairId<>(numberOfPlayers, new ArrayList<>() {{
            add(player);
        }});
        setGameState(State.WAITING_FOR_PLAYERS);
    }

    /**
     * @return the correct status of the game (it calculates it lookig at the game)
     */
    private State calculateGameState(){
        for (Player i : game.getPlayers()) {
            if (i.getBoard().getLeaderCards().size() != 2) return State.PREPARATION;
        }
        for (Player i : game.getPlayers()) {
            if (i.getBoard().getInitialRes() != 0) return State.PREPARATION;
        }

        if (game.isGameOver()) return State.OVER;
        return State.PLAY;
    }

    /**
     * useful for preparing for the rejoining
     */
    public ControllerActionsServerMulti(MultiPlayer game, int id) {
        super(game, id);
        this.numberAndPlayers = new PairId<>(game.getPlayers().size(), game.getPlayers());
        setGameState(calculateGameState());
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
        setGameState(State.PREPARATION);
    }

    @Override
    public synchronized boolean checkToGamePlayState() {
        for(Player p: game.getPlayers()){
            Board board=p.getBoard();
            if(!(board.getInitialRes()==0) || board.getLeaderCards().size()!=2)
                return false;
        }
        logger.debug("switching to status: gamePlay");
        return true;
    }

    /**
     * removes the effect of all the leader cards of the current player
     */
    @Override
    public synchronized void removeLeadersEffect() throws UnexpectedControllerException {
        for(LeaderCard<? extends Requirement> lc: game.getTurn().getCurrentPlayer().getBoard().getLeaderCards()){
            try {
                lc.removeEffect(game);
            } catch (NoSuchResourceException e) {
                throw new UnexpectedControllerException(e.getMessage());
            }
        }
    }

    @Override
    public synchronized void applyLeadersEffect() throws UnexpectedControllerException {
        for(LeaderCard<? extends Requirement> lc: game.getTurn().getCurrentPlayer().getBoard().getLeaderCards()){

            try {
                lc.applyEffect(game);
            } catch (AlreadyAppliedLeaderCardException | AlreadyPresentLeaderResException | AlreadyAppliedDiscountForResException | TooManyLeaderResourcesException e) {
                throw new UnexpectedControllerException(e.getMessage());
            }
        }
    }

    /**
     * @return the answer containing the player or the players who won the game
     * @throws UnexpectedControllerException if it's called when the game is not over yet
     */
    @Override
    public synchronized ArrayList<LocalPlayer> getWinners() throws UnexpectedControllerException {
        ArrayList<Player> winners;
        ArrayList<LocalPlayer> localWinners=new ArrayList<>();

        try {
            winners = game.getWinners();
        } catch (GameNotOverException e) {
            logger.error("calling getWinners while the game is not over");
            throw new UnexpectedControllerException("The game is not over: you cannot know the winner!");
        }
        for (Player winner : winners) {
            LocalPlayer localWinner = ConverterToLocalModel.convert(winner, winner.getPlayerId());
            localWinners.add(localWinner);
        }

        return localWinners;
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
                        b.getFaithtrack().move(1, game);
                    } catch (InvalidStepsException | EndAlreadyReachedException e) {
                        logger.error("something unexpected happened in distributeBeginningRes");
                    }
                    break;
                case 3:
                    b.setInitialRes(2);
                    try {
                        b.getFaithtrack().move(1, game);
                    } catch (InvalidStepsException | EndAlreadyReachedException e) {
                        logger.error("something unexpected happened in distributeBeginningRes");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
