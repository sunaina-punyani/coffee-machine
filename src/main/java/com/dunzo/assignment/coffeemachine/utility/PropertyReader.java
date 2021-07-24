package com.dunzo.assignment.coffeemachine.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class to read and load properties from application.properties file"
 */
@Component
public class PropertyReader {

    @Value("${inputPath}")
    private String inputPath;

    public String getInputPath() { return inputPath; }

    @Value("${input_case_1}")
    private String inputFirstCase;

    @Value("${input_case_2}")
    private String inputSecondCase;

    @Value("${input_case_3}")
    private String inputThirdCase;

    @Value("${input_case_4}")
    private String inputFourthCase;

    @Value("${input_case_5}")
    private String inputFifthCase;

    @Value("${input_case_6}")
    private String inputSixthCase;

    public String getInputSixthCase() { return inputSixthCase; }

    public String getInputFifthCase() {
        return inputFifthCase;
    }

    public String getInputFirstCase() {
        return inputFirstCase;
    }

    public String getInputSecondCase() {
        return inputSecondCase;
    }

    public String getInputThirdCase() {
        return inputThirdCase;
    }

    public String getInputFourthCase() {
        return inputFourthCase;
    }
}
