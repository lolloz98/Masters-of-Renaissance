package it.polimi.ingsw.client.localmodel.localcards;

public class LocalConcealedCard extends LocalCard {
    private static final long serialVersionUID = 3L;

    /**
     * Tell if the concealed leader card is discarded/removed or not.
     */
    private boolean isDiscarded = false;

    public LocalConcealedCard() {
        super(0);
    }

    public LocalConcealedCard(boolean isDiscarded) {
        this();
        this.isDiscarded = isDiscarded;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    public void setDiscarded(boolean discarded) {
        this.isDiscarded = discarded;
    }
}
