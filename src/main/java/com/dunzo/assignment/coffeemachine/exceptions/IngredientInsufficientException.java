package com.dunzo.assignment.coffeemachine.exceptions;

public class IngredientInsufficientException extends Exception{
    /**
     *
     * @param errorMessage
     */
    public IngredientInsufficientException(String errorMessage) {
        super(errorMessage);
    }
}
