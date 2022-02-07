package features.massbit_route;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Community_Nodes_Steps;
import steps.api_massbit_route.Gateway_Community_Steps;
import utilities.DataCSV;
import utilities.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(SerenityParameterizedRunner.class)
public class Community_Nodes {

    @TestData
    public static Collection<Object[]> testData() throws Exception {
        Object[][] data = DataCSV.getAllDataCSV("data/nodes_info.csv");
        return Arrays.asList(data);
    }

    private String gateway_name;
    private String blockchain;
    private String zone;
    private String data_url;
    private String network;

    public Community_Nodes(String gateway_name, String blockchain, String zone, String data_url, String network){
        this.gateway_name = gateway_name;
        this.blockchain = blockchain;
        this.zone = zone;
        this.data_url = data_url;
        this.network = network;
    }

    @Before
    public void prepareForTest(){

    }

    @Steps
    Community_Nodes_Steps community_nodes_steps;

    @Test
    public void massbit_route_node_testing() throws IOException, InterruptedException {

        Log.info("----------- Start Community Nodes test ----------");

        community_nodes_steps.should_be_able_to_say_hello()
                             .should_be_able_to_login()
                             .should_be_able_to_add_new_node(gateway_name, blockchain, zone, data_url, network);

        Log.highlight("install script: " + community_nodes_steps.get_install_node_script());

    }
}
