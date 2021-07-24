package com.dunzo.assignment.coffeemachine.utility;

import com.dunzo.assignment.coffeemachine.services.BeverageOutletService;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class OutletProvider {

    /**
     *
     * @return
     */
    @Lookup
    public BeverageOutletService getBeverageOutlet() {
        return null;
    }
}
