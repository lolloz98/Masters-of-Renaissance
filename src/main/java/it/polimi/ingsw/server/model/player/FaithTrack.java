package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.server.model.exception.EndAlreadyReachedException;
import it.polimi.ingsw.server.model.exception.FigureAlreadyActivatedException;
import it.polimi.ingsw.server.model.exception.FigureAlreadyDiscardedException;
import it.polimi.ingsw.server.model.exception.InvalidStepsException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * class that models the faith path of each player (including Lorenzo)
 */

public class FaithTrack implements VictoryPointCalculator {
    private static final Logger logger = LogManager.getLogger(FaithTrack.class);

    private final VaticanFigure[] figures;
    private int position;

    public FaithTrack() {
        this.position = 0;
        this.figures = new VaticanFigure[3];
        this.figures[0] = new VaticanFigure(2);
        this.figures[1] = new VaticanFigure(3);
        this.figures[2] = new VaticanFigure(4);
    }

    public int getPosition() {
        return position;
    }

    /**
     * @return a deep copy of VaticanFigure[]
     */
    public VaticanFigure[] getFigures() {
        VaticanFigure[] vaticanFigureCopy = new VaticanFigure[3];
        for(int i=0; i<3; i++) {
            vaticanFigureCopy[i] = new VaticanFigure(figures[i].getLevel());
            try {
                // we are just doing a copy: no need for checking for exceptions
                vaticanFigureCopy[i].setState(figures[i].getState());
            } catch (FigureAlreadyDiscardedException | FigureAlreadyActivatedException e) {
                logger.error("error happened while getting figures");
            }
        }
        return vaticanFigureCopy;
    }

    @Override
    public int getVictoryPoints() {
        int points = 0;
        for (int i = 0; i < 3; i++) {
            if (figures[i].isActive())
                points += figures[i].getLevel();
        }
        if (position < 3)
            return points;
        else if (position < 6)
            return points + 1;
        else if (position < 9)
            return points + 2;
        else if (position < 12)
            return points + 4;
        else if (position < 15)
            return points + 6;
        else if (position < 18)
            return points + 9;
        else if (position < 21)
            return points + 12;
        else if (position < 24)
            return points + 16;
        else if (position == 24)
            return points + 20;
        return points;
    }

    /**
     * method that moves the player through the path and handle the vaticanfigures activation
     *
     * @param steps number of steps to move the player on the track
     * @throws EndAlreadyReachedException if the end is already reached
     * @throws InvalidStepsException if the steps are negative, or zero
     */
    public void move(int steps, Game<?> game) throws EndAlreadyReachedException, InvalidStepsException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        if (isEndReached()) throw new EndAlreadyReachedException();
        advance(steps);
        ArrayList<Integer> checkpointnumber = whichCheckpointIsReached(steps);
        if (!checkpointnumber.isEmpty()) {
            for (Integer integer : checkpointnumber) {
                if (amiTheFirst(integer)) {
                    if (game instanceof MultiPlayer)
                        checkpointHandling((MultiPlayer) game, integer);
                    else
                        checkpointHandling((SinglePlayer) game, integer);
                }
            }

        }
    }

    /**
     * method that activate the VaticanFigures of the player that has the rights (in a single player game)
     */
    private void checkpointHandling( SinglePlayer game, int checkpointnumber) throws FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        if (game.getLorenzo().getFaithTrack().hasRights(checkpointnumber))
            game.getLorenzo().getFaithTrack().activateVatican(checkpointnumber);
        else
            game.getLorenzo().getFaithTrack().discardVatican(checkpointnumber);
        if (game.getPlayer().getBoard().getFaithtrack().hasRights(checkpointnumber))
            game.getPlayer().getBoard().getFaithtrack().activateVatican(checkpointnumber);
        else
            game.getPlayer().getBoard().getFaithtrack().discardVatican(checkpointnumber);
    }


    /**
     * method that activate the VaticanFigures of the player that has the rights (in a multi player game)
     */
    private void checkpointHandling( MultiPlayer game, int checkpointnumber) throws FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        for (Player p :
                game.getPlayers()) {
            if (p.getBoard().getFaithtrack().hasRights(checkpointnumber))
                p.getBoard().getFaithtrack().activateVatican(checkpointnumber);
            else
                p.getBoard().getFaithtrack().discardVatican(checkpointnumber);
        }
    }


    /**
     * method that advance the piece of n-steps
     *
     * @param n number of steps forward
     * @throws InvalidStepsException if n is negative
     */
    private void advance(int n) throws InvalidStepsException {
        if (n <= 0) throw new InvalidStepsException();
        if (position + n <= 24)
            this.position += n;
        else
            this.position = 24;
    }

    public boolean isEndReached() {
        return position == 24;
    }


    /**
     * method that checks if the player has reached the checkpoint
     *
     * @return -1 if no checkpoint is reached, or returns the number of the checkpoint reached
     */
    private ArrayList<Integer> whichCheckpointIsReached(int steps) {
        int oldposition = position - steps;
        ArrayList<Integer> checks;
        checks = new ArrayList<>();
        if (oldposition < 8 && position >= 8)
            checks.add(1);
        if (oldposition < 16 && position >= 16)
            checks.add(2);
        if (oldposition < 24 && position >= 24)
            checks.add(3);
        return checks;
    }

    /**
     * method that checks if the player has the rights to activate the vaticanfigure
     *
     * @param checkpointnumber is the number of the checkpoint which i'm analyzing
     */
    private boolean hasRights(int checkpointnumber) {
        switch (checkpointnumber) {
            case 1: {
                if (this.position >= 5)
                    return true;
            }

            case 2: {
                if (this.position >= 12)
                    return true;
            }

            case 3: {
                if (this.position >= 19)
                    return true;
            }

            default:
                return false;
        }
    }


    /**
     * method that checks if the current player is the first that has reached the checkpoint
     *
     * @param whichcheckpoint is the number of the checkpoint to control
     */
    private boolean amiTheFirst(int whichcheckpoint) {
        return figures[whichcheckpoint - 1].isInactive();
    }

    /**
     * method that activates the vatican figure
     */
    private void activateVatican(int whichvf) throws FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        this.figures[whichvf - 1].activate();
    }

    /**
     * method that discards the vatican figure
     */
    private void discardVatican(int whichvf) throws FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        this.figures[whichvf - 1].discard();
    }

}
