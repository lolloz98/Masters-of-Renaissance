package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

public class SinglePlayer extends Game{
    private Player player;
    private Lorenzo lorenzo;

    public SinglePlayer(Player player) {
        this.player = player;
        this.lorenzo = new Lorenzo();
    }
}
