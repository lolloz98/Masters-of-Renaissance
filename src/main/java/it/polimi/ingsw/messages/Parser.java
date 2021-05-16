package it.polimi.ingsw.messages;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.ParserServer;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Parser {
    private static final Logger logger = LogManager.getLogger(Parser.class);

    public static Object parse(Object object, String packageName, String suffix) throws ParserException {
        Reflections reflections = new Reflections(packageName);
        Set<String> allClasses = reflections.getStore().values("SubTypesScanner");

        List<String> targetClasses = allClasses.stream().filter(x -> x.contains(packageName)).collect(Collectors.toList());
        List<String> targetClassName =  targetClasses.stream().filter(x -> x.endsWith(object.getClass().getSimpleName() + suffix)).collect(Collectors.toList());
        if(targetClassName.size() != 1) {
            logger.error("for the " + object.getClass().getSimpleName() + " we got " + targetClassName.size() + " target classes corresponding");
            throw new ParserException("unable to parse " + object.getClass().getSimpleName());
        }

        try{
            Class<?> targetClass = Class.forName(targetClassName.get(0));
            Constructor<?> constructor = targetClass.getConstructor(object.getClass());
            return constructor.newInstance(object);
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new ParserException("Unable to parse: " + e);
        }
    }
}
