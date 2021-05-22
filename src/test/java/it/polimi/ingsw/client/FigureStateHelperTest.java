package it.polimi.ingsw.client;

import it.polimi.ingsw.client.localmodel.LocalFigureState;
import it.polimi.ingsw.server.model.player.FigureState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * helper class for testing
 * @param <T>
 */
public class FigureStateHelperTest<T extends LocalFigureState> {
    private T figureState;

    public FigureStateHelperTest(LocalFigureState figureState){
        this.figureState= (T) figureState;
    }

    private static final List<LocalFigureState> VALUES =
            Collections.unmodifiableList(Arrays.asList(LocalFigureState.values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     *
     * @return a random value of LocalFigureState
     */
    public static LocalFigureState randomFigureState()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
