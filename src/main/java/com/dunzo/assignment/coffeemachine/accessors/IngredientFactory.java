package com.dunzo.assignment.coffeemachine.accessors;

import com.dunzo.assignment.coffeemachine.constants.BeverageConstants;
import com.dunzo.assignment.coffeemachine.exceptions.IngredientAlreadyPresentException;
import com.dunzo.assignment.coffeemachine.exceptions.IngredientNotExistentException;
import com.dunzo.assignment.coffeemachine.models.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Ingredient Factory (Singleton) will be the single gateway for all services to access, use or add ingredients in the coffee machine
 */
@Component
public class IngredientFactory {

    private Map<String, Ingredient> ingredientStorageMap;

    private static final Logger logger = LoggerFactory.getLogger(IngredientFactory.class);

    /**
     * refill the ingredient to max value
     * @param ingredientName - ingredient to be refilled
     * @throws IngredientNotExistentException - thrown when given ingredient in composition is not part of ingredient factory
     */
    public void refillIngredient(String ingredientName) throws IngredientNotExistentException {
        if(!ingredientStorageMap.containsKey(ingredientName)){
            throw new IngredientNotExistentException("Ingredient does not exist");
        }

        Ingredient ingredient = ingredientStorageMap.get(ingredientName);
        ingredient.setAvailableQuantity(ingredient.getMaxQuantity());
    }

    /**
     * This method will check if the ingredient factory has enough amount of ingredients to serve this beverage,
     * using the beverage composition provided by the user. If ingredients suffice, it will reserve it for this beverage.
     * This method will be accessed by multiple threads and each will try reserving(using) the ingredients from ingredient
     * factory hence the method is made synchronized
     * @param beverageName - beverage ordered
     * @param composition - ingredient proportion mentioned
     * @param reservedIngredients - ingredients with proportions reserved for this beverage
     */
    public synchronized void checkAndReserveIngredientStoreForRecipe(String beverageName, Map<String, BigDecimal> composition, Map<String, BigDecimal> reservedIngredients){
        for (Map.Entry<String, BigDecimal> ingredientEntry : composition.entrySet()) {
            if(!ingredientStorageMap.containsKey(ingredientEntry.getKey())){
                logger.info("{} cannot be prepared because {} is not available", beverageName, ingredientEntry.getKey());
                return;
            }
            Ingredient ingredient = ingredientStorageMap.get(ingredientEntry.getKey());
            if(ingredient.getAvailableQuantity().compareTo(ingredientEntry.getValue()) < 0){
                logger.info("{} cannot be prepared because {} is not sufficient", beverageName, ingredientEntry.getKey());
                return;
            }
        }

        for (Map.Entry<String, BigDecimal> ingredientEntry : composition.entrySet()) {
            Ingredient ingredient = ingredientStorageMap.get(ingredientEntry.getKey());
            ingredient.setAvailableQuantity(ingredient.getAvailableQuantity().subtract(ingredientEntry.getValue()));
            reservedIngredients.put(ingredientEntry.getKey(), ingredientEntry.getValue());
        }
    }

    /**
     *
     * @param ingredientQuantityMap
     * @throws IngredientAlreadyPresentException
     */
    public void initializeIngredientFactory(Map<String, BigDecimal> ingredientQuantityMap) throws IngredientAlreadyPresentException {
        ingredientStorageMap = new ConcurrentHashMap<>();
        for(Map.Entry<String, BigDecimal> ingredientEntry : ingredientQuantityMap.entrySet()){
            if(ingredientStorageMap.containsKey(ingredientEntry.getKey())){
                throw new IngredientAlreadyPresentException("Ingredient is already present in storage");
            }
            Ingredient ingredient = Ingredient.builder()
                    .name(ingredientEntry.getKey())
                    .maxQuantity(ingredientEntry.getValue())
                    .availableQuantity(ingredientEntry.getValue())
                    .thresholdPercent(BigDecimal.valueOf(BeverageConstants.THRESHOLD_PERCENTAGE_QUANTITY))
                    .isRunningLow(false)
                    .build();
            ingredientStorageMap.put(ingredientEntry.getKey(), ingredient);
        }
    }

    /**
     *
     * @param ingredientName
     * @return boolean
     * @throws IngredientNotExistentException
     */
    public boolean isIngredientRunningLow(String ingredientName) throws IngredientNotExistentException {
        if(!ingredientStorageMap.containsKey(ingredientName)){
            throw new IngredientNotExistentException("Ingredient does not exist");
        }
        return ingredientStorageMap.get(ingredientName).getIsRunningLow();
    }
}
