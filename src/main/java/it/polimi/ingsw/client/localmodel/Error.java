package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.cli.Observer;

import java.io.Serializable;

public class Error implements Serializable {
    private ErrorType type;
    private Observer observer;

    public synchronized ErrorType getType() {
        return type;
    }

    public synchronized void setType(ErrorType type) {
        this.type = type;
        if(type != ErrorType.NONE){
            observer.notifyError();
        }
    }
    public void addObserver(Observer observer){
        this.observer = observer;
    }

    public void removeObserver(){
        this.observer = null;
    }

    public Error(){
        type = ErrorType.NONE;
    }
}
