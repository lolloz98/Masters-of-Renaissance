package it.polimi.ingsw.client.localmodel;

public enum ErrorType {
    NONE("No errors."),
    MISSING_GAME("This game id doesn't belong to a game in this server."),
    GAME_ALREADY_STARTED("This id belongs to an ongoing game. ");

    public final String errMsg;

    ErrorType(String errMsg) {
        this.errMsg = errMsg;
    }
}
