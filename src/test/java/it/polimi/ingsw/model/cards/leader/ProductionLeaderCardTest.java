package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.Production;
import it.polimi.ingsw.model.game.*;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

public class ProductionLeaderCardTest {
    ProductionLeaderCard leaderCard;
    Production production;

    @Before
    public void setUp() throws Exception {
        TreeMap<Resource, Integer> toGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};


        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        production = new Production(toGive, toGain);

        leaderCard = new ProductionLeaderCard(2, new RequirementLevelDevelop(Color.BLUE), production, 0);
    }

    @Test
    public void testApplyEffect() {
        // todo: after game implementation
    }

    @Test
    public void testApplyEffectNoCheckOnActive() {
        // todo: after game implementation
    }

    @Test
    public void testRemoveEffect() {
        // todo: after game implementation
    }

    @Test
    public void testGetProduction() {
        // todo: after game implementation
    }
}