package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

public class CLITest extends TestCase {
    CLI cli;

    @Before
    public void setUp(){
        cli = new CLI();
        cli.setup();
    }

    @Test
    public void testDevelopmentGrid(){
        LocalDevelopmentGrid localDevelopmentGrid = cli.getLocalDevelopmentGrid();
        int[][] developCardsNumber = new int[4][3];
        LocalDevelopCard[][] topDevelopCards = new LocalDevelopCard[4][3];
        for(int y=0; y<3; y++){
            for(int x=0; x<4; x++){
                developCardsNumber[x][y] = 4;
                topDevelopCards[x][y] = new LocalDevelopCard(
                        new TreeMap<>(){{ // cost
                            put(Resource.GOLD, 1);
                            put(Resource.SHIELD, 2);
                        }},
                        1,
                        Color.BLUE,
                        3,
                        new TreeMap<>(){{ //resToGive
                            put(Resource.GOLD, 1);
                            put(Resource.SHIELD, 2);
                        }},
                        new TreeMap<>(){{ //resToGive
                            put(Resource.GOLD, 1);
                            put(Resource.SHIELD, 2);
                        }}
                );
            }
        }
        localDevelopmentGrid.setDevelopCardsNumber(developCardsNumber);
        localDevelopmentGrid.setTopDevelopCards(topDevelopCards);
        cli.handleCommand("develop");
    }
}