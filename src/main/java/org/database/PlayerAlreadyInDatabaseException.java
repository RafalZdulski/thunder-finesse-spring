package org.database;

public class PlayerAlreadyInDatabaseException extends Exception {
    public PlayerAlreadyInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
