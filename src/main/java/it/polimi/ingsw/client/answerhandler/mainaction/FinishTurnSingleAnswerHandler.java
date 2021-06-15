package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnSingleAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Answer Handler that handles the end of a turn, in a single-player, modifying the local game.
 */
public class FinishTurnSingleAnswerHandler extends AnswerHandler {

    private static final Logger logger = LogManager.getLogger(FinishTurnSingleAnswerHandler.class);


    public FinishTurnSingleAnswerHandler(FinishTurnSingleAnswer answer) {
        super(answer);
    }

    /**
     * method that updates the local game after a finish turn request.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FinishTurnSingleAnswer serverAnswer = (FinishTurnSingleAnswer) getAnswer();

        if (localGame instanceof LocalSingle) {
            LocalSingle localSingle = (LocalSingle) localGame;
            LocalBoard localBoard = localSingle.getMainPlayer().getLocalBoard();

            localGame.getLocalTurn().setMainActionOccurred(false);
            String turnDescription;
            switch (serverAnswer.getLorenzoCard()) {
                case FAITH:
                    turnDescription = "Lorenzo gained 2 faith points";
                    break;
                case RESHUFFLE:
                    turnDescription = "Lorenzo gained 1 faith point";
                    break;
                case DISCARD_BLUE:
                    turnDescription = "Lorenzo discarded 2 blue development cards";
                    break;
                case DISCARD_GOLD:
                    turnDescription = "Lorenzo discarded 2 gold development cards";
                    break;
                case DISCARD_GREEN:
                    turnDescription = "Lorenzo discarded 2 green development cards";
                    break;
                case DISCARD_PURPLE:
                    turnDescription = "Lorenzo discarded 2 purple development cards";
                    break;
                default:
                    turnDescription = "";
            }
            localGame.getLocalTurn().getHistoryObservable().getHistory().add(turnDescription);

            //update the grid
            localSingle.setLocalDevelopmentGrid(serverAnswer.getLocalGrid());
            localSingle.getLocalDevelopmentGrid().notifyObservers();

            //update lorenzo's track
            localSingle.getLorenzoTrack().setFaithTrackScore(serverAnswer.getLocalLorenzoTrack().getFaithTrackScore());

            //update player's track
            localBoard.setLocalTrack(serverAnswer.getLocalPlayerTrack());


            localBoard.notifyObservers();
            localGame.getLocalTurn().notifyObservers();
            localGame.getLocalTurn().getHistoryObservable().notifyObservers();

        } else
            logger.error("the answer is for a single player game and " + logger.getName() + " has been sent to a multiplayer player");

    }
}
