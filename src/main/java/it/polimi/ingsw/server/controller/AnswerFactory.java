package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.Turn;

import java.util.ArrayList;

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
}
