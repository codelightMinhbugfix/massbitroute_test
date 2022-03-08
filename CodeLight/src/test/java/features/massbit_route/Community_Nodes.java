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

    enum Zone{AS,EU,NA,SA,AF,OC}

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
    public void prepareForTest() throws IOException, InterruptedException {
        Log.highlight("Java version:" + System.getProperty("java.version"));
        UtilSteps.runCommand("terraform --version");
        community_nodes_steps.should_be_able_to_say_hello()
                             .should_be_able_to_login();
    }

    @Steps
    Community_Nodes_Steps community_nodes_steps;

    @Test
    public void add_new_node_in_Asia_zone()throws IOException, InterruptedException {
        community_nodes_steps.should_be_able_to_add_new_node(gateway_name, blockchain, Zone.AS.toString(), network)
                             .create_vm_instance_and_register_node(community_nodes_steps.get_install_node_script())
                             .should_be_able_to_activate_node_successfully()
                             .destroy_vm_instance();

    }

}
