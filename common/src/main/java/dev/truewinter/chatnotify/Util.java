package dev.truewinter.chatnotify;

/**
 * Internal utility class
 * @hidden
 */
public class Util {
    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public static boolean intToBoolean(int integer) {
        return integer == 1;
    }
}
