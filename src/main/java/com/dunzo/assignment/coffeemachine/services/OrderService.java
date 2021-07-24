package com.dunzo.assignment.coffeemachine.services;

import com.dunzo.assignment.coffeemachine.models.BeverageOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * The service to manage the beverage orders for the coffee machine, it queues the orders and exposes methods to fetch orders
 * and get pending order count.
 * Uses a Queue to service orders in sequence
 */
@Service
public class OrderService {

    private final Queue<BeverageOrder> beverageOrderQueue;

    public OrderService() {
        this.beverageOrderQueue = new LinkedList<>();
    }

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     *  register orders with the order service, for simplicity assuming correct orders will have their ingredient composition along
     * @param orders
     */
    public void enqueueOrders(Map<String, Map<String, BigDecimal>> orders){
        if(CollectionUtils.isEmpty(orders))
            return;
        for(Map.Entry<String, Map<String, BigDecimal>> orderEntry : orders.entrySet()){
            if(CollectionUtils.isEmpty(orderEntry.getValue())){
                logger.error("Composition not found for - {}, not accepting order", orderEntry.getKey());
                continue;
            }
            beverageOrderQueue.add(BeverageOrder.builder()
                    .beverageName(orderEntry.getKey())
                    .composition(orderEntry.getValue())
                    .build());
        }
    }

    /**
     * returning number of pending orders, as reading an order more or less will cause no harm because we are only using
     * pending orders to check if we can stop teh brewing. And upon fetching orders, queue already being empty case is handled
     * @return
     */
    public Integer getPendingOrder(){
        return beverageOrderQueue.size();
    }

    /**
     *  fetch an order to be serviced
     * @return
     */
    public  Optional<BeverageOrder> getOrder(){
        if(CollectionUtils.isEmpty(beverageOrderQueue)){
            return Optional.empty();
        }
        BeverageOrder beverageOrder = beverageOrderQueue.peek();
        beverageOrderQueue.poll();
        return Optional.of(beverageOrder);
    }

}
