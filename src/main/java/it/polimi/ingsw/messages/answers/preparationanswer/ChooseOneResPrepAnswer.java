package it.polimi.ingsw.messages.answers.preparationanswer;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

public class ChooseOneResPrepAnswer extends Answer {
    private final Resource res;
    private final LocalGameState state;

    public Resource getRes() {
        return res;
    }

    public ChooseOneResPrepAnswer(int gameId, int playerId, Resource res, LocalGameState state) {
        super(gameId, playerId);
        this.res = res;
        this.state = state;
    }

    public LocalGameState getState() {
        return state;
    }
}
