package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.game.MultiPlayer;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest {
    private Board board;
    private ArrayList<DevelopCard> developCards;
    private MultiPlayer multiPlayer;

    @Before
    public void setUp() throws Exception{
        board= new Board();
        multiPlayer=new MultiPlayer(new ArrayList<>(){{
            add(new Player("first", 1));
            add(new Player("second", 2));
            add(new Player("third", 3));
            add(new Player("fourth", 4));
        }});

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        developCards = new ArrayList<>();
        String path;
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 1);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 3);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 4);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 17);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 24);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 27);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 37);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 46);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 48);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
    }

    @Test
    public void getVictoryPointsTest(){
        board.buyDevelopCard(multiPlayer, Color.BLUE,1,0);
    }


}