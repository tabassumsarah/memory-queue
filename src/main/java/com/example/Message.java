package com.example;

import java.util.UUID;

/**
 * Message pojo, contains information about the message i.e visibility status, uniqueIdentifier.
 * This unique id can be used later for doing special processing.
 */

public class Message {

    private String message;
    private Boolean isVisible;
    private String uniqueIdentifier;

    Message() {
        //todo remove this when exception handling is done properly.
    }

    Message(String message) {

        this.message = message;
        this.uniqueIdentifier  = UUID.randomUUID().toString();
    }

    String getUniqueIdentifier() {

        return uniqueIdentifier;
    }

    String getMessage() {

        return this.message;
    }

    void setMessage(String message) {

        this.message = message;
    }

    Boolean getVisible() {

        return isVisible;
    }

    void setVisible(Boolean visible) {

        isVisible = visible;
    }
}
