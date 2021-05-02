package it.polimi.ingsw.server.model.utility;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CollectionsHelperTest {
    ArrayList<Object> original;
    ArrayList<Object> toShuffle;
    ArrayList<Object> shuffled;

    @Before
    public void setup(){
        CollectionsHelper.setTest();
        init();
    }

    private void init(){
        original = new ArrayList<>(){
            {
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
                add(new Object());
            }
        };
    }

    @Test
    public void shuffle() {
        assertTrue(CollectionsHelper.isTest());
        toShuffle = new ArrayList<>(original);
        CollectionsHelper.shuffle(toShuffle);
        shuffled = new ArrayList<>(toShuffle);
        for(int i = 0; i < 100; i++){
            // always shuffles toShuffle in the same way
            toShuffle = new ArrayList<>(original);
            CollectionsHelper.shuffle(toShuffle);
            assertEquals(shuffled, toShuffle);
        }
    }
}