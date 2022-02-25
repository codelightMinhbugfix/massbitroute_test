package features.massbit_route;


import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.UtilSteps;
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

    @Steps
    Gateway_Community_Steps gateway_community_steps;

    @Before
    public void prepareForTest(){


    }


    @Test
    public void massbit_route_gateway_testing() throws IOException, InterruptedException {

        Log.info("----------- Start Community Gateway test ----------");

        gateway_community_steps.should_be_able_to_say_hello();
        gateway_community_steps.should_be_able_to_login();
//        gateway_community_steps.listGateway();
        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name, blockchain, zone, network);

        String installScript = "echo yes|" + gateway_community_steps.get_install_gateway_script();
        Log.highlight("install script: " + installScript);

        UtilSteps.writeToFile("terraform_gateway/init.sh", installScript);

        Thread.sleep(1000);

        UtilSteps.runCommand("terraform_gateway/cmTerraformApply.sh");

        gateway_community_steps.should_be_able_to_activate_gateway_successfully();

        Thread.sleep(4000);
//
        UtilSteps.runCommand("terraform_gateway/cmTerraformDestroy.sh");

        Thread.sleep(10000);

        Log.highlight("Destroy VM instance successfully");

    }

}
