package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLorenzoCard;
import it.polimi.ingsw.messages.answers.Answer;

public class FinishTurnSingleAnswer extends Answer {
    private final LocalDevelopmentGrid localGrid;
    /**
     * all useful information about what Lorenzo did
     */
    private final LocalTrack localLorenzoTrack;
    private final LocalTrack localPlayerTrack;
    private final LocalLorenzoCard lorenzoCard;


    public FinishTurnSingleAnswer(int gameId, int playerId, LocalDevelopmentGrid localGrid, LocalTrack localPlayerTrack, LocalTrack localLorenzoTrack, LocalLorenzoCard lorenzoCard) {
        super(gameId, playerId);
        this.localGrid = localGrid;
        this.localLorenzoTrack = localLorenzoTrack;
        this.localPlayerTrack = localPlayerTrack;
        this.lorenzoCard = lorenzoCard;
    }

    public LocalDevelopmentGrid getLocalGrid() {
        return localGrid;
    }

    public LocalTrack getLocalLorenzoTrack() {
        return localLorenzoTrack;
    }

    public LocalTrack getLocalPlayerTrack() {
        return localPlayerTrack;
    }

    public LocalLorenzoCard getLorenzoCard() {
        return lorenzoCard;
    }
}
