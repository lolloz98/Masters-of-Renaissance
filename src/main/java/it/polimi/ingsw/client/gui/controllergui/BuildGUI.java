package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.creation.*;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.TreeMap;

public class BuildGUI {
    private static final Logger logger = LogManager.getLogger(BuildGUI.class);

    private static BuildGUI INSTANCE = null;

    private BuildGUI(){}

    public static BuildGUI getInstance(){
        if(INSTANCE != null) return INSTANCE;
        return INSTANCE = new BuildGUI();
    }

    public Scene newScene(Parent root, Stage stage){
        return new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
    }

    public void toStartScene(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start.fxml"));
            try {
                logger.debug("To start scene");
                Parent root = fxmlLoader.load();
                StartGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);

                stage.setTitle("Master of Renaissance");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toCreateGame(Stage stage, GUI ui){
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/create_game.fxml"));
            try {
                Parent root = fxmlLoader.load();
                CreateGameGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);
                stage.setScene(newScene(root, stage));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toStartLocal(Stage stage, GUI ui){
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start_local.fxml"));
            try {
                Parent root = fxmlLoader.load();
                StartLocalGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);
                stage.setScene(newScene(root, stage));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toStartRemote(Stage stage, GUI ui){
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start_remote.fxml"));
            try {
                Parent root = fxmlLoader.load();
                StartRemoteGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);
                stage.setScene(newScene(root, stage));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toBoard(Stage stage, GUI ui){
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                logger.debug("going in board");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/board2.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    BoardControllerGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }

    public void toWait(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wait_players.fxml"));
            try {
                Parent root = fxmlLoader.load();
                WaitForPlayersGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);
                stage.setScene(newScene(root, stage));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toJoinGame(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/join_game.fxml"));
            try {
                Parent root = fxmlLoader.load();
                JoinGameGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);
                stage.setScene(newScene(root, stage));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toJoinOrCreate(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/create_or_join.fxml"));
            try {
                Parent root = fxmlLoader.load();
                CreateOrJoinGUI controller = fxmlLoader.getController();
                controller.setUp(stage, root, ui);
                stage.setScene(newScene(root, stage));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toChooseInitRes(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_init_res.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    ChooseInitResGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }

    public void toRemoveLeaders(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/remove_leaders.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    RemoveLeadersGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }

    public void toMarket(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/market.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    MarketControllerGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }

    public void toFlushRes(Stage stage, GUI ui, TreeMap< Resource, Integer > resComb) {
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/market.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    FlushResControllerGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui, resComb);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }

    public void toDevelopGrid(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DevelopGrid/DevelopGrid.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    DevelopGridControllerGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }

    public void toActivateProduction(Stage stage, GUI ui, LocalCard card){
        logger.debug("to activate production scene");
    }

    public void toBuyDevelop(Stage stage, GUI ui, LocalDevelopCard toBuy) {
        Platform.runLater(() -> {
            synchronized(ui.getLocalGame()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DevelopGrid/BuyCardScene.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    BuyCardSceneControllerGUI controller = fxmlLoader.getController();
                    controller.setUp(stage, root, ui, toBuy);
                    stage.setScene(newScene(root, stage));
                    stage.show();
                } catch (IOException e) {
                    logger.error("file not found: " + e);
                }
            }
        });
    }
}
