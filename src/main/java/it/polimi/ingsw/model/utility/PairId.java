package it.polimi.ingsw.model.utility;

public class PairId<T, U> {
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
}