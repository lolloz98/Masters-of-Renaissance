package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReshuffleLorenzoCardTest {

    LorenzoCard card;
    SinglePlayer singlePlayer;
    Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player("single", 1);

        singlePlayer = new SinglePlayer(player);
    }

    @Test
    public void testApplyEffect() throws ModelException {
        card = new ReshuffleLorenzoCard(0);
        assertEquals(0, singlePlayer.getLorenzo().getFaithTrack().getPosition());
        card.applyEffect(singlePlayer);
        assertEquals(1, singlePlayer.getLorenzo().getFaithTrack().getPosition());
        card.applyEffect(singlePlayer);
        assertEquals(2, singlePlayer.getLorenzo().getFaithTrack().getPosition());
    }
}