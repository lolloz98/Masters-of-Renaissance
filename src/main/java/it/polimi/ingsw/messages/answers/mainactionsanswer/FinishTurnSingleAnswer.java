package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.cards.lorenzo.LorenzoCard;

public class FinishTurnSingleAnswer extends Answer {
    private final LocalDevelopmentGrid localGrid;
    private final LocalTrack localTrack;
    private final LorenzoCard lorenzoCard;


    public FinishTurnSingleAnswer(int gameId, int playerId, LocalDevelopmentGrid localGrid, LorenzoCard lorenzoCard, LocalTrack localTrack) {
        super(gameId, playerId);
        this.localGrid = localGrid;
        this.lorenzoCard = lorenzoCard;
        this.localTrack = localTrack;
    }
}
