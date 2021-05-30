package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.NoSuchGameException;
import it.polimi.ingsw.server.model.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Persist {
    private static final Logger logger = LogManager.getLogger(Persist.class);

    private final String PATH = "tmp/game";
    private final String EXTENSION = ".tmp";
    private static Persist INSTANCE = null;

    private Persist() {}

    public static Persist getInstance() {
        if (INSTANCE == null) return INSTANCE = new Persist();
        else return INSTANCE;
    }

    public void persist(Game<?> game, int gameId) throws IOException {
        String path = PATH + gameId + EXTENSION;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (IOException e) {
            logger.error("Exception happened while writing a file: " + e);
        }
    }

    public Game<?> retrieve(int gameId) throws NoSuchGameException, UnexpectedControllerException {
        String path = PATH + gameId + EXTENSION;
        return retrieve(path);
    }

    public Game<?> retrieve(String path) throws NoSuchGameException, UnexpectedControllerException {
        try{
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Game<?> game = (Game<?>) objectInputStream.readObject();
            fileInputStream.close();
            return game;
        } catch (FileNotFoundException e) {
            logger.info("File not found for path: " + path);
            throw new NoSuchGameException("No game found with given gameId");
        } catch (IOException | ClassNotFoundException e) {
            logger.warn(e.getClass().getSimpleName() + " while retrieving a game: " + e.getMessage());
            throw new UnexpectedControllerException("error while retrieving the game");
        }
    }

    /**
     * Delete file with game
     * @param gameId id of game to be deleted
     */
    public void remove(int gameId){
        try {
            Files.deleteIfExists(Paths.get(PATH + gameId + EXTENSION));
        } catch (IOException e) {
            logger.warn("Delete if exist threw an exception: " + e);
        }
    }
}
