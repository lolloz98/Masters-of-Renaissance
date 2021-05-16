package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;

public class LocalTurnSingle extends LocalTurn implements Serializable {

    public LocalTurnSingle(){}
    public LocalTurnSingle(boolean mainActionOccurred, boolean productionsActivated, boolean marketActivated){
        super(mainActionOccurred, productionsActivated, marketActivated);
    }

}
