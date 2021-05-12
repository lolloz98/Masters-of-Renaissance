package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnTest {
    private Turn turn;

    @Before
    public void setUp() {
        turn = new Turn() {
            @Override
            public Turn nextTurn(Game<? extends Turn> game) {
                return null;
            }
        };
    }

    @Test
    public void setMarketActivated()throws ModelException {
        // nothing should happen
        turn.setMarketActivated(false);
        try {
            turn.checkConditions();
        }catch(MainActionNotOccurredException ignore){}

        turn.setMarketActivated(true);
        try {
            turn.checkConditions();
        }catch(MarketTrayNotEmptyException ignore){}

        try {
            turn.setProductionsActivated(true);
        }catch(MainActionAlreadyOccurredException ignore){}
        try {
            turn.setMarketActivated(true);
        }catch(MainActionAlreadyOccurredException ignore){}

        assertTrue(turn.isMarketActivated());
        assertFalse(turn.isMainActionOccurred());
        assertFalse(turn.isProductionsActivated());

        turn.setMarketActivated(false);

        assertFalse(turn.isMarketActivated());
        assertTrue(turn.isMainActionOccurred());
        assertFalse(turn.isProductionsActivated());

        turn.checkConditions();
    }

    @Test
    public void setProductionsActivated() throws ModelException{
        turn.setProductionsActivated(false);
        try {
            turn.checkConditions();
        }catch(MainActionNotOccurredException ignore){}

        turn.setProductionsActivated(true);
        try {
            turn.checkConditions();
        }catch(ProductionsResourcesNotFlushedException ignore){}

        try {
            turn.setMarketActivated(true);
        }catch(MainActionAlreadyOccurredException ignore){}

        assertFalse(turn.isMarketActivated());
        assertFalse(turn.isMainActionOccurred());
        assertTrue(turn.isProductionsActivated());

        turn.setProductionsActivated(true);

        turn.setProductionsActivated(false);

        assertFalse(turn.isMarketActivated());
        assertTrue(turn.isMainActionOccurred());
        assertFalse(turn.isProductionsActivated());

        turn.checkConditions();
    }

    @Test
    public void setMainActionOccurred()throws ModelException {
        turn.setMainActionOccurred();
        turn.checkConditions();
        assertTrue(turn.isMainActionOccurred());
    }
}