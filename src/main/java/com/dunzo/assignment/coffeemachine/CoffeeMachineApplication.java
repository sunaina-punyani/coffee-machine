package com.dunzo.assignment.coffeemachine;

import com.dunzo.assignment.coffeemachine.input.MachineDetails;
import com.dunzo.assignment.coffeemachine.services.CoffeeBrewingService;
import com.dunzo.assignment.coffeemachine.services.OrderService;
import com.dunzo.assignment.coffeemachine.utility.InputReader;
import com.dunzo.assignment.coffeemachine.utility.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Optional;


@SpringBootApplication
public class CoffeeMachineApplication implements ApplicationRunner {

	@Autowired
	private CoffeeBrewingService coffeeBrewingService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private InputReader inputReader;

	private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CoffeeMachineApplication.class, args);
	}

	/**
	 *
	 * @param args
	 */
	@Override
	public void run(ApplicationArguments args){

		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputPath());

		if(machineDetailsOptional.isEmpty()) {
			logger.error("Parsing input failed, exiting!");
			return;
		}
		MachineDetails machineDetails = machineDetailsOptional.get();
		if(!coffeeBrewingService.initialize(machineDetails.getOutlets().getOutletCount(), machineDetails.getTotalItemsQuantityMap())){
			logger.error("Coffee brewing service could not be initialized, exiting!");
			return;
		}
		orderService.initialize(machineDetails.getBeverageCompositionsMap());
		coffeeBrewingService.brew();
		logger.info("Orders finished, Yay!");

	}
}
