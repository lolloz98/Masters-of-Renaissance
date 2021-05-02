package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalDevelopmentGrid extends LocalModelAbstract {
    private UI ui;
    private LocalDevelopCard[][] topDevelopCards;
    private int[][] developCardsNumber;

    public LocalDevelopCard[][] getTopDevelopCards() {
        return topDevelopCards;
    }

    public void setTopDevelopCards(LocalDevelopCard[][] topDevelopCards) {
        this.topDevelopCards = topDevelopCards;
    }

    public int[][] getDevelopCardsNumber() {
        return developCardsNumber;
    }

    public void setDevelopCardsNumber(int[][] developCardsNumber) {
        this.developCardsNumber = developCardsNumber;
    }

    public LocalDevelopmentGrid(UI ui){
        topDevelopCards = new LocalDevelopCard[3][4];
        developCardsNumber = new int[3][4];
        this.ui=ui;
        // todo substitute with real constructor
       for(int i=0; i<3; i++){
           for(int j=0; j<3; j++){
               developCardsNumber[i][j] = 3;
               topDevelopCards[i][j] = new LocalDevelopCard(
                       new TreeMap<>(){{ // cost
                           put(Resource.GOLD, 1);
                           put(Resource.SHIELD, 2);
                       }},
                       1,
                       Color.BLUE,
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
    }

}
