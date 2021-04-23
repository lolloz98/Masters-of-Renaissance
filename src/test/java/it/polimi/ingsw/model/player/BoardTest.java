package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.model.cards.leader.LeaderCard;
import it.polimi.ingsw.model.cards.leader.Requirement;
import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.utility.CollectionsHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class BoardTest {
    private ArrayList<DevelopCard> developCards;
    private ArrayList<LeaderCard<? extends Requirement>> depotLeaderCards;
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
        String path;

        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        depotLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 55);//resource type: SHIELD
        depotLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));

        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot leader to board
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot leader to board
        assertEquals(2, board.getDepotLeaders().size());
        assertEquals(Resource.ROCK, board.getDepotLeaders().get(0).getDepot().getTypeOfResource());
        assertEquals(Resource.SHIELD, board.getDepotLeaders().get(1).getDepot().getTypeOfResource());

    }

    public void buildBoard() {
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

        TreeMap<Resource, Integer> toFlush = new TreeMap<>() {{
            put(Resource.GOLD, 5);
        }};

        board.flushGainedResources(toFlush, multiPlayer);
    }

    @Test
    public void enoughResourcesToPay() {
        buildBoard();
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
    public void payResourcesTest() {
        buildBoard();

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

    public void fillStrongBox() {
        board.flushGainedResources(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, multiPlayer);
    }

    @Test
    public void buyDevelopCardTest() {
        board = multiPlayer.getPlayers().get(0).getBoard();
        fillStrongBox();

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

    @Test(expected = InvalidDevelopCardToSlotException.class)
    public void buyDevelopCardExceptionTest1() {
        fillStrongBox();
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
    public void buyDevelopCardExceptionTest2() {
        fillStrongBox();
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
    public void getVictoryPointsTest() {
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

        board.buyDevelopCard(multiPlayer, Color.PURPLE, 3, 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 4);
                put(Resource.SERVANT, 4);
            }});
        }});

        //buy a card of level 1 and put in the third slot
        devCard = multiPlayer.getDecksDevelop().get(Color.GREEN).get(1).topCard();//it is a develop card of level 2 with 2 shields and 2 golds required required to buy,
        //it gives 4 victory points

        board.buyDevelopCard(multiPlayer, Color.GREEN, 1, 2, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<Resource, Integer>() {{
                put(Resource.SHIELD, 2);
                put(Resource.GOLD, 2);
            }});
        }});
        //should have 63 resources-->12 victory points

        board.moveOnFaithPath(3, multiPlayer);
        int pointsFaithTrack = board.getFaithtrack().getVictoryPoints();//should be 1
        int pointsLeader = 3 + 3;//3 victory points for each leadercard that has the board
        int pointsDevelop = 4 + 8 + 12 + 4;//two cards of level 1, one of level 2 and one of level 3

        assertEquals(12 + pointsFaithTrack + pointsLeader + pointsDevelop, board.getVictoryPoints());
    }

    @Test
    public void howManyResourcesTest1() {
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

    @Test(expected = InvalidTypeOfResourceToDepotExeption.class)
    public void storeInNormalDepotsExceptionTest1() {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.ANYTHING, 1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInNormalDepotsExceptionTest2() {
        TreeMap<Resource, Integer> toAdd = new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInNormalDepotsTest1() {
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
    public void storeInDepotLeaderExceptionTest1() {

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        board.storeInDepotLeader(toGain);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInDepotLeaderExceptionTest2() {

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.ROCK, 3);
        }};
        board.storeInDepotLeader(toGain);
    }

    @Test
    public void storeInDepotLeaderTest1() {

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
    public void cannotAppendToNormalDepotTest1() {
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
    public void cannotAppendToLeaderDepotsTest1() {
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
    public void enoughResInNormalDepotsTest1() {
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
    public void EnoughResInLeaderDepots() {
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
    public void flushGainedResourcesTest() {
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
    public void enoughResInStrongBoxTest1() {
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
    public void removeResFromNormalDepotTest1() {
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
    public void removeResFromLeaderDepotTest1() {
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
    public void removeResFromStrongBoxTest1() {
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

    @Test
    public void gainResourcesExceptionTest1() {
        //TODO:risorsa invalida in tokeep
    }

    @Test
    public void gainResourcesExceptionTest2() {
        //TODO:risorsa invalida in togain
    }

    @Test
    public void gainResourcesExceptionTest3() {
        //TODO:tokeep greater that togain
    }

    @Test
    public void gainResourcesExceptionTest4() {
        //TODO:strongbox in tokeep
    }

    @Test
    public void gainResourcesMultiplayerTest1() {
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
    public void gainResourcesSingleplayerTest1() {
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

    @Test
    public void gainResourcesSmartExceptionTest1() {
        //TODO:risorsa invalida in tokeep
    }

    @Test
    public void gainResourcesSmartExceptionTest2() {
        //TODO:risorsa invalida in togain
    }

    @Test
    public void gainResourcesSmartExceptionTest3() {
        //TODO:tokeep greater that togain
    }

    @Test
    public void gainResourcesSmartExceptionTest4() {
        //TODO:strongbox in tokeep
    }

    @Test
    public void gainResourcesSmartTest1() {
        //TODO
    }

}