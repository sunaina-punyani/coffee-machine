package com.dunzo.assignment.coffeemachine.exceptions;

public class CompositionNotFoundException extends Exception{
    /**
     * Beverage ordered without the composition is currently treated as incomplete input for simplicity,
     * can maintain a default composition per beverage in future.
     * @param errorMessage
     */
    public CompositionNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
