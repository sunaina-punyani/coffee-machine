package com.dunzo.assignment.coffeemachine.exceptions;

public class IngredientAlreadyPresentException extends Exception{
    /**
     *
     * @param errorMessage
     */
    public IngredientAlreadyPresentException(String errorMessage) {
        super(errorMessage);
    }
}
