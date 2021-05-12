package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DeckTest {
    Deck<Card> deck;
    ArrayList<Card> cards;
    final int NUMBER = 15;

    @Before
    public void setUp(){
        cards = new ArrayList<>();
        for(int i = 0; i < NUMBER; i++){
            cards.add(() -> 0);
        }
        deck = new Deck<>(cards);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(deck.isEmpty());
    }

    @Test
    public void testDrawCard() throws EmptyDeckException {
        int i = 0;
        while(!deck.isEmpty()){
            Assert.assertTrue(cards.contains(deck.drawCard()));
            i++;
        }
        Assert.assertEquals(NUMBER, i);
    }

    @Test(expected = EmptyDeckException.class)
    public void testDrawCardException() throws EmptyDeckException {
        testDrawCard();
        deck.drawCard();
    }

    @Test
    public void testTopCard() throws EmptyDeckException {
        Assert.assertTrue(cards.contains(deck.topCard()));
        Assert.assertEquals(deck.topCard(), deck.topCard());
    }

    @Test
    public void testShuffle() throws EmptyDeckException {
        deck.shuffle();
        testDrawCard();
    }

    @Test
    public void testDistributeCards() throws EmptyDeckException {
        int i1 = NUMBER / 2, i2 = NUMBER / 3, i3 = NUMBER - (i1 + i2);
        ArrayList<Card> tmp;

        tmp = deck.distributeCards(i1);
        Assert.assertTrue(cards.containsAll(tmp));
        Assert.assertEquals(tmp.size(), i1);

        tmp = deck.distributeCards(i2);
        Assert.assertTrue(cards.containsAll(tmp));
        Assert.assertEquals(tmp.size(), i2);

        tmp = deck.distributeCards(i3);
        Assert.assertTrue(cards.containsAll(tmp));
        Assert.assertEquals(tmp.size(), i3);

        Assert.assertTrue(deck.isEmpty());
    }
}