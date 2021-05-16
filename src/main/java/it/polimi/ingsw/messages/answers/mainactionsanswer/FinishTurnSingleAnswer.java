package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.cards.lorenzo.LorenzoCard;

public class FinishTurnSingleAnswer extends Answer {
    private final LocalDevelopmentGrid localGrid;
    private final LocalTrack localLorenzoTrack;
    private final LocalTrack localPlayerTrack;


    public FinishTurnSingleAnswer(int gameId, int playerId, LocalDevelopmentGrid localGrid, LocalTrack localPlayerTrack, LocalTrack localLorenzoTrack) {
        super(gameId, playerId);
        this.localGrid = localGrid;
        this.localLorenzoTrack = localLorenzoTrack;
        this.localPlayerTrack = localPlayerTrack;
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
}
