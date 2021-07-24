package com.dunzo.assignment.coffeemachine;

import com.dunzo.assignment.coffeemachine.input.MachineDetails;
import com.dunzo.assignment.coffeemachine.input.SystemDetails;
import com.dunzo.assignment.coffeemachine.services.CoffeeBrewingService;
import com.dunzo.assignment.coffeemachine.services.OrderService;
import com.dunzo.assignment.coffeemachine.utility.InputReader;
import com.dunzo.assignment.coffeemachine.utility.PropertyReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
class CoffeeMachineApplicationTests {

	@MockBean
	private CoffeeMachineApplication coffeeMachineApplication;

	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private CoffeeBrewingService coffeeBrewingService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private InputReader inputReader;

	private ObjectMapper objectMapper;
	private SystemDetails systemDetails;
	private String inputPath;
	private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineApplication.class);


	@Test
	public void ZeroOutletsProvidedScenarioTest() {
		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputSecondCase());

		if(machineDetailsOptional.isEmpty()) {
			logger.error("Parsing input failed, exiting!");
			return;
		}
		MachineDetails machineDetails = machineDetailsOptional.get();
		if(!coffeeBrewingService.initialize(machineDetails.getOutlets().getOutletCount(), machineDetails.getTotalItemsQuantityMap())){
			logger.error("Coffee brewing service could not be initialized, exiting!");
			Assert.assertEquals(coffeeBrewingService.getNumberOfBeveragesDelivered(), 0);
			return;
		}
		orderService.initialize(machineDetails.getBeverageCompositionsMap());
		coffeeBrewingService.brew();
		logger.info("Orders finished, Yay!");

	}

	@Test
	public void NoIngredientsProvidedScenarioTest() {
		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputThirdCase());

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
		Assert.assertEquals(coffeeBrewingService.getNumberOfBeveragesDelivered(), 0);
	}

	@Test
	public void NoOrdersProvidedScenarioTest() {
		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputFourthCase());

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
		Assert.assertEquals(coffeeBrewingService.getNumberOfBeveragesDelivered(), 0);
	}

	@Test
	public void NoCompositionProvidedScenarioTest() {
		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputFirstCase());

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
		Assert.assertEquals(coffeeBrewingService.getNumberOfBeveragesDelivered(), 2);
	}

	@Test
	public void IngredientNotExistentScenarioTest() {
		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputFifthCase());

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
		Assert.assertEquals(coffeeBrewingService.getNumberOfBeveragesDelivered(), 0);
	}

	@Test
	public void OutletsLessThanOrdersScenarioTest() {
		logger.info("WELCOME!");

		Optional<MachineDetails> machineDetailsOptional = inputReader.getInputData(propertyReader.getInputSixthCase());

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
		Assert.assertEquals(coffeeBrewingService.getNumberOfBeveragesDelivered(), 2);
	}


}
