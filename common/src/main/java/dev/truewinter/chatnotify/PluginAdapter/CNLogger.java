package dev.truewinter.chatnotify.PluginAdapter;

/**
 * A cross-platform logger interface
 */
public interface CNLogger {
    /**
     * Logs an informational string
     * @param message the string to log
     */
    void info(String message);

    /**
     * Logs a warning
     * @param message the warning
     */
    void warn(String message);

    /**
     * Logs a string as an error
     * @param message the message
     */
    void error(String message);

    /**
     * Logs an error
     * @param message a message briefly explaining the error
     * @param exception the exception
     */
    void error(String message, Exception exception);
}
