package com.dunzo.assignment.coffeemachine.exceptions;

public class IngredientAlreadyPresentException extends Exception{
    /**
     *  Ingredient has already been added in max capacity, can't accept more, this exception will
     *  never happen here as we are reading from a hash map, so duplicate insertions would have been overwritten, but writing this to track
     *  an edge case for future if hash map based storage is changed to models instead.
     * @param errorMessage
     */
    public IngredientAlreadyPresentException(String errorMessage) {
        super(errorMessage);
    }
}
