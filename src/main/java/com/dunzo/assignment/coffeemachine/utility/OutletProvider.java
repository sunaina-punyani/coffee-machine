package com.dunzo.assignment.coffeemachine.utility;

import com.dunzo.assignment.coffeemachine.services.BeverageOutletService;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class OutletProvider {

    /**
     * This class is to only ensure per thread a new instance of BeverageOutletService(ProtoType Bean) is provided as that service maintains order assigned
     * @return
     */
    @Lookup
    public BeverageOutletService getBeverageOutlet() {
        return null;
    }
}
