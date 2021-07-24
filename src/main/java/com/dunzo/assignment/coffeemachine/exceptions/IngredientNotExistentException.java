package com.dunzo.assignment.coffeemachine.exceptions;

public class IngredientNotExistentException extends Exception {
    /**
     *
     * @param errorMessage
     */
    public IngredientNotExistentException(String errorMessage) {
        super(errorMessage);
    }
}
