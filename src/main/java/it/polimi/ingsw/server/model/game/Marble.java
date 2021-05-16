package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.enums.Resource;

import java.io.Serializable;

public final class Marble implements Serializable {
    private static final long serialVersionUID = 1018L;

    private final Resource resource;

    public Marble(Resource resource){
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Marble) return getResource() == ((Marble) obj).getResource();
        else if(obj instanceof Resource) return getResource() == obj;
        return false;
    }
}
