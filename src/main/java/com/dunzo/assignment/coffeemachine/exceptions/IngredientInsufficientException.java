package com.dunzo.assignment.coffeemachine.exceptions;

public class IngredientInsufficientException extends Exception{
    /**
     *  Ingredient is insufficient to prepare the required beverage, handled case, adding an Exception to track
     * @param errorMessage
     */
    public IngredientInsufficientException(String errorMessage) {
        super(errorMessage);
    }
}
