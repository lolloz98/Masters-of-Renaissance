package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.enums.Resource;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Some pointers to images useful in multiple views.
 */
public final class ImageCache {
    public static final Image EMPTY_CARD = new Image("/png/empty_card.png");
    private static final Logger logger = LogManager.getLogger(FaithTrackComponent.class);

    public static final Image NOTHINGIMG = new Image("/png/res/no_res.png");
    public static final Image ROCKIMG = new Image("/png/res/rock.png");
    public static final Image SERVANTIMG = new Image("/png/res/servant.png");
    public static final Image GOLDIMG = new Image("/png/res/gold.png");
    public static final Image SHIELDIMG = new Image("/png/res/shield.png");
    public static final Image FAITHIMG = new Image("/png/res/faith.png");
    public static final Image ANYTHINGHIMG = new Image("/png/res/anything.png");
    public static final Image PLAYER = new Image("/png/punchboard/player.png");
    public static final Image LORENZO = new Image("/png/punchboard/cross.png");
    private static final Image LORENZO_AND_PLAYER = new Image("/png/punchboard/player_and_lorenzo.png");

    public static final Image VATICAN_ACTIVE_1 = new Image("/png/punchboard/vat_act_1.png");
    public static final Image VATICAN_ACTIVE_2 = new Image("/png/punchboard/vat_act_2.png");
    public static final Image VATICAN_ACTIVE_3 = new Image("/png/punchboard/vat_act_3.png");

    public static final Image NOTHINGMARBLE = new Image("png/punchboard/marbles/NOTHING.png");
    public static final Image ROCKMARBLE = new Image("png/punchboard/marbles/ROCK.png");
    public static final Image GOLDMARBLE = new Image("png/punchboard/marbles/GOLD.png");
    public static final Image SHIELDMARBLE = new Image("png/punchboard/marbles/SHIELD.png");
    public static final Image FAITHMARBLE = new Image("png/punchboard/marbles/FAITH.png");
    public static final Image SERVANTMARBLE = new Image("png/punchboard/marbles/SERVANT.png");

    private static boolean isSinglePlayer;

    public static boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public static void setIsSinglePlayer(boolean isSinglePlayer){
        ImageCache.isSinglePlayer = isSinglePlayer;
    }
    /**
     * @param r valid resource to be stored + Nothing
     * @param im image view which needs to display the image
     */
    public static void setImageInStore(Resource r, ImageView im){
        switch (r){
            case NOTHING:
                im.setImage(NOTHINGIMG);
                break;
            case SHIELD:
                im.setImage(SHIELDIMG);
                break;
            case GOLD:
                im.setImage(GOLDIMG);
                break;
            case SERVANT:
                im.setImage(SERVANTIMG);
                break;
            case ROCK:
                im.setImage(ROCKIMG);
                break;
            default:
                logger.error("Cannot store this type of res: " + r);
        }
    }

    /**
     * @param r valid marble resource
     * @param im image view which needs to display the image
     */
    public static void setMarbleInView(Resource r, ImageView im){
        switch (r){
            case NOTHING:
                im.setImage(NOTHINGMARBLE);
                break;
            case SHIELD:
                im.setImage(SHIELDMARBLE);
                break;
            case GOLD:
                im.setImage(GOLDMARBLE);
                break;
            case SERVANT:
                im.setImage(SERVANTMARBLE);
                break;
            case ROCK:
                im.setImage(ROCKMARBLE);
                break;
            case FAITH:
                im.setImage(FAITHMARBLE);
                break;
            default:
                logger.error("Cannot store this type of res: " + r);
        }
    }

    public static Image getLorenzoAndPlayer(){
        return (isSinglePlayer? LORENZO_AND_PLAYER: PLAYER);
    }
}
