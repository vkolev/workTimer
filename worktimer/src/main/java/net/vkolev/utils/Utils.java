package net.vkolev.utils;

/**
 * Created by vlado on 08.12.15.
 */
public class Utils {

    private static Utils instance = null;

    protected Utils() {}

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public static String getAppDirectory() {
        return System.getProperty("user.home");
    }
}
