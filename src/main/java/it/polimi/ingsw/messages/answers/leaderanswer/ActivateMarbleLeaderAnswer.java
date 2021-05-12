package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;

public class ActivateMarbleLeaderAnswer extends LeaderAnswer {

    public ActivateMarbleLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader) {
        super(gameId, playerId,leader);
    }

}
