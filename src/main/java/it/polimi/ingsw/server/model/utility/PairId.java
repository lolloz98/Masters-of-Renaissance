package it.polimi.ingsw.server.model.utility;

import java.io.Serializable;

public class PairId<T, U> implements Serializable {
    private static final long serialVersionUID = 2000L;

    private final T t;
    private final U u;

    public PairId(T t, U u) {
        this.t= t;
        this.u= u;
    }

    public U getSecond() {
        return u;
    }

    public T getFirst() {
        return t;
    }

    @Override
    public String toString() {
        return  t +
                ", " + u;
    }
}