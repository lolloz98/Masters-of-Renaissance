package it.polimi.ingsw.server;

/**
 * Gets the location where should be the folder in which to retrieve or save the games.
 */
public class PersistenceDirectoryLocator {
    private static final String appName = "/MastersOfRenaissance_CarpanetoDeSantisInnocenti/";
    private static String dir = "";
    public static String getDir(){
        if(dir.equals("")){
            String workingDirectory;
            String OS = (System.getProperty("os.name")).toUpperCase();
            if (OS.contains("WIN"))
            {
                // it is simply the location of the "AppData" folder
                workingDirectory = System.getenv("APPDATA") + "/Local";
            }
            else if(OS.contains("MAC"))
            {
                //in either case, we would start in the user's home directory
                workingDirectory = System.getProperty("user.home");
                //if we are on a Mac, we are not done, we look for "Application Support"
                workingDirectory += "/Library/Application Support";
            }else{
                workingDirectory = System.getProperty("user.home");
                workingDirectory += "/.local";
            }
            return dir = workingDirectory + appName + "tmp";
        }
        else{
            return dir;
        }
    }
}
