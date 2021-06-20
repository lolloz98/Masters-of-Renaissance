package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.localcards.LocalConcealedCard;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.Turn;

import java.util.ArrayList;

/**
 * Useful class to automatically create answers
 */
public final class AnswerFactory {
    public static GameStatusAnswer createGameStatusAnswer(int gameId, int playerId, int playerIdReceiver, Game<?> game) throws UnexpectedControllerException {
        return new GameStatusAnswer(gameId,
                playerId,
                playerIdReceiver,
                ConverterToLocalModel.convert(game, playerIdReceiver, gameId));
    }

    public static ChooseOneResPrepAnswer createChooseOneResPrepAnswer(int gameId, int playerId, Resource res, Game<?> game){
        return new ChooseOneResPrepAnswer(gameId, playerId, res, ConverterToLocalModel.getGameState(game));
    }

    public static RemoveLeaderPrepAnswer createRemoveLeaderPrepAnswer(int gameId, int playerId, ArrayList<Integer> toRemove, Game<? extends Turn> game) throws UnexpectedControllerException {
        return new RemoveLeaderPrepAnswer(gameId, playerId, toRemove, ConverterToLocalModel.getGameState(game));
    }

    public static RemoveLeaderPrepAnswer createConcealedRemoveLeaderPrepAnswer(RemoveLeaderPrepAnswer answer) {
        return new RemoveLeaderPrepAnswer(answer.getGameId(), answer.getPlayerId(), new ArrayList<>(){{add(0); add(0);}}, answer.getState());
    }

    public static DiscardLeaderAnswer createConcealedDiscardLeaderAnswer(DiscardLeaderAnswer answer) {
        return new DiscardLeaderAnswer(answer.getGameId(), answer.getPlayerId(), new LocalConcealedCard(true), answer.getLocalTracks(), answer.getLorenzoTrack());
    }
}
