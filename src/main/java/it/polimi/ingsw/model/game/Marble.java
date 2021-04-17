package it.polimi.ingsw.model.game;

public final class Marble {
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
