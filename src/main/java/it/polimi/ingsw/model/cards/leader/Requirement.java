package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;

public interface Requirement {
    boolean checkRequirement(Player player);
}
