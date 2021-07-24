package com.dunzo.assignment.coffeemachine.exceptions;

public class IngredientNotExistentException extends Exception {
    /**
     *  Ingredient part of the composition of a beverage is not part of the ingredient factory.
     * @param errorMessage
     */
    public IngredientNotExistentException(String errorMessage) {
        super(errorMessage);
    }
}
