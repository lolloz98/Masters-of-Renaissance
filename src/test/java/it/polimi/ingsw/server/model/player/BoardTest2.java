package it.polimi.ingsw.server.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class BoardTest2 {
    Gson gson;
    SinglePlayer singlePlayer;

    @Before
    public void setUp() throws ModelException  {
        CollectionsHelper.setTest();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        singlePlayer = new SinglePlayer(new Player("play", 0));
    }

    @Test
    public void testStoreInNormalDepot() throws ModelException {
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 1);
        }});
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.SERVANT, 2);
        }});
    }

    @Test
    public void testStoreInNormalDepot2() throws ModelException {
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 1);
        }});
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.ROCK, 2);
        }});
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void testStoreInNormalDepot3() throws ModelException {
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 1);
        }});
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.ROCK, 3);
        }});
    }

    @Test
    public void testStoreInNormalDepot6() throws ModelException {
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 1);
        }});
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.ROCK, 2);
            put(Resource.SERVANT, 1);
        }});
        singlePlayer.getPlayer().getBoard().removeResFromNormalDepot(new TreeMap<>() {{
            put(Resource.ROCK, 1);
        }});
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.SERVANT, 1);
        }});
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void testStoreInNormalDepot4() throws ModelException {
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 1);
            put(Resource.ROCK, 4);
        }});
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void testStoreInNormalDepot5() throws ModelException {
        singlePlayer.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 4);
            put(Resource.ROCK, 1);
        }});
    }

    @Test
    public void testGaignResources() throws ModelException, FileNotFoundException, InvalidResourcesToKeepByPlayerException {
        singlePlayer.getPlayer().getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, singlePlayer);
        String path;
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        DepotLeaderCard dlc = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        singlePlayer.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
            add(dlc);
        }});
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).activate(singlePlayer, singlePlayer.getPlayer());
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
        // lorenzo track should be at pos 2 because i discarded 2 shields
        assertEquals(2,singlePlayer.getLorenzo().getFaithTrack().getPosition());
    }

    // trying to push resources of the wrong type to leader
    @Test(expected = InvalidResourcesToKeepByPlayerException.class)
    public void testGaignResourcesException() throws ModelException, FileNotFoundException, InvalidResourcesToKeepByPlayerException {
        singlePlayer.getPlayer().getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, singlePlayer);
        String path;
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        DepotLeaderCard dlc = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        singlePlayer.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
            add(dlc);
        }});
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).activate(singlePlayer, singlePlayer.getPlayer());
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.SERVANT, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.SERVANT, 2);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
    }

    // trying to push resources from market to strongbox
    @Test(expected = InvalidArgumentException.class)
    public void testGaignResourcesException1() throws ModelException, FileNotFoundException, InvalidResourcesToKeepByPlayerException {
        singlePlayer.getPlayer().getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, singlePlayer);
        String path;
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        DepotLeaderCard dlc = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        singlePlayer.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
            add(dlc);
        }});
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).activate(singlePlayer, singlePlayer.getPlayer());
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
    }

    @Test
    public void testGaignResourcesTwoLeaders() throws ModelException, FileNotFoundException, InvalidResourcesToKeepByPlayerException {
        singlePlayer.getPlayer().getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, singlePlayer);
        String path;
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        DepotLeaderCard dlc1 = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 55); //resource type: SHIELD
        DepotLeaderCard dlc2 = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        singlePlayer.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
            add(dlc1);
            add(dlc2);
        }});
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).activate(singlePlayer, singlePlayer.getPlayer());
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(1).activate(singlePlayer, singlePlayer.getPlayer());
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 2);
                put(Resource.SHIELD, 2);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
    }

    @Test
    public void testGaignResourcesTwoLeadersException() throws ModelException, FileNotFoundException, InvalidResourcesToKeepByPlayerException {
        singlePlayer.getPlayer().getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, singlePlayer);
        String path;
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        DepotLeaderCard dlc1 = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 55); //resource type: SHIELD
        DepotLeaderCard dlc2 = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        singlePlayer.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
            add(dlc1);
            add(dlc2);
        }});
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).activate(singlePlayer, singlePlayer.getPlayer());
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(1).activate(singlePlayer, singlePlayer.getPlayer());
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 2);
                put(Resource.SHIELD, 2);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
    }

    // test changing turn
    @Test
    public void testGaignResourcesNextTurn() throws ModelException, FileNotFoundException, InvalidResourcesToKeepByPlayerException {
        singlePlayer.getPlayer().getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 20);
            put(Resource.SERVANT, 20);
            put(Resource.SHIELD, 20);
            put(Resource.ROCK, 20);
        }}, singlePlayer);
        String path;
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        DepotLeaderCard dlc = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
        singlePlayer.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
            add(dlc);
        }});
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).activate(singlePlayer, singlePlayer.getPlayer());
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.ROCK, 2);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).removeEffect(singlePlayer);
        singlePlayer.nextTurn();
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).applyEffect(singlePlayer);
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.ROCK, 2);
        }}, singlePlayer.getPlayer().getBoard().getResInLeaderDepot(0));
        singlePlayer.getPlayer().getBoard().getLeaderCards().get(0).removeEffect(singlePlayer);
    }

    @Test (expected = ResourceNotDiscountableException.class)
    public void testEnoughResInNormalDepot() throws ModelException {
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
        TreeMap<Resource, Integer> resToGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 2);
        }};
        singlePlayer.getPlayer().getBoard().enoughResInNormalDepots(resToGive);
    }

    @Test
    public void testEnoughResInNormalDepot2() throws ModelException, InvalidResourcesToKeepByPlayerException {
        TreeMap<Resource, Integer> resGained = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 2);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(resGained, toKeep, singlePlayer);
        TreeMap<Resource, Integer> resToGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.SHIELD, 1);
        }};
        assertFalse(singlePlayer.getPlayer().getBoard().enoughResInNormalDepots(resToGive));
    }

    @Test
    public void testGainResources2() throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, NotEnoughResourcesException {
        TreeMap<Resource, Integer> initial = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 3);
        }};
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> initialToKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 3);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(
                initial, initialToKeep, singlePlayer
        );

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.ROCK, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().payResources(toPay);

        initial = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ROCK, 1);
        }};
        initialToKeep = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.SHIELD, 2);
            }});
        }};
        singlePlayer.getPlayer().getBoard().gainResources(
                initial, initialToKeep, singlePlayer
        );
    }
}