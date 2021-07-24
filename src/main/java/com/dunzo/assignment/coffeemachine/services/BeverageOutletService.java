package com.dunzo.assignment.coffeemachine.services;

import com.dunzo.assignment.coffeemachine.accessors.IngredientFactory;
import com.dunzo.assignment.coffeemachine.exceptions.CompositionNotFoundException;
import com.dunzo.assignment.coffeemachine.exceptions.IngredientNotExistentException;
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

import static java.lang.Thread.sleep;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BeverageOutletService implements Runnable{

    private final IngredientFactory ingredientFactory;
    private  BeverageOrder beverageOrder;

    @Autowired
    private CoffeeBrewingService coffeeBrewingService;

    private static final Logger logger = LoggerFactory.getLogger(BeverageOutletService.class);

    public BeverageOutletService(IngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    public void assignOrder(BeverageOrder beverageOrder){
        this.beverageOrder = beverageOrder;
    }

    /**
     *
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

        prepareBeverage(beverageName, reservedIngredients);

    }

    //refill ingredients quantity wise

    /**
     *
     * @param ingredientName
     */
    public void refillIngredient(String ingredientName){
        try{
            ingredientFactory.refillIngredient(ingredientName);
        }catch(IngredientNotExistentException e){
            logger.error("Ingredient does not exist");
        }
    }

    /**
     *
     * @param beverageName
     * @param reservedIngredients
     * @throws InterruptedException
     */
    private void prepareBeverage(String beverageName, Map<String, BigDecimal> reservedIngredients) throws InterruptedException {
        logger.info("{} is prepared", beverageName);
        sleep((long) (Math.random()*10));//TODO: change to constant
        coffeeBrewingService.incrementNumberOfBeveragesDelivered();
    }

    /**
     *
     */
    @Override
    public void run() {
        System.out.println(this);
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
