package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;

public class LocalTurnSingle extends LocalTurn implements Serializable {
    private static final long serialVersionUID = 22L;

    public LocalTurnSingle(){}
    public LocalTurnSingle(boolean mainActionOccurred, boolean productionsActivated, boolean marketActivated){
        super(mainActionOccurred, productionsActivated, marketActivated);
    }

}
