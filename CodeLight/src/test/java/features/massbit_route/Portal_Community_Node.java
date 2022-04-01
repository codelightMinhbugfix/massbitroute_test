package features.massbit_route;

import constants.Massbit_Route_Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Portal_Community_Node_Steps;
import utilities.DataCSV;
import utilities.Log;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SerenityParameterizedRunner.class)
public class Portal_Community_Node {


    enum Zone{AS,EU,NA,SA,AF,OC}
    enum Node_status{CREATED,INSTALLED,VERIFED,APPROVED,STAKED}

    @TestData
    public static Collection<Object[]> testData() throws Exception {
        Object[][] data = DataCSV.getAllDataCSV("data/portal_node_info.csv");
        return Arrays.asList(data);
    }

    private String node_name;
    private String blockchain;
    private String network;

    public Portal_Community_Node(String node_name, String blockchain, String network){
        this.node_name = node_name;
        this.blockchain = blockchain;
        this.network = network;
    }

    @Steps
    Portal_Community_Node_Steps portal_community_node_steps;

    @Before
    public void prepareForTest() throws IOException, InterruptedException {

        Log.info("----------- Start Portal Community Node test ----------");

        portal_community_node_steps.should_be_able_to_login(Massbit_Route_Config.uname, Massbit_Route_Config.password);

    }

    @Test
    public void add_new_node_without_name(){
        portal_community_node_steps.should_be_able_to_add_new_node("",blockchain, Portal_Community_Gateway.Zone.AS.toString(), network);
    }

    @Test
    public void add_new_node_without_blockchain(){
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,"", Portal_Community_Gateway.Zone.EU.toString(), network);
    }

    @Test
    public void add_new_node_without_zone(){
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, "", network);
    }

    @Test
    public void add_new_node_without_network(){
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, Portal_Community_Gateway.Zone.NA.toString(), "");
    }

    @Test
    public void add_new_node_in_Asia_zone()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .create_vm_instance_and_register_node(portal_community_node_steps.get_install_node_script())
                                   .should_be_able_to_activate_node_successfully(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                   .destroy_vm_instance();
    }

    @Test
    public void add_new_node_in_Europe_zone()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.EU.toString())
                                   .create_vm_instance_and_register_node(portal_community_node_steps.get_install_node_script())
                                   .should_be_able_to_activate_node_successfully(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                   .destroy_vm_instance();
    }

    @Test
    public void add_new_node_in_North_America_zone()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.NA.toString())
                                   .create_vm_instance_and_register_node(portal_community_node_steps.get_install_node_script())
                                   .should_be_able_to_activate_node_successfully(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                   .destroy_vm_instance();
    }

    @Test
    public void add_new_node_in_South_America_zone()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.SA.toString())
                                   .create_vm_instance_and_register_node(portal_community_node_steps.get_install_node_script())
                                   .should_be_able_to_activate_node_successfully(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                   .destroy_vm_instance();
    }

    @Test
    public void add_new_node_in_Africa_zone()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AF.toString())
                                   .create_vm_instance_and_register_node(portal_community_node_steps.get_install_node_script())
                                   .should_be_able_to_activate_node_successfully(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                   .destroy_vm_instance();
    }

    @Test
    public void add_new_node_in_Oceania_zone()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.OC.toString())
                                   .create_vm_instance_and_register_node(portal_community_node_steps.get_install_node_script())
                                   .should_be_able_to_activate_node_successfully(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                   .destroy_vm_instance();
    }

    @Test
    public void get_node_info()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .should_be_able_to_get_node_info(portal_community_node_steps.getNode_id() );
    }

    @Test
    public void edit_node()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .should_be_able_to_edit_node(portal_community_node_steps.getNode_id(), Massbit_Route_Config.NEW_NAME, Massbit_Route_Config.NEW_DATA_SOURCE );
    }

    @Test
    public void delete_node()throws IOException, InterruptedException {
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .should_be_able_to_delete_node(portal_community_node_steps.getNode_id());
    }

    @Test
    public void get_my_portal_node_list()throws IOException, InterruptedException {

        Log.info("Start to get my portal node list");
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString());
        String id_1 = portal_community_node_steps.getNode_id();

        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString());
        String id_2 = portal_community_node_steps.getNode_id();

        HttpResponse response = portal_community_node_steps.getMyNodeList();
        String response_body = response.body().toString();

        List<String> list = JsonPath.from(response_body).getList("id");

        Assert.assertTrue(response.statusCode()==200);
        Assert.assertTrue(list.contains(id_1));
        Assert.assertTrue(list.contains(id_2));


        Log.highlight("Get my portal node list successfully");

    }

    @Test
    public void get_my_portal_node_by_created_status()throws IOException, InterruptedException {

        Log.info("Start to get my portal node by status");
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString());
        String id_1 = portal_community_node_steps.getNode_id();

        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString());
        String id_2 = portal_community_node_steps.getNode_id();

        portal_community_node_steps.should_be_able_to_login(Massbit_Route_Config.uname_2, Massbit_Route_Config.password_2);

        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString());
        String id_3 = portal_community_node_steps.getNode_id();

        Response response = portal_community_node_steps.getNodeByStatus(Node_status.CREATED.toString().toLowerCase());
        String response_body = response.getBody().asString();

        List<String> list_id = JsonPath.from(response_body).getList("id");
        List<String> list_status = JsonPath.from(response_body).getList("status");

        Assert.assertTrue(response.getStatusCode()==200);
        Assert.assertTrue(list_id.size() == list_status.size());

        for(String status : list_status){
            if(!status.equalsIgnoreCase(Node_status.CREATED.toString())){
                Assert.assertTrue(false);
            }
        }
        Assert.assertTrue(list_id.contains(id_1));
        Assert.assertTrue(list_id.contains(id_2));
        Assert.assertTrue(list_id.contains(id_3));


        Log.highlight("Get my portal node by status successfully");

    }

    //APIs are called in install script to register node

    @Test
    public void get_geo(){
        Log.info("Start to add geo of my portal node");
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .should_be_able_to_get_geo(portal_community_node_steps.getNode_id());
    }

    @Test
    public void get_register(){
        Log.info("Start to register portal node");
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .should_be_able_to_get_register(portal_community_node_steps.getNode_id());
    }

    @Test
    public void get_verify(){
        Log.info("Start to verify portal node");
        portal_community_node_steps.should_be_able_to_add_new_node(node_name,blockchain, network, Zone.AS.toString())
                                   .should_be_able_to_get_verify(portal_community_node_steps.getNode_id());
    }

    @Test
    public void clear_all_test_data() throws IOException, InterruptedException {
        portal_community_node_steps.delete_all_node();
    }

}
