package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.game.Game;

public final class AnswerFactory {
    public static GameStatusAnswer createGameStatusAnswer(int gameId, int playerId, int playerIdReceiver, Game<?> game) throws UnexpectedControllerException {
        return new GameStatusAnswer(gameId,
                playerId,
                playerIdReceiver,
                ConverterToLocalModel.convert(game, playerIdReceiver, gameId));
    }
}
