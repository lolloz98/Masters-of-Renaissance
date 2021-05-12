package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LorenzoDeckTest {
    private LorenzoDeck deck;
    private ArrayList<LorenzoCard> cards;
    private final int N = 3;

    @Before
    public void setUp() throws EmptyDeckException {
        cards = new ArrayList<>();
        for(int i = 0; i < N; i++){
            cards.add(new LorenzoCard(i) {
                @Override
                public void applyEffect(SinglePlayer game) {
                    // do nothing
                }
            });
        }

        deck =  new LorenzoDeck(cards);
    }

    @Test
    public void testDrawCard() throws EmptyDeckException {
        ArrayList<LorenzoCard> c = new ArrayList<>();
        for (int i = 0; i < N; i++){
            c.add(deck.drawCard());
        }
        assertTrue(c.containsAll(cards));
        assertTrue(cards.containsAll(c));
    }

    @Test
    public void testBackToOriginalAndShuffle() throws EmptyDeckException {
        deck.backToOriginalAndShuffle();
        testDrawCard();

        deck.backToOriginalAndShuffle();
        deck.drawCard();
        ArrayList<LorenzoCard> c = new ArrayList<>();
        for (int i = 0; i < N - 1; i++){
            c.add(deck.drawCard());
        }
        assertTrue(cards.containsAll(c));

        try{
            deck.drawCard();
            fail();
        }
        catch(EmptyDeckException ignore){}
    }
}