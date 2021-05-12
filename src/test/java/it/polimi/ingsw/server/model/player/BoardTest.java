package it.polimi.ingsw.server.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.cards.leader.ProductionLeaderCard;
import it.polimi.ingsw.server.model.cards.leader.Requirement;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class BoardTest {
    private ArrayList<DevelopCard> developCards;
    private ArrayList<LeaderCard<? extends Requirement>> depotLeaderCards;
    private ArrayList<LeaderCard<? extends Requirement>> productionLeaderCards;
    private MultiPlayer multiPlayer;
    private SinglePlayer singlePlayer;
    private Board board;

    @Before
    public void setUp() throws Exception {
        CollectionsHelper.setTest();

        singlePlayer = new SinglePlayer(new Player("first", 1));
        multiPlayer = new MultiPlayer(new ArrayList<>() {{
            add(new Player("first", 1));
            add(new Player("second", 2));
            add(new Player("third", 3));
            add(new Player("fourth", 4));
        }});
        board = new Board();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        developCards = new ArrayList<>();
        depotLeaderCards = new ArrayList<>();
        productionLeaderCards = new ArrayList<>();
        String path;

        //adding 2 depot leader slots
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        depotLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 55);//resource type: SHIELD
        depotLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));

        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot leader to board
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot leader to board
        assertEquals(2, board.getDepotLeaders().size());
        assertEquals(Resource.ROCK, board.getDepotLeaders().get(0).getDepot().getTypeOfResource());
        assertEquals(Resource.SHIELD, board.getDepotLeaders().get(1).getDepot().getTypeOfResource());

        //adding 2 production leader slots
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 62); //"resourcesToGive:"SERVANT": 1; "resourcesToGain": "FAITH": 1,,"ANYTHING": 1
        productionLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), ProductionLeaderCard.class));
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 63);//"resourcesToGive": "ROCK": 1; "resourcesToGain": "FAITH": 1,"ANYTHING": 1
        productionLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), ProductionLeaderCard.class));

        board.addLeaderCards(productionLeaderCards);
        board.addLeaderCards(depotLeaderCards);

        board.discoverProductionLeader((ProductionLeaderCard) productionLeaderCards.get(0));
        board.discoverProductionLeader((ProductionLeaderCard) productionLeaderCards.get(1));
        assertEquals(2, board.getProductionLeaders().size());
    }

    @Test
    public void discardLeaderCardTest() throws ModelException{
        DepotLeaderCard toDiscard= (DepotLeaderCard) depotLeaderCards.get(0);

        getVictoryPointsTest();//add and activate 2 depot leaders

        board.discardLeaderCard(toDiscard);

        assertFalse(board.getLeaderCards().contains(toDiscard));
        assertEquals(1,board.getLeaderCards().size());
        assertEquals(1,board.getDepotLeaders().size());
        assertTrue(toDiscard.isDiscarded());
        assertFalse(board.getDepotLeaders().contains(toDiscard));
    }

    public void buildBoard2() throws ModelException{

        fillStrongBox1();
        //adding the depot leader cards and activating it
        board.addLeaderCards(new ArrayList<>() {{
            add(depotLeaderCards.get(0));
            add(depotLeaderCards.get(1));
        }});

        board.getLeaderCards().get(0).activate(multiPlayer, multiPlayer.getPlayers().get(0));
        board.getLeaderCards().get(1).activate(multiPlayer, multiPlayer.getPlayers().get(0));

        //preparing the board
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 4);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SERVANT, 3);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 2);
                put(Resource.SHIELD, 2);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }
    }

    public void buildBoard1() throws ModelException {
        //preparing the board
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SHIELD, 3);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }

        fillStrongBox2();
    }

    @Test
    public void enoughResourcesToPay()  throws ModelException {
        buildBoard1();
        //building a treemap to pay
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        assertTrue(board.enoughResourcesToPay(toPay));

        toPay = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 4);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        assertTrue(board.enoughResourcesToPay(toPay));

        toPay = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 4);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
            }});
        }};

        assertTrue(board.enoughResourcesToPay(toPay));

        toPay = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 6);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
            }});
        }};

        assertFalse(board.enoughResourcesToPay(toPay));

        toPay = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 3);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
                put(Resource.GOLD, 3);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        assertFalse(board.enoughResourcesToPay(toPay));

        toPay = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 3);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
                put(Resource.ROCK, 3);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        assertFalse(board.enoughResourcesToPay(toPay));
    }

    @Test
    public void payResourcesTest() throws ModelException {
        buildBoard1();

        //first payment
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        board.payResources(toPay);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 3);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<Resource, Integer>(), board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 5);
        }}, board.getResourcesInStrongBox());

        //second payment
        toPay = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 1);
            }});
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 3);
            }});
        }};

        board.payResources(toPay);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<Resource, Integer>(), board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 2);
        }}, board.getResourcesInStrongBox());

        //third payment
        toPay = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.ROCK, 2);
                put(Resource.SHIELD, 2);
            }});
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
            }});
        }};

        board.payResources(toPay);

        assertEquals(new TreeMap<Resource, Integer>() {{
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<Resource, Integer>(), board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 2);
        }}, board.getResourcesInStrongBox());

        //fourth payment
        toPay = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
            }});
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};

        board.payResources(toPay);

        assertEquals(new TreeMap<Resource, Integer>() {{
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<Resource, Integer>(), board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
        }}, board.getResourcesInStrongBox());

    }

    public void fillStrongBox1() throws ModelException{
        board.flushGainedResources(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, multiPlayer);
    }

    public void fillStrongBox2() throws ModelException{
        TreeMap<Resource, Integer> toFlush = new TreeMap<>() {{
            put(Resource.GOLD, 5);
        }};

        board.flushGainedResources(toFlush, multiPlayer);
    }

    public void fillStrongBox3() throws ModelException{
        board.flushGainedResources(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 5);
            put(Resource.SERVANT, 5);
        }}, multiPlayer);
    }

    @Test
    public void buyDevelopCardTest() throws ModelException{
        board = multiPlayer.getPlayers().get(0).getBoard();
        fillStrongBox1();

        //buy a developcard and put in the first slot
        DevelopCard devCard = multiPlayer.getDecksDevelop().get(Color.GOLD).get(1).topCard();//it is a develop card of level 1 with 2 rocks and 2 shields required required to buy

        board.buyDevelopCard(multiPlayer, Color.GOLD, 1, 0, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.ROCK, 2);
                put(Resource.SHIELD, 2);
            }});
        }});

        assertEquals(devCard, board.getDevelopCardSlots().get(0).getCards().get(0));
        assertEquals(Color.GOLD, board.getDevelopCardSlots().get(0).getCards().get(0).getColor());
        assertEquals(1, board.getDevelopCardSlots().get(0).getCards().get(0).getLevel());
        assertEquals(3, multiPlayer.getDecksDevelop().get(Color.GOLD).get(1).howManyCards());

        //buy another card of level 1 and put in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.BLUE).get(1).topCard();//it is a develop card of level 1 with 2 golds and 2 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.BLUE, 1, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.GOLD, 2);
                put(Resource.SERVANT, 2);
            }});
        }});

        assertEquals(devCard, board.getDevelopCardSlots().get(1).getCards().get(0));
        assertEquals(Color.BLUE, board.getDevelopCardSlots().get(1).getCards().get(0).getColor());
        assertEquals(1, board.getDevelopCardSlots().get(1).getCards().get(0).getLevel());
        assertEquals(3, multiPlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());

        //buy a card of level 2 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).topCard();//it is a develop card of level 2 with 3 shields and 3 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 2, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 3);
                put(Resource.SERVANT, 3);
            }});
        }});

        assertEquals(devCard, board.getDevelopCardSlots().get(1).getCards().get(1));
        assertEquals(Color.PURPLE, board.getDevelopCardSlots().get(1).getCards().get(1).getColor());
        assertEquals(2, board.getDevelopCardSlots().get(1).getCards().get(1).getLevel());
        assertEquals(3, multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).howManyCards());
    }

    @Test
    public void buyDevelopCardSmartTest() throws ModelException{
        board = multiPlayer.getPlayers().get(0).getBoard();
        fillStrongBox1();

        //buy a developcard and put in the first slot
        DevelopCard devCard = multiPlayer.getDecksDevelop().get(Color.GOLD).get(1).topCard();//it is a develop card of level 1 with 2 rocks and 2 shields required required to buy

        board.buyDevelopCardSmart(multiPlayer, Color.GOLD, 1, 0);

        assertEquals(devCard, board.getDevelopCardSlots().get(0).getCards().get(0));
        assertEquals(Color.GOLD, board.getDevelopCardSlots().get(0).getCards().get(0).getColor());
        assertEquals(1, board.getDevelopCardSlots().get(0).getCards().get(0).getLevel());
        assertEquals(3, multiPlayer.getDecksDevelop().get(Color.GOLD).get(1).howManyCards());

        //buy another card of level 1 and put in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.BLUE).get(1).topCard();//it is a develop card of level 1 with 2 golds and 2 servants required required to buy

        board.buyDevelopCardSmart(multiPlayer, Color.BLUE, 1, 1);

        assertEquals(devCard, board.getDevelopCardSlots().get(1).getCards().get(0));
        assertEquals(Color.BLUE, board.getDevelopCardSlots().get(1).getCards().get(0).getColor());
        assertEquals(1, board.getDevelopCardSlots().get(1).getCards().get(0).getLevel());
        assertEquals(3, multiPlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());

        //buy a card of level 2 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).topCard();//it is a develop card of level 2 with 3 shields and 3 servants required required to buy

        board.buyDevelopCardSmart(multiPlayer, Color.PURPLE, 2, 1);

        assertEquals(devCard, board.getDevelopCardSlots().get(1).getCards().get(1));
        assertEquals(Color.PURPLE, board.getDevelopCardSlots().get(1).getCards().get(1).getColor());
        assertEquals(2, board.getDevelopCardSlots().get(1).getCards().get(1).getLevel());
        assertEquals(3, multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).howManyCards());
    }

    @Test(expected = InvalidDevelopCardToSlotException.class)
    public void buyDevelopCardExceptionTest1() throws ModelException {
        fillStrongBox1();
        //buying a develop card which i cannot obtain
        //buy a card of level 2 and put it in the second slot
        DevelopCard devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).topCard();//it is a develop card of level 2 with 3 shields and 3 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 2, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 3);
                put(Resource.SERVANT, 3);
            }});
        }});
    }

    @Test(expected = FullDevelopSlotException.class)
    public void buyDevelopCardExceptionTest2() throws ModelException {
        fillStrongBox1();
        //adding 4 develop card on the same depot
        //buy another card of level 1 and put in the second slot
        DevelopCard devCard = multiPlayer.getDecksDevelop().get(Color.BLUE).get(1).topCard();//it is a develop card of level 1 with 2 golds and 2 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.BLUE, 1, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.GOLD, 2);
                put(Resource.SERVANT, 2);
            }});
        }});

        //buy a card of level 2 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).topCard();//it is a develop card of level 2 with 3 shields and 3 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 2, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 3);
                put(Resource.SERVANT, 3);
            }});
        }});

        //buy a card of level 3 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(3).topCard();//it is a develop card of level 2 with 4 shields and 4 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 3, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 4);
                put(Resource.SERVANT, 4);
            }});
        }});

        //buy a card of level 3 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(3).topCard();//it is a develop card of level 2 with 6 servants required required to buy

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 3, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SERVANT, 6);
            }});
        }});

    }

    @Test
    public void getVictoryPointsTest() throws ModelException {
        board = multiPlayer.getPlayers().get(0).getBoard();

        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 1);
        }};
        board.storeInNormalDepot(toAdd);

        board.flushGainedResources(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, multiPlayer);


        board.addLeaderCards(new ArrayList<>() {{
            add(depotLeaderCards.get(0));//3 victory points
            add(depotLeaderCards.get(1));//3 victory points
        }});

        board.getLeaderCards().get(0).activate(multiPlayer, multiPlayer.getPlayers().get(0));
        board.getLeaderCards().get(1).activate(multiPlayer, multiPlayer.getPlayers().get(0));

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 2);
        }};
        board.storeInDepotLeader(toAdd);

        buyDevelopCards();
        //should have 63 resources-->12 victory points

        board.moveOnFaithPath(3, multiPlayer);
        int pointsFaithTrack = board.getFaithtrack().getVictoryPoints();//should be 1
        int pointsLeader = 3 + 3;//3 victory points for each leadercard that has the board
        int pointsDevelop = 4 + 8 + 12 + 4;//two cards of level 1, one of level 2 and one of level 3

        assertEquals(12 + pointsFaithTrack + pointsLeader + pointsDevelop, board.getVictoryPoints());
    }

    public void buyDevelopCards() throws ModelException {
        //buy a card of level 1 and put in the second slot
        DevelopCard devCard = multiPlayer.getDecksDevelop().get(Color.BLUE).get(1).topCard();//it is a develop card of level 1 with 2 golds and 2 servants required required to buy,
        // it gives 4 victory points

        board.buyDevelopCard(multiPlayer, Color.BLUE, 1, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.GOLD, 2);
                put(Resource.SERVANT, 2);
            }});
        }});

        //buy a card of level 2 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(2).topCard();//it is a develop card of level 2 with 3 shields and 3 servants required required to buy,
        //it gives 8 victory points

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 2, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 3);
                put(Resource.SERVANT, 3);
            }});
        }});

        //buy a card of level 3 and put it in the second slot
        devCard = multiPlayer.getDecksDevelop().get(Color.PURPLE).get(3).topCard();//it is a develop card of level 2 with 4 shields and 4 servants required required to buy,
        //it gives 12 victory points
        //resources to give: 1 gold
        //resources to gain: 1 servant, 3 rocks

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 3, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 4);
                put(Resource.SERVANT, 4);
            }});
        }});

        //buy a card of level 1 and put in the third slot
        devCard = multiPlayer.getDecksDevelop().get(Color.GREEN).get(1).topCard();//it is a develop card of level 2 with 2 shields and 2 golds required required to buy,
        //it gives 4 victory points
        //resources to give: 1 servant, 1 rock
        //resources to gain: 2 gold, 1 faith

        board.buyDevelopCard(multiPlayer, Color.GREEN, 1, 2, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 2);
                put(Resource.GOLD, 2);
            }});
        }});
    }

    @Test
    public void howManyResourcesTest1() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 1);
        }};
        board.storeInNormalDepot(toAdd);

        assertEquals(2, board.howManyResources());

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 6);
        }};
        board.flushGainedResources(toAdd, multiPlayer);

        assertEquals(9, board.howManyResources());

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 2);
        }};
        board.storeInDepotLeader(toAdd);

        assertEquals(12, board.howManyResources());
    }

    @Test(expected = InvalidTypeOfResourceToDepotException.class)
    public void storeInNormalDepotsExceptionTest1() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ANYTHING, 1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInNormalDepotsExceptionTest2() throws ModelException{
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInNormalDepotsTest1() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
        }};
        board.storeInNormalDepot(toAdd);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 1);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SERVANT, 1);
        }}, board.getResInDepot(2));

        toAdd = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
        }};
        board.storeInNormalDepot(toAdd);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SERVANT, 2);
        }}, board.getResInDepot(2));

        toAdd = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        board.storeInNormalDepot(toAdd);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SERVANT, 3);
        }}, board.getResInDepot(2));

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInDepotLeaderExceptionTest1() throws ModelException {

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        board.storeInDepotLeader(toGain);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInDepotLeaderExceptionTest2() throws ModelException {

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.ROCK, 3);
        }};
        board.storeInDepotLeader(toGain);
    }

    @Test
    public void storeInDepotLeaderTest1() throws ModelException {

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 2);
        }};
        board.storeInDepotLeader(toGain);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 1);
        }}, board.getResInLeaderDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInLeaderDepot(1));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }};
        board.storeInDepotLeader(toGain);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInLeaderDepot(1));
        assertTrue(board.getDepotLeaders().get(0).getDepot().isFull());
        assertTrue(board.getDepotLeaders().get(1).getDepot().isFull());

    }

    @Test
    public void cannotAppendToNormalDepotTest1() throws ModelException {
        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 2);
        }};
        assertFalse(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 3);
        }};
        assertFalse(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 1);
        }};
        assertFalse(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 4);
        }};
        assertTrue(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 1);
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 1);
        }};
        assertTrue(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 1);
            put(Resource.GOLD, 1);
        }};
        board.storeInNormalDepot(toGain);

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 1);
            put(Resource.GOLD, 1);
        }};
        assertTrue(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.GOLD, 3);
        }};
        assertTrue(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.GOLD, 2);
        }};
        assertFalse(board.cannotAppendToNormalDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.SERVANT, 2);
        }};
        assertFalse(board.cannotAppendToNormalDepots(toGain));
    }

    @Test
    public void cannotAppendToLeaderDepotsTest1() throws ModelException {
        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        assertFalse(board.cannotAppendToLeaderDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
        }};
        assertFalse(board.cannotAppendToLeaderDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }};
        assertFalse(board.cannotAppendToLeaderDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.cannotAppendToLeaderDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.cannotAppendToLeaderDepots(toGain));

        toGain = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
            put(Resource.GOLD, 2);
        }};
        assertTrue(board.cannotAppendToLeaderDepots(toGain));
    }

    @Test
    public void enoughResInNormalDepotsTest1() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};
        board.storeInNormalDepot(toAdd);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SERVANT, 3);
        }}, board.getResInDepot(2));

        TreeMap<Resource, Integer> toGive = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
        }};
        assertTrue(board.enoughResInNormalDepots(toGive));

        //assert that the method has not side effects
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SERVANT, 3);
        }}, board.getResInDepot(2));

        toGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.SERVANT, 1);
        }};
        assertFalse(board.enoughResInNormalDepots(toGive));

        toGive = new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }};
        assertFalse(board.enoughResInNormalDepots(toGive));

        toGive = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};
        assertTrue(board.enoughResInNormalDepots(toGive));

    }

    @Test
    public void EnoughResInLeaderDepots() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        board.storeInDepotLeader(toAdd);

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.enoughResInLeaderDepots(toAdd));

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.enoughResInLeaderDepots(toAdd));

        toAdd = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.enoughResInLeaderDepots(toAdd));

        toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 2);
        }};
        assertFalse(board.enoughResInLeaderDepots(toAdd));

        toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.SHIELD, 2);
        }};
        assertFalse(board.enoughResInLeaderDepots(toAdd));
    }

    @Test
    public void flushGainedResourcesTest() throws ModelException {
        board = multiPlayer.getPlayers().get(0).getBoard();
        TreeMap<Resource, Integer> toFlush = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        board.flushGainedResources(toFlush, multiPlayer);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }}, board.getResourcesInStrongBox());

        toFlush = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.GOLD, 2);
        }};
        board.flushGainedResources(toFlush, multiPlayer);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 2);
            put(Resource.GOLD, 2);
        }}, board.getResourcesInStrongBox());

        toFlush = new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 1);
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.FAITH, 1);
        }};
        board.flushGainedResources(toFlush, multiPlayer);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 4);
            put(Resource.SHIELD, 3);
            put(Resource.GOLD, 3);
            put(Resource.SERVANT, 1);
        }}, board.getResourcesInStrongBox());
        assertEquals(1, board.getFaithtrack().getPosition());

        toFlush = new TreeMap<>() {{
            put(Resource.ROCK, 5);
            put(Resource.FAITH, 3);
        }};
        board.flushGainedResources(toFlush, multiPlayer);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 9);
            put(Resource.SHIELD, 3);
            put(Resource.GOLD, 3);
            put(Resource.SERVANT, 1);
        }}, board.getResourcesInStrongBox());
        assertEquals(4, board.getFaithtrack().getPosition());
    }

    @Test
    public void enoughResInStrongBoxTest1() throws ModelException {
        TreeMap<Resource, Integer> toFlush = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
            put(Resource.GOLD, 4);
        }};
        board.flushGainedResources(toFlush, multiPlayer);

        TreeMap<Resource, Integer> toSpend = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
            put(Resource.GOLD, 4);
        }};
        assertTrue(board.enoughResInStrongBox(toSpend));

        toSpend = new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }};
        assertTrue(board.enoughResInStrongBox(toSpend));

        toSpend = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.enoughResInStrongBox(toSpend));

        toSpend = new TreeMap<>() {{
            put(Resource.GOLD, 3);
        }};
        assertTrue(board.enoughResInStrongBox(toSpend));

        toSpend = new TreeMap<>() {{
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
            put(Resource.GOLD, 4);
        }};
        assertFalse(board.enoughResInStrongBox(toSpend));

        toSpend = new TreeMap<>() {{
            put(Resource.GOLD, 7);
        }};
        assertFalse(board.enoughResInStrongBox(toSpend));
    }

    @Test
    public void removeResFromNormalDepotTest1() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};
        board.storeInNormalDepot(toAdd);

        TreeMap<Resource, Integer> toRemove = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};
        board.removeResFromNormalDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>(), board.getResInNormalDepots());

        toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 2);
        }};
        board.storeInNormalDepot(toAdd);

        toRemove = new TreeMap<>() {{
            put(Resource.GOLD, 1);
        }};
        board.removeResFromNormalDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInNormalDepots());

        toRemove = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        board.removeResFromNormalDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 1);
        }}, board.getResInNormalDepots());

        toRemove = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        board.removeResFromNormalDepot(toRemove);

        assertTrue(board.getResInNormalDepots().isEmpty());

        toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 3);
        }};
        board.storeInNormalDepot(toAdd);

        toRemove = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
            put(Resource.GOLD, 1);
        }};
        board.removeResFromNormalDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SERVANT, 2);
        }}, board.getResInNormalDepots());
    }

    @Test
    public void removeResFromLeaderDepotTest1() throws ModelException {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        board.storeInDepotLeader(toAdd);

        TreeMap<Resource, Integer> toRemove = new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }};
        board.removeResFromLeaderDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInLeaderDepots());

        toRemove = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        board.removeResFromLeaderDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 1);
        }}, board.getResInLeaderDepots());

        toRemove = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        board.removeResFromLeaderDepot(toRemove);

        assertEquals(new TreeMap<Resource, Integer>(), board.getResInLeaderDepots());
    }

    @Test
    public void removeResFromStrongBoxTest1() throws ModelException {
        TreeMap<Resource, Integer> toFlush = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
            put(Resource.GOLD, 4);
        }};
        board.flushGainedResources(toFlush, multiPlayer);

        TreeMap<Resource, Integer> toRemove = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 3);
            put(Resource.GOLD, 4);
        }};
        board.removeResFromStrongBox(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResourcesInStrongBox());

        toRemove = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        board.removeResFromStrongBox(toRemove);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 1);
        }}, board.getResourcesInStrongBox());

        toRemove = new TreeMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        board.removeResFromStrongBox(toRemove);

        assertEquals(new TreeMap<Resource, Integer>(), board.getResourcesInStrongBox());
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesExceptionTest1() throws ModelException {
        //invalid resource in tokeep
        //preparing the board
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.NOTHING, 1);//invalid resource
                put(Resource.SHIELD, 3);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesExceptionTest2() throws ModelException {
        //invalid resource in resGained
        //preparing the board
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.NOTHING, 1);//invalid resource
            put(Resource.SHIELD, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SHIELD, 3);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesExceptionTest3() throws ModelException {
        //tokeep greater than togain
        //preparing the board
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SHIELD, 6);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesExceptionTest4() throws ModelException {
        //strongbox in tokeep
        //preparing the board
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 3);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{//illegal
                put(Resource.GOLD, 1);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }
    }

    @Test
    public void gainResourcesMultiplayerTest1()throws ModelException  {
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.FAITH, 2);
            put(Resource.SHIELD, 3);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SHIELD, 3);
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        board = multiPlayer.getPlayers().get(0).getBoard();
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 3);
            put(Resource.ROCK, 2);
        }}, board.getResInNormalDepots());
        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }}, board.getResInLeaderDepots());
        assertEquals(2, board.getFaithtrack().getPosition());
        for (int i = 1; i < 4; i++)
            assertEquals(0, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getPosition());

        resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.FAITH, 2);
            put(Resource.SHIELD, 2);
        }};

        toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 3);
            put(Resource.ROCK, 2);
        }}, board.getResInNormalDepots());
        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 2);
        }}, board.getResInLeaderDepots());
        assertEquals(4, board.getFaithtrack().getPosition());
        for (int i = 1; i < 4; i++)
            assertEquals(1, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getPosition());//one resource discarded

        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();

        board = multiPlayer.getPlayers().get(1).getBoard();

        resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.FAITH, 2);
            put(Resource.SHIELD, 2);
        }};

        toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
            }});
        }};

        try {
            board.gainResources(resGained, toKeep, multiPlayer);//one resource discarded
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.SHIELD, 2);
        }}, board.getResInNormalDepots());
        assertEquals(3, board.getFaithtrack().getPosition());
        assertEquals(5, multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getPosition());//test the faithtrack of the other players
        assertEquals(2, multiPlayer.getPlayers().get(2).getBoard().getFaithtrack().getPosition());
        assertEquals(2, multiPlayer.getPlayers().get(3).getBoard().getFaithtrack().getPosition());
    }

    @Test
    public void gainResourcesSingleplayerTest1() throws ModelException {
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.FAITH, 2);
            put(Resource.SERVANT, 1);
        }};

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.ROCK, 2);
                put(Resource.SERVANT, 1);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        board = singlePlayer.getPlayer().getBoard();
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot

        try {
            board.gainResources(resGained, toKeep, singlePlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 1);
        }}, board.getResInNormalDepots());
        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }}, board.getResInLeaderDepots());
        assertEquals(2, board.getFaithtrack().getPosition());
        assertEquals(1, singlePlayer.getLorenzo().getFaithTrack().getPosition());
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesSmartExceptionTest1() throws ModelException {
        //invalid resource in tokeep
        board = multiPlayer.getPlayers().get(0).getBoard();

        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 2);
        }};

        TreeMap<Resource, Integer> toKeep = new TreeMap<>() {{
            put(Resource.NOTHING, 1);
            put(Resource.ROCK, 2);
        }};

        try {
            board.gainResourcesSmart(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesSmartExceptionTest2() throws ModelException {
        //invalid resource in togain
        board = multiPlayer.getPlayers().get(0).getBoard();

        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ANYTHING, 3);
            put(Resource.ROCK, 2);
        }};

        TreeMap<Resource, Integer> toKeep = new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }};

        try {
            board.gainResourcesSmart(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void gainResourcesSmartExceptionTest3() throws ModelException {
        //tokeep or different than togain
        board = multiPlayer.getPlayers().get(0).getBoard();

        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 2);
        }};

        TreeMap<Resource, Integer> toKeep = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 1);
        }};

        board.gainResourcesSmart(resGained, toKeep, multiPlayer);
    }


    @Test
    public void gainResourcesSmartTest1() throws ModelException {
        board = multiPlayer.getPlayers().get(0).getBoard();

        //adding leader depots
        //fill the strongbox to meet the leadercards requirements
        fillStrongBox1();

        board.addLeaderCards(new ArrayList<>() {{
            add(depotLeaderCards.get(0));
            add(depotLeaderCards.get(1));
        }});

        board.getLeaderCards().get(0).activate(multiPlayer, multiPlayer.getPlayers().get(0));//adding a rock depot
        board.getLeaderCards().get(1).activate(multiPlayer, multiPlayer.getPlayers().get(0));//adding a shield depot


        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 2);
        }};

        TreeMap<Resource, Integer> toKeep = new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.GOLD, 1);
        }};

        try {
            board.gainResourcesSmart(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD, 1);
        }}, board.getResInNormalDepots());


        resGained = new TreeMap<>() {{
            put(Resource.SERVANT, 4);
            put(Resource.ROCK, 2);
            put(Resource.FAITH, 1);
        }};

        toKeep = new TreeMap<>() {{//one resource discarded
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 3);
            put(Resource.FAITH, 1);
        }};

        try {
            board.gainResourcesSmart(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 3);
        }}, board.getResInNormalDepots());

        for (int i = 0; i < 4; i++)
            assertEquals(1, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getPosition());


        resGained = new TreeMap<>() {{
            put(Resource.SERVANT, 4);
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 2);
        }};

        toKeep = new TreeMap<>() {{//four resources discarded
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 2);
        }};

        try {
            board.gainResourcesSmart(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }}, board.getResInLeaderDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 3);
        }}, board.getResInNormalDepots());

        assertEquals(3, multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getPosition());
        for (int i = 1; i < 4; i++)
            assertEquals(5, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getPosition());
    }

    @Test
    public void activateProductionTest1() throws ModelException {
        board = multiPlayer.getPlayers().get(0).getBoard();

        createEnvironment();

        //activating the normal production
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
            }});
        }};

        TreeMap<Resource, Integer> resToGain = new TreeMap<>() {{
            put(Resource.GOLD, 1);
        }};

        try {
            board.activateProduction(0, resToGive, resToGain, multiPlayer);
        } catch (InvalidResourcesByPlayerException | InvalidProductionSlotChosenException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 3);
            put(Resource.GOLD, 1);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepots());

        board.flushResFromProductions(multiPlayer);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 17);
            put(Resource.SHIELD, 11);
            put(Resource.SERVANT, 11);
            put(Resource.ROCK, 20);
        }}, board.getResourcesInStrongBox());

        //activating the second production slot
        //i should have a card with the following production: {resources to give: 1 gold ;resources to gain: 1 servant, 3 rocks}
        resToGive = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 1);
            }});
        }};

        resToGain = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 3);
        }};

        try {
            board.activateProduction(2, resToGive, resToGain, multiPlayer);
        } catch (InvalidResourcesByPlayerException | InvalidProductionSlotChosenException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 3);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepots());

        board.flushResFromProductions(multiPlayer);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 17);
            put(Resource.SHIELD, 11);
            put(Resource.SERVANT, 12);
            put(Resource.ROCK, 23);
        }}, board.getResourcesInStrongBox());

        //activating the second leader production slot
        //activating the leader
        board.addLeaderCards(new ArrayList<>() {{
            add(productionLeaderCards.get(0));
            add(productionLeaderCards.get(1));
        }});
        /*
        adding a leader a card with the following production: "resourcesToGive": "ROCK": 1; "resourcesToGain": "FAITH": 1,"ANYTHING": 1
        it requires a purple develop card of level 2 to be activated, the demand is met
        */
        board.getLeaderCards().get(3).activate(multiPlayer, multiPlayer.getPlayers().get(0));

        resToGive = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};

        resToGain = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
            put(Resource.FAITH, 1);
        }};

        try {
            board.activateProduction(4, resToGive, resToGain, multiPlayer);
        } catch (InvalidResourcesByPlayerException | InvalidProductionSlotChosenException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 3);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepots());

        board.flushResFromProductions(multiPlayer);

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 17);
            put(Resource.SHIELD, 11);
            put(Resource.SERVANT, 13);
            put(Resource.ROCK, 23);
        }}, board.getResourcesInStrongBox());

        assertEquals(1, board.getFaithtrack().getPosition());
    }

    public void createEnvironment() throws ModelException {
        buildBoard2();
        buyDevelopCards();
    }

    @Test(expected = InvalidResourcesByPlayerException.class)
    public void activateProductionTestException1() throws ModelException {
        board = multiPlayer.getPlayers().get(0).getBoard();
        createEnvironment();

        //activating the base production with faith in resToGain
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 2);
            }});
        }};

        TreeMap<Resource, Integer> resToGain = new TreeMap<>() {{
            put(Resource.FAITH, 1);
        }};

        try {
            board.activateProduction(0, resToGive, resToGain, multiPlayer);
        } catch (InvalidProductionSlotChosenException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void prepareBoardForTest() throws ModelException {
        //preparing the board
        board = multiPlayer.getPlayers().get(0).getBoard();

        //adding leader depots
        //fill the strongbox to meet the leadercards requirements
        fillStrongBox3();//adding 5 servants and 5 golds

        board.addLeaderCards(new ArrayList<>() {{
            add(depotLeaderCards.get(0));
            add(depotLeaderCards.get(1));
        }});

        board.getLeaderCards().get(0).activate(multiPlayer, multiPlayer.getPlayers().get(0));//adding a rock depot
        board.getLeaderCards().get(1).activate(multiPlayer, multiPlayer.getPlayers().get(0));//adding a shield depot


        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};

        TreeMap<Resource, Integer> toKeep = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 3);
        }};

        try {
            board.gainResourcesSmart(resGained, toKeep, multiPlayer);
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = ResourceNotDiscountableException.class)
    public void enoughResToActivateExceptionTest1() throws ModelException {
        //giving an illegal Resource
        prepareBoardForTest();

        TreeMap<Resource, Integer> resToGive = new TreeMap<>() {{
            put(Resource.FAITH, 2);
        }};

        board.enoughResToActivate(resToGive);
    }

    @Test
    public void enoughResToActivateTest1() throws ModelException {
        prepareBoardForTest();

        //start to test
        TreeMap<Resource, Integer> resToGive = new TreeMap<>() {{
            put(Resource.SERVANT, 3);
        }};
        assertTrue(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.GOLD,4 );
        }};
        assertTrue(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.ROCK, 3);
        }};
        assertTrue(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
        }};
        assertTrue(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
        }};
        assertTrue(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 3);
            put(Resource.SHIELD, 3);
        }};
        assertFalse(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.GOLD,8);
        }};
        assertFalse(board.enoughResToActivate(resToGive));

        resToGive = new TreeMap<>() {{
            put(Resource.GOLD, 3);
            put(Resource.ROCK, 4);
            put(Resource.SERVANT, 4);
        }};
        assertFalse(board.enoughResToActivate(resToGive));
    }

    @Test
    public void removeResourcesSmartTest() throws ModelException{
        prepareBoardForTest();

        TreeMap<Resource,Integer> resToGive=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.SHIELD,2);
        }};

        board.removeResourcesSmart(resToGive);

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 1);
            put(Resource.GOLD,1);
            put(Resource.SERVANT, 3);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }}, board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 5);
            put(Resource.SERVANT, 5);
        }}, board.getResourcesInStrongBox());


        resToGive=new TreeMap<>(){{
            put(Resource.ROCK,2);
        }};

        board.removeResourcesSmart(resToGive);

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD,1);
            put(Resource.SERVANT, 3);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }}, board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 5);
            put(Resource.SERVANT, 5);
        }}, board.getResourcesInStrongBox());


        resToGive=new TreeMap<>(){{
            put(Resource.ROCK,1);
            put(Resource.SERVANT,2);
        }};

        board.removeResourcesSmart(resToGive);

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD,1);
            put(Resource.SERVANT, 1);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() , board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 5);
            put(Resource.SERVANT, 5);
        }}, board.getResourcesInStrongBox());


        resToGive=new TreeMap<>(){{
            put(Resource.SERVANT,2);
        }};

        board.removeResourcesSmart(resToGive);

        assertEquals(new TreeMap<>() {{
            put(Resource.GOLD,1);
        }}, board.getResInNormalDepots());

        assertEquals(new TreeMap<>() , board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 5);
            put(Resource.SERVANT, 4);
        }}, board.getResourcesInStrongBox());

        resToGive=new TreeMap<>(){{
            put(Resource.SERVANT,2);
            put(Resource.GOLD,2);
        }};

        board.removeResourcesSmart(resToGive);

        assertEquals(new TreeMap<>(), board.getResInNormalDepots());

        assertEquals(new TreeMap<>() , board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 4);
            put(Resource.SERVANT, 2);
        }}, board.getResourcesInStrongBox());

        resToGive=new TreeMap<>(){{
            put(Resource.SERVANT,2);
            put(Resource.GOLD,2);
        }};

        board.removeResourcesSmart(resToGive);

        assertEquals(new TreeMap<>(), board.getResInNormalDepots());

        assertEquals(new TreeMap<>() , board.getResInLeaderDepots());

        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 2);
        }}, board.getResourcesInStrongBox());
    }
}