package it.polimi.ingsw.enums;

public enum Resource {
    NOTHING,
    SHIELD,
    GOLD,
    SERVANT,
    ROCK,
    FAITH,
    ANYTHING;

    public static boolean isDiscountable(Resource res) {
        return res == SHIELD || res == GOLD || res == SERVANT || res == ROCK;
    }
}
