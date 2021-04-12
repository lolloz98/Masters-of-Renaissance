package it.polimi.ingsw.model.game;

public final class Marble {
    private final Resource resource;

    public Marble(Resource resource){
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
