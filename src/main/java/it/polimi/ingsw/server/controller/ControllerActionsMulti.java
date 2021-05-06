package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.messages.requests.BeginningResourceDistributionMessage;

import java.util.ArrayList;
import java.util.TreeMap;

public class ControllerActionsMulti extends ControllerActions<MultiPlayer> {

    public ControllerActionsMulti(MultiPlayer game, int id) {
        super(game, id);
        distributeBeginningRes();
    }

    /**
     * method that distributes to the players the resources at the beginning of the match
     */
    private synchronized void distributeBeginningRes() {
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
                    b.moveOnFaithPath(1, game);
                    break;
                case 3:
                    b.setInitialRes(2);
                    b.moveOnFaithPath(1, game);
                    break;
                default:
                    break;
            }
        }
    }
}
