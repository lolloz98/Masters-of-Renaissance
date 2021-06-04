package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.Observable;
import javafx.scene.image.Image;
import java.io.File;
import java.util.Objects;

public class LocalCard extends Observable {
    protected final int id;
    protected Image image;

    public int getId() {
        return id;
    }

    public LocalCard(int id) {
        this.id = id;
        this .image = null;
    }

    public Image getImage(){
        if (image == null) {
            this.image = new Image(String.format("/png/cards_front/%03d.png", id));
        }
        return image;
    }
}
