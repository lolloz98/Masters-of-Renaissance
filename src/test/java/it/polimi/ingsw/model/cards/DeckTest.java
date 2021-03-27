package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.EmptyDeckException;
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
        System.out.println("Deck preparation");
        cards = new ArrayList<>();
        for(int i = 0; i < NUMBER; i++){
            cards.add(new Card() {});
        }
        deck = new Deck<Card>(cards);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(deck.isEmpty());
    }

    @Test
    public void testDrawCard() {
        int i = 0;
        while(!deck.isEmpty()){
            Assert.assertTrue(cards.contains(deck.drawCard()));
            i++;
        }
        Assert.assertEquals(NUMBER, i);
    }

    @Test(expected = EmptyDeckException.class)
    public void testDrawCardException() {
        testDrawCard();
        deck.drawCard();
    }

    @Test
    public void testTopCard() {
        Assert.assertTrue(cards.contains(deck.topCard()));
        Assert.assertEquals(deck.topCard(), deck.topCard());
    }

    @Test
    public void testShuffle() {
        deck.shuffle();
        testDrawCard();
    }

    @Test
    public void testDistributeCards() {
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