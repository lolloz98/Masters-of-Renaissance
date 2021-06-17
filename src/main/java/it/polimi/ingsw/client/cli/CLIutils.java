package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;

/**
 * Class that contains useful parameters and methods for the CLI
 */
public class CLIutils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String GREY_BACKGROUND = "\033[0;100m";
    public static final String BLACK_BACKGROUND = "\033[40m";
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String MAGENTA_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String WHITE_BACKGROUND = "\033[47m";

    public static final String GREY = "\033[90m";
    public static final String BLACK = "\033[30m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";

    /**
     * appends a string to string[index]
     *
     * @param strings        block to which the string must be appended to
     * @param index          of the list to which the string must be appended
     * @param stringToAppend string to append
     */
    public static void append(ArrayList<String> strings, int index, String stringToAppend) {
        strings.set(index, strings.get(index) + stringToAppend);
    }

    /**
     * appends a block of strings to another block of strings,
     * the first string will be appended to strings[index], the second to strings[index+1], and so on
     *
     * @param strings       block to which the string must be appended to
     * @param index         of the list to which the string must be appended
     * @param blockToAppend string to append
     */
    public static void append(ArrayList<String> strings, int index, ArrayList<String> blockToAppend) {
        for (int i = 0; i < blockToAppend.size(); i++)
            strings.set(index + i, strings.get(index + i) + blockToAppend.get(i));
    }

    /**
     * appends spaces to a block of strings
     *
     * @param strings the block of string to append spaces to
     * @param index   of the list to which the string must be appended
     * @param number  of spaces to append
     */
    public static void appendSpaces(ArrayList<String> strings, int index, int number) {
        for (int i = 0; i < number; i++) append(strings, index, " ");
    }

    /**
     * @return the ansi color corresponding to a color
     */
    public static String colorToAnsi(Color color) {
        switch (color) {
            case BLUE:
                return ANSI_BLUE;
            case GOLD:
                return ANSI_YELLOW;
            case PURPLE:
                return ANSI_PURPLE;
            case GREEN:
                return ANSI_GREEN;
        }
        return " ";
    }

    /**
     * @return the ansi color corresponding to a resource, for the marbles
     */
    public static String resourceToAnsiMarble(Resource res) {
        switch (res) {
            case NOTHING:
            case ANYTHING:
                return WHITE;
            case GOLD:
                return YELLOW;
            case SERVANT:
                return MAGENTA;
            case SHIELD:
                return CYAN;
            case ROCK:
                return GREY;
            case FAITH:
                return RED;
        }
        return " ";
    }

    /**
     * @return the ansi color corresponding to a resource, for the cards
     */
    public static String resourceToAnsi(Resource res) {
        switch (res) {
            case NOTHING:
            case ANYTHING:
                return WHITE_BACKGROUND + ANSI_BLACK;
            case GOLD:
                return YELLOW_BACKGROUND + ANSI_BLACK;
            case SERVANT:
                return MAGENTA_BACKGROUND + ANSI_BLACK;
            case SHIELD:
                return CYAN_BACKGROUND + ANSI_BLACK;
            case ROCK:
                return GREY_BACKGROUND + ANSI_BLACK;
            case FAITH:
                return RED_BACKGROUND + ANSI_BLACK;
        }
        return " ";
    }

    public static void printResList() {
        System.out.println("1. Shield");
        System.out.println("2. Gold");
        System.out.println("3. Servant");
        System.out.println("4. Rock");
    }

    public static void printWarehouseList() {
        System.out.println("1. Normal depot");
        System.out.println("2. Leader depot");
        System.out.println("3. Strongbox");
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.print(CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE);
    }

    /**
     * prints the list of strings
     */
    public static void printBlock(ArrayList<String> out) {
        for (String o : out) {
            System.out.println(o);
        }
    }

}
