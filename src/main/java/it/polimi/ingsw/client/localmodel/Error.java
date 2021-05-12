package it.polimi.ingsw.client.localmodel;

public class Error extends Observable{
    private ErrorType type;

    public synchronized ErrorType getType() {
        return type;
    }

    public synchronized void setType(ErrorType type) {
        this.type = type;
        notifyObserver();
    }

    public Error(){
        type = ErrorType.NONE;
    }
}
