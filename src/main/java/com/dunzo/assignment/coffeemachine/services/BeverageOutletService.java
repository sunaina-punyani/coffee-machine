package com.dunzo.assignment.coffeemachine.services;

import com.dunzo.assignment.coffeemachine.accessors.IngredientFactory;
import com.dunzo.assignment.coffeemachine.exceptions.CompositionNotFoundException;
import com.dunzo.assignment.coffeemachine.models.BeverageOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.dunzo.assignment.coffeemachine.constants.SystemConstants.TIME_to_PREPARE_BEVERAGE;
import static java.lang.Thread.sleep;

/**
 * This is the runnable(outlet task) which will be associated to each thread, where we will have outlet number of threads.
 * This outlet will be assigned a BeverageOrder, upon its overrided run method being called on a thread, it will call the make beverage method
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BeverageOutletService implements Runnable{

    private final IngredientFactory ingredientFactory;
    private  BeverageOrder beverageOrder;

    /**
     * Only coupling the CoffeeBrewingService here so that outlet can update the number of beverages delivered, to be checked for
     * in the test cases
     */
    @Autowired
    private CoffeeBrewingService coffeeBrewingService;

    private static final Logger logger = LoggerFactory.getLogger(BeverageOutletService.class);

    public BeverageOutletService(IngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    /**
     * Assigns a beverage order to this outlet
     * @param beverageOrder
     */
    public void assignOrder(BeverageOrder beverageOrder){
        this.beverageOrder = beverageOrder;
    }

    /**
     *  Checks and reserves the ingredients of the beverage composition from the ingredient factory and then calls prepareBeverage method
     *  Does not reserve ingredients and does not call prepareBeverage, if all ingredients of the composition are not adequate
     * @param beverageName
     * @param composition
     * @throws CompositionNotFoundException
     * @throws InterruptedException
     */
    public void makeBeverage(String beverageName, Map<String, BigDecimal> composition) throws CompositionNotFoundException, InterruptedException {
        if(CollectionUtils.isEmpty(composition)){
            throw new CompositionNotFoundException("Composition not provided");
        }

        Map<String, BigDecimal> reservedIngredients = new HashMap<>();
        ingredientFactory.checkAndReserveIngredientStoreForRecipe(beverageName, composition, reservedIngredients);

        if(reservedIngredients.isEmpty())
            return;

        logger.info("{} is prepared", beverageName);
        prepareBeverage(beverageName, reservedIngredients);

    }

    /**
     *  the preparation logic of beverage should lie here, as current requirements consider all beverages need equal time,
     *  adding constant sleep
     * @param beverageName
     * @param reservedIngredients
     * @throws InterruptedException
     */
    private void prepareBeverage(String beverageName, Map<String, BigDecimal> reservedIngredients) throws InterruptedException {
        sleep(TIME_to_PREPARE_BEVERAGE);
        coffeeBrewingService.incrementNumberOfBeveragesDelivered();
    }

    /**
     * method that each thread will call
     */
    @Override
    public void run() {
        try{
            makeBeverage(beverageOrder.getBeverageName(), beverageOrder.getComposition());
        }catch (CompositionNotFoundException e){
            logger.error("Composition not found for - {}", beverageOrder.getBeverageName());
        } catch (InterruptedException e) {
            logger.error("Thread interrupted");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
