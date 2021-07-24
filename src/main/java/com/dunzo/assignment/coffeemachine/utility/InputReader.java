package com.dunzo.assignment.coffeemachine.utility;

import com.dunzo.assignment.coffeemachine.CoffeeMachineApplication;
import com.dunzo.assignment.coffeemachine.input.MachineDetails;
import com.dunzo.assignment.coffeemachine.input.SystemDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InputReader {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineApplication.class);

    /**
     *
     * @param inputPath
     * @return
     */
    public Optional<MachineDetails> getInputData(String inputPath){
        ObjectMapper objectMapper = new ObjectMapper();
        SystemDetails systemDetails = null;
        MachineDetails machineDetails = null;
        try {
            //System.out.println("ip:" +inputPath);
            systemDetails = objectMapper.readValue(getClass().getResource(inputPath), SystemDetails.class);
            //logger.info(systemDetails.toString());
            machineDetails = systemDetails.getMachineDetails();
        }catch(Exception e){
            e.printStackTrace();//TODO: comment this!!
            logger.error("Parsing input failed");
            return Optional.empty();
        }
        return Optional.of(machineDetails);
    }
}
