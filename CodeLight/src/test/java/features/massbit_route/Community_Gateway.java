package features.massbit_route;


import constants.Massbit_Route_Config;
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

    enum Zone{AS,EU,NA,SA,AF,OC}

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
    public void prepareForTest() throws IOException, InterruptedException {

        Log.info("----------- Start Community Gateway test ----------");

        gateway_community_steps.should_be_able_to_say_hello();
        gateway_community_steps.should_be_able_to_login();

    }

//    @Test
//    public void add_new_gateway_without_name(){
//        gateway_community_steps.should_be_able_to_add_new_gateway("",blockchain, zone, network);
//    }
//
//    @Test
//    public void add_new_gateway_without_blockchain(){
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,"", zone, network);
//    }
//
//    @Test
//    public void add_new_gateway_without_zone(){
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, "", network);
//    }
//
//    @Test
//    public void add_new_gateway_without_network(){
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, zone, "");
//    }
//
//    @Test
//    public void add_new_gateway_in_Asia_zone()throws IOException, InterruptedException {
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, Zone.AS.toString(), network)
//                               .create_vm_instance_and_register_gateway(gateway_community_steps.get_install_gateway_script())
//                               .should_be_able_to_activate_gateway_successfully()
//                               .destroy_vm_instance();
//    }
//
//    @Test
//    public void add_new_gateway_in_Europe_zone()throws IOException, InterruptedException {
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, Zone.EU.toString(), network)
//                               .create_vm_instance_and_register_gateway(gateway_community_steps.get_install_gateway_script())
//                               .should_be_able_to_activate_gateway_successfully()
//                               .destroy_vm_instance();
//    }
//
//    @Test
//    public void add_new_gateway_in_North_America_zone()throws IOException, InterruptedException {
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, Zone.NA.toString(), network)
//                               .create_vm_instance_and_register_gateway(gateway_community_steps.get_install_gateway_script())
//                               .should_be_able_to_activate_gateway_successfully()
//                               .destroy_vm_instance();
//    }
//
//    @Test
//    public void add_new_gateway_in_South_America_zone()throws IOException, InterruptedException {
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, Zone.SA.toString(), network)
//                               .create_vm_instance_and_register_gateway(gateway_community_steps.get_install_gateway_script())
//                               .should_be_able_to_activate_gateway_successfully()
//                               .destroy_vm_instance();
//    }
//
//    @Test
//    public void add_new_gateway_in_Africa_zone()throws IOException, InterruptedException {
//        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, Zone.AF.toString(), network)
//                               .create_vm_instance_and_register_gateway(gateway_community_steps.get_install_gateway_script())
//                               .should_be_able_to_activate_gateway_successfully()
//                               .destroy_vm_instance();
//    }

    @Test
    public void add_new_gateway_in_Oceania_zone()throws IOException, InterruptedException {
        gateway_community_steps.should_be_able_to_add_new_gateway(gateway_name,blockchain, Zone.OC.toString(), network)
                               .create_vm_instance_and_register_gateway(gateway_community_steps.get_install_gateway_script())
                               .should_be_able_to_activate_gateway_successfully()
                               .destroy_vm_instance();
    }

}
