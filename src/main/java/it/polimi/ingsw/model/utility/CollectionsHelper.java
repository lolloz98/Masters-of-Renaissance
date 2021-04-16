package it.polimi.ingsw.model.utility;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CollectionsHelper {
    private CollectionsHelper(){}
    private static boolean isTest = false;
    private static int n = 0;
    public static void shuffle(List<?> l){
        if(!isTest) Collections.shuffle(l);
        else Collections.shuffle(l, new Random(n));
    }
    public static void setTest(){
        isTest = true;
    }
    public static boolean isTest(){
        return isTest;
    }
    public static void setSeedForTest(int seed){
        n = seed;
    }
}
