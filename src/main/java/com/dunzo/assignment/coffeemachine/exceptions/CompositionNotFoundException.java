package com.dunzo.assignment.coffeemachine.exceptions;

public class CompositionNotFoundException extends Exception{
    /**
     *
     * @param errorMessage
     */
    public CompositionNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
