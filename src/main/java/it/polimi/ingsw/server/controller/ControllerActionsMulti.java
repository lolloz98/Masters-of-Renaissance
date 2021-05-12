package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.model.exception.EndAlreadyReachedException;
import it.polimi.ingsw.server.model.exception.FigureAlreadyActivatedException;
import it.polimi.ingsw.server.model.exception.FigureAlreadyDiscardedException;
import it.polimi.ingsw.server.model.exception.InvalidStepsException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
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

    public synchronized void setGame(MultiPlayer game){
        this.game = game;
        distributeBeginningRes();
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
