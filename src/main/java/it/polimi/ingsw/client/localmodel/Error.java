package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.cli.Observer;

import java.io.Serializable;

public class Error implements Serializable {
    private static final long serialVersionUID = 9L;

    private String errorMessage;
    private Observer observer;

    public synchronized String getErrorMessage() {
        return errorMessage;
    }

    public synchronized void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if(observer != null)
            observer.notifyError();
    }
    public void addObserver(Observer observer){
        this.observer = observer;
    }

    public void removeObserver(){
        this.observer = null;
    }

    public Error(){

    }
}
