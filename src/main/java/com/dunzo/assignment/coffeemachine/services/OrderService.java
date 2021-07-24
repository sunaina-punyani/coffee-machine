package com.dunzo.assignment.coffeemachine.services;

import com.dunzo.assignment.coffeemachine.models.BeverageOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    private final List<BeverageOrder> beverageOrderList;

    public OrderService() {
        this.beverageOrderList = new LinkedList<>();
    }

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     *
     * @param orders
     */
    public void initialize(Map<String, Map<String, BigDecimal>> orders){
        if(CollectionUtils.isEmpty(orders))
            return;
        for(Map.Entry<String, Map<String, BigDecimal>> orderEntry : orders.entrySet()){
            if(CollectionUtils.isEmpty(orderEntry.getValue())){
                logger.error("Composition not found for - {}, not accepting order", orderEntry.getKey());
                continue;
            }
            beverageOrderList.add(BeverageOrder.builder()
                    .beverageName(orderEntry.getKey())
                    .composition(orderEntry.getValue())
                    .build());
        }
    }

    /**
     *
     * @return
     */
    public Integer getPendingOrder(){
        return beverageOrderList.size();
    }

    /**
     *
     * @return
     */
    public  Optional<BeverageOrder> getOrder(){
        if(CollectionUtils.isEmpty(beverageOrderList)){
            return Optional.empty();
        }
        BeverageOrder beverageOrder = beverageOrderList.get(0);
        beverageOrderList.remove(0);
        return Optional.of(beverageOrder);
    }

}
