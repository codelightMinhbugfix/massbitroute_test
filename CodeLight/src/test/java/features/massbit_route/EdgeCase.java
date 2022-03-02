package features.massbit_route;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Decentralized_API_Steps;

import java.io.IOException;

@RunWith(SerenityRunner.class)
public class EdgeCase {

    @Steps
    Decentralized_API_Steps decentralized_api_steps;

    @Before
    public void prepareForTest() throws IOException, InterruptedException {
        decentralized_api_steps.should_be_able_to_say_hello();
        decentralized_api_steps.should_be_able_to_login();
    }



}
