package com.dunzo.assignment.coffeemachine.services;

import com.dunzo.assignment.coffeemachine.accessors.IngredientFactory;
import com.dunzo.assignment.coffeemachine.exceptions.IngredientAlreadyPresentException;
import com.dunzo.assignment.coffeemachine.exceptions.IngredientNotExistentException;
import com.dunzo.assignment.coffeemachine.models.BeverageOrder;
import com.dunzo.assignment.coffeemachine.utility.OutletProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dunzo.assignment.coffeemachine.constants.SystemConstants.AWAIT_TIME_FOR_SUBMITTED_TASKS_POST_SHUTDOWN;

@Service
public class CoffeeBrewingService {

    private final OutletProvider outletProvider;
    private final IngredientFactory ingredientFactory;
    private final OrderService orderService;

    private ThreadPoolExecutor outletExecutor;

    private AtomicInteger numberOfBeveragesDelivered;

    private static final Logger logger = LoggerFactory.getLogger(CoffeeBrewingService.class);

    public CoffeeBrewingService(OutletProvider outletProvider, IngredientFactory ingredientFactory, OrderService orderService) {
        this.outletProvider = outletProvider;
        this.ingredientFactory = ingredientFactory;
        this.orderService = orderService;
    }

    /**
     *  Initializes the thread pool size with the outlet count
     *  Initializes the ingredient factory with ingredients
     *  Initializes th number of delivered beverages, maintained for test case purpose
     * @param outletCount
     * @param ingredientsQuantity
     * @return
     */
    public boolean initialize(Integer outletCount, Map<String, BigDecimal> ingredientsQuantity){
        try{
            numberOfBeveragesDelivered = new AtomicInteger(0);
            outletExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(outletCount);
            ingredientFactory.initializeIngredientFactory(ingredientsQuantity);
        }catch(IngredientAlreadyPresentException e){
            logger.error("Ingredients not added as duplicates found - {}", ingredientsQuantity);
            return false;
        }catch(IllegalArgumentException e){
            logger.error(" {} Number of outlets is not an acceptable input", outletCount);
            return false;
        }
        return true;
    }

    /**
     *  keeps placing one order per outlet, until the orders have exhausted, post that shutdown the thread pool executor
     */
    public void brew(){
        while(orderService.getPendingOrder() > 0){
            Optional<BeverageOrder> beverageOrder = orderService.getOrder();

            if(beverageOrder.isEmpty())
                return;

            BeverageOutletService beverageOutletService = outletProvider.getBeverageOutlet();
            beverageOutletService.assignOrder(beverageOrder.get());
            outletExecutor.submit(beverageOutletService);
        }
        shutDown();
        logger.info("Delivered {} beverages", numberOfBeveragesDelivered.get());

    }

    /**
     *  gracefully shuts down the thread pool executor
     */
    private void shutDown(){
        outletExecutor.shutdown();
        try {
            /**
             * Blocks until all tasks have completed execution after a shutdown request,
             * or the timeout occurs, or the current thread is interrupted, whichever happens first.
             * If you are giving as input a large number of tasks and outlets such that a lot of tasks could have been submitted before shutdown,
             * please increase timeout time in below method, from the SystemConstants class
             */
            outletExecutor.awaitTermination(AWAIT_TIME_FOR_SUBMITTED_TASKS_POST_SHUTDOWN, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     *  used in test cases
     * @return int
     */
    public int getNumberOfBeveragesDelivered() {
        return numberOfBeveragesDelivered.get();
    }

    /**
     *  increments the number of beverages delivered
     */
    public void incrementNumberOfBeveragesDelivered(){
        numberOfBeveragesDelivered.incrementAndGet();
    }

    /**
     *  Method to refill beverage ingredients in ingredient factory via coffee brewing service, max capacity is maintained inside ingredient model
     *  For simplicity refilling is currently treated as refilling to full amount
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
}
