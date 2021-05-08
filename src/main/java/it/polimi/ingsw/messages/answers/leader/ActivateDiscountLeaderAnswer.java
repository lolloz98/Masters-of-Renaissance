package it.polimi.ingsw.messages.answers.leader;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;

import java.util.TreeMap;

public class ActivateDiscountLeaderAnswer extends Answer {
    private final TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;

    public ActivateDiscountLeaderAnswer(int gameId, int playerId, TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop) {
        super(gameId, playerId);
        this.decksDevelop = new TreeMap<>(decksDevelop);
    }
}
