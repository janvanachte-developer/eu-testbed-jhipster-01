package eu.europa.ec.digit.bris.testbed.cucumber.stepdefs;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = TestbedApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
