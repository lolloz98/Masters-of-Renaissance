package it.polimi.ingsw.model.game;

public enum Resource {
    NOTHING,
    SHIELD,
    GOLD,
    SERVANT,
    ROCK,
    TRANSFORM,
    FAITH,
    ANYTHING;

    public static boolean isDiscountable(Resource res) {
        return res == SHIELD || res == GOLD || res == SERVANT || res == ROCK;
    }
}
