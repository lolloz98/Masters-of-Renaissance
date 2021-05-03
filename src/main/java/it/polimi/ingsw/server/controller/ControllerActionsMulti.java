package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.messages.requests.BeginningResourceDistributionMessage;

import java.util.ArrayList;
import java.util.TreeMap;

public class ControllerActionsMulti extends ControllerActions<MultiPlayer> {


    public ControllerActionsMulti(MultiPlayer game, int id) {
        super( game, id);
    }

    /**
     * method that distributes to the players the resources at the beginning of the match
     */
    @Override
    protected synchronized void distributeBeginningRes() {
        ArrayList<Player> players = game.getPlayers();
        int numberOfPlayers = players.size();
        for (int i = 1; i < numberOfPlayers; i++) {
            switch (i) {
                case 1:
                    BeginningResourceDistributionMessage message = new BeginningResourceDistributionMessage(new TreeMap<>() {{
                        put(Resource.ANYTHING, 1);
                    }}, players.get(i).getPlayerId());
                    //ClientHandler.sendMessage(message);
                case 2:
                    message = new BeginningResourceDistributionMessage(new TreeMap<>() {{
                        put(Resource.ANYTHING, 1);
                    }}, players.get(1).getPlayerId());
                    players.get(1).getBoard().moveOnFaithPath(1, game);
                    //ClientHandler.sendMessage(message);
                case 3:
                    message = new BeginningResourceDistributionMessage(new TreeMap<>() {{
                        put(Resource.ANYTHING, 2);
                    }}, players.get(1).getPlayerId());
                    players.get(1).getBoard().moveOnFaithPath(1, game);
                    //ClientHandler.sendMessage(message);
            }
        }
    }
}
