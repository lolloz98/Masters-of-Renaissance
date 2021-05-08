package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.cards.leader.DiscountLeaderCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

public final class Parser {
    private static final Logger logger = LogManager.getLogger(Parser.class);
    private static final String packageName = "it.polimi.ingsw.server.controller.messagesctr";

    public static Object parse(ClientMessage clientMessage) throws ControllerException{
        // Get all classes in the package with controller Requests
        Reflections reflections = new Reflections(packageName);
        Set<String> allClasses = reflections.getStore().values("SubTypesScanner");

        List<String> controllerClasses = allClasses.stream().filter(x -> x.contains(packageName)).collect(Collectors.toList());
        List<String> controllerClassName =  controllerClasses.stream().filter(x -> x.endsWith(clientMessage.getClass().getSimpleName() + "Controller")).collect(Collectors.toList());
        if(controllerClassName.size() != 1) {
            logger.error("for the clientMessage we got " + controllerClassName.size() + " controllerMessages corresponding");
            throw new ControllerException("unable to parse the request");
        }

        try{
            Class<?> controllerClass = Class.forName(controllerClassName.get(0));
            Constructor<?> constructor = controllerClass.getConstructor(clientMessage.getClass());
            return constructor.newInstance(clientMessage);
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new ControllerException("Unable to parse the request");
        }
    }
}
