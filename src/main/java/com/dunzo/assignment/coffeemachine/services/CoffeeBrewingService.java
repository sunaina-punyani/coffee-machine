package com.dunzo.assignment.coffeemachine.services;

import com.dunzo.assignment.coffeemachine.accessors.IngredientFactory;
import com.dunzo.assignment.coffeemachine.exceptions.IngredientAlreadyPresentException;
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
     *
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
     *
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
     *
     */
    private void shutDown(){
        outletExecutor.shutdown();
        try {
            outletExecutor.awaitTermination(8, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     *
     * @return
     */
    public int getNumberOfBeveragesDelivered() {
        return numberOfBeveragesDelivered.get();
    }

    /**
     *
     */
    public void incrementNumberOfBeveragesDelivered(){
        numberOfBeveragesDelivered.incrementAndGet();
    }
}
