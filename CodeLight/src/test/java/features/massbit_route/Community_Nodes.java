package features.massbit_route;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.jruby.RubyProcess;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.UtilSteps;
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
    private String network;

    public Community_Nodes(String gateway_name, String blockchain, String zone, String network){
        this.gateway_name = gateway_name;
        this.blockchain = blockchain;
        this.zone = zone;
        this.network = network;
    }

    @Before
    public void prepareForTest() throws IOException {
        Log.highlight("Java version:" + System.getProperty("java.version"));
        UtilSteps.runCommand("terraform --version");
    }

    @Steps
    Community_Nodes_Steps community_nodes_steps;

    @Test
    public void massbit_route_node_testing() throws IOException, InterruptedException {

        Log.info("----------- Start Community Nodes test -----------");

        community_nodes_steps.should_be_able_to_say_hello()
                             .should_be_able_to_login()
                             .should_be_able_to_add_new_node(gateway_name, blockchain, zone, network);

        String installScript = "echo yes|" + community_nodes_steps.get_install_node_script();
        Log.highlight("install script: " + installScript);

//        UtilSteps.runCommand("ls");
//        UtilSteps.runCommand("cd terraform_node");
//        UtilSteps.runCommand("cd terraform_node");

        UtilSteps.writeToFile("terraform_node/init.sh", installScript);

        Thread.sleep(1000);

        UtilSteps.runCommand("terraform_node/cmTerraformApply.sh");

        community_nodes_steps.should_be_able_to_activate_node_successfully();

        Thread.sleep(4000);

        UtilSteps.runCommand("terraform_node/cmTerraformDestroy.sh");

        Thread.sleep(10000);

        Log.highlight("Destroy VM instance successfully");

    }
}
