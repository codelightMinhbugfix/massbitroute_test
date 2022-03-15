package features.massbit_route;

import constants.Massbit_Route_Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Portal_Community_Gateway_Steps;
import utilities.DataCSV;
import utilities.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SerenityParameterizedRunner.class)
public class Portal_Community_Gateway {


    enum Zone{AS,EU,NA,SA,AF,OC}
    enum Gateway_status{CREATED,INSTALLED,VERIFED,APPROVED,STAKED}

    @TestData
    public static Collection<Object[]> testData() throws Exception {
        Object[][] data = DataCSV.getAllDataCSV("data/portal_gateway_info.csv");
        return Arrays.asList(data);
    }

    private String gateway_name;
    private String blockchain;
    private String network;

    public Portal_Community_Gateway(String gateway_name, String blockchain, String network){
        this.gateway_name = gateway_name;
        this.blockchain = blockchain;
        this.network = network;
    }

    @Steps
    Portal_Community_Gateway_Steps portal_community_gateway_steps;

    @Before
    public void prepareForTest() throws IOException, InterruptedException {

        Log.info("----------- Start Portal Community Gateway test ----------");

        portal_community_gateway_steps.should_be_able_to_login(Massbit_Route_Config.uname, Massbit_Route_Config.password);

    }

    @Test
    public void add_new_gateway_without_name(){
            portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway("",blockchain, Zone.AS.toString(), network);
    }

    @Test
    public void add_new_gateway_without_blockchain(){
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,"", Zone.EU.toString(), network);
    }

    @Test
    public void add_new_gateway_without_zone(){
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, "", network);
    }

    @Test
    public void add_new_gateway_without_network(){
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.NA.toString(), "");
    }

    @Test
    public void add_new_gateway_in_Asia_zone()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.AS.toString(), network)
                                      .create_vm_instance_and_register_gateway(portal_community_gateway_steps.get_install_gateway_script())
                                      .should_be_able_to_activate_gateway_successfully(portal_community_gateway_steps.get_gateway_id(), Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .destroy_vm_instance();
    }

    @Test
    public void add_new_gateway_in_Europe_zone()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.EU.toString(), network)
                                      .create_vm_instance_and_register_gateway(portal_community_gateway_steps.get_install_gateway_script())
                                      .should_be_able_to_activate_gateway_successfully(portal_community_gateway_steps.get_gateway_id(), Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .destroy_vm_instance();
    }

    @Test
    public void add_new_gateway_in_North_America_zone()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.NA.toString(), network)
                                      .create_vm_instance_and_register_gateway(portal_community_gateway_steps.get_install_gateway_script())
                                      .should_be_able_to_activate_gateway_successfully(portal_community_gateway_steps.get_gateway_id(), Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .destroy_vm_instance();
    }

    @Test
    public void add_new_gateway_in_South_America_zone()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.SA.toString(), network)
                                      .create_vm_instance_and_register_gateway(portal_community_gateway_steps.get_install_gateway_script())
                                      .should_be_able_to_activate_gateway_successfully(portal_community_gateway_steps.get_gateway_id(), Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .destroy_vm_instance();
    }

    @Test
    public void add_new_gateway_in_Africa_zone()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.AF.toString(), network)
                                      .create_vm_instance_and_register_gateway(portal_community_gateway_steps.get_install_gateway_script())
                                      .should_be_able_to_activate_gateway_successfully(portal_community_gateway_steps.get_gateway_id(), Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .destroy_vm_instance();
    }

    @Test
    public void add_new_gateway_in_Oceania_zone()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network)
                                      .create_vm_instance_and_register_gateway(portal_community_gateway_steps.get_install_gateway_script())
                                      .should_be_able_to_activate_gateway_successfully(portal_community_gateway_steps.get_gateway_id(), Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .destroy_vm_instance();
    }

    @Test
    public void get_portal_gateway_info()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network)
                                      .should_be_able_to_get_gateway_info(portal_community_gateway_steps.get_gateway_id());
    }

    @Test
    public void edit_portal_gateway_name()throws IOException, InterruptedException {
        String new_name = "new gateway name";
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network)
                                      .should_be_able_to_edit_gateway_name(portal_community_gateway_steps.get_gateway_id(), new_name);
    }

    @Test
    public void delete_portal_gateway()throws IOException, InterruptedException {
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network)
                                      .should_be_able_to_delete_gateway(portal_community_gateway_steps.get_gateway_id());
    }

    @Test
    public void get_my_portal_gateway_list()throws IOException, InterruptedException {

        Log.info("Start to get my portal gateway list");
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network);
        String id_1 = portal_community_gateway_steps.get_gateway_id();
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network);
        String id_2 = portal_community_gateway_steps.get_gateway_id();

        Response response = portal_community_gateway_steps.getMyGatewayList();
        String response_body = response.getBody().asString();

        List<String> list = JsonPath.from(response_body).getList("id");

        Assert.assertTrue(response.getStatusCode()==200);
        Assert.assertTrue(list.contains(id_1));
        Assert.assertTrue(list.contains(id_2));


        Log.highlight("Get my portal gateway list successfully");

    }

    @Test
    public void get_my_portal_gateway_by_created_status()throws IOException, InterruptedException {

        Log.info("Start to get my portal gateway by status");
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network);
        String id_1 = portal_community_gateway_steps.get_gateway_id();
        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network);
        String id_2 = portal_community_gateway_steps.get_gateway_id();

        portal_community_gateway_steps.should_be_able_to_login(Massbit_Route_Config.uname_2, Massbit_Route_Config.password_2);

        portal_community_gateway_steps.should_be_able_to_add_new_portal_gateway(gateway_name,blockchain, Zone.OC.toString(), network);
        String id_3 = portal_community_gateway_steps.get_gateway_id();

        Response response = portal_community_gateway_steps.getGatewayByStatus(Gateway_status.CREATED.toString().toLowerCase());
        String response_body = response.getBody().asString();

        List<String> list_id = JsonPath.from(response_body).getList("id");
        List<String> list_status = JsonPath.from(response_body).getList("status");

        Assert.assertTrue(response.getStatusCode()==200);
        Assert.assertTrue(list_id.size() == list_status.size());

        for(String status : list_status){
            if(!status.equalsIgnoreCase(Gateway_status.CREATED.toString())){
                Assert.assertTrue(false);
            }
        }
        Assert.assertTrue(list_id.contains(id_1));
        Assert.assertTrue(list_id.contains(id_2));
        Assert.assertTrue(list_id.contains(id_3));


        Log.highlight("Get my portal gateway by status successfully");

    }


//     Use to clear test data
    @Test
    public void deleteAllGateway(){
        Response response = portal_community_gateway_steps.getMyGatewayList();
        String response_body = response.getBody().asString();

        List<String> list_gw_id = JsonPath.from(response_body).getList("id");

        for(String id : list_gw_id){
            portal_community_gateway_steps.should_be_able_to_delete_gateway(id);
        }

        Log.highlight("Delete all gateway successfully");

    }

}
