package features.massbit_route;


import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Gateway_Community_Steps;
import utilities.DataCSV;
import utilities.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(SerenityParameterizedRunner.class)
public class Community_Gateway {

    @TestData
    public static Collection<Object[]> testData() throws Exception {
        Object[][] data = DataCSV.getAllDataCSV("data/gateway_info.csv");
        return Arrays.asList(data);
    }

    private String gateway_name;
    private String blockchain;
    private String zone;
    private String network;

    public Community_Gateway(String gateway_name, String blockchain, String zone, String network){
        this.gateway_name = gateway_name;
        this.blockchain = blockchain;
        this.zone = zone;
        this.network = network;
    }

    @Before
    public void prepareForTest(){

    }

    @Steps
    Gateway_Community_Steps gateway_community_steps;

    @Test
    public void massbit_route_gateway_testing() throws IOException, InterruptedException {

        Log.info("----------- Start Community Gateway test ----------");

        gateway_community_steps.should_be_able_to_say_hello()
                               .should_be_able_to_login()
                               .should_be_able_to_add_new_gateway(gateway_name, blockchain, zone, network);
        Log.highlight("install script: " + gateway_community_steps.get_install_gateway_script());

    }

}
