package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

public class MultiPlayer extends Game{
    private ArrayList<Player> players;

    public MultiPlayer(ArrayList<Player> players){
        this.players = players;
    }
}
