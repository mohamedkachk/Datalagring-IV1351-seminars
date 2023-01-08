
package se.kth.iv1351.jdbc.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Lists all available instruments of specified kind.
     */
    LIST,
    /**
     * Terminates an ongoing rental by instrument's id.
     */
    TERMINATE,
    /**
     * Lists all commands.
     */
    HELP,
    /**
     * Leave the application.
     */
    QUIT,
    /**
     * Creates a new rental for an instrument, a student and period of time.
     */
    RENT,
    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND
}