package steps.api_massbit_route;

import constants.Massbit_Route_Config;
import constants.Massbit_Route_Endpoint;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;
import steps.UtilSteps;
import utilities.Log;

import java.io.IOException;

public class Portal_Community_Node_Steps {


    public static String access_token = "";
    public static String node_info = "";

    @Steps
    private UtilSteps utilSteps;

    public Response login(String uname, String password){

        String body = "{\n" +
                "  \"username\": \"" + uname + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.PORTAL_LOGIN);

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_login(String uname, String password){

        Response response = login(uname, password);
        String response_body = response.getBody().asString();
        Log.info("Response of login " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertFalse(JsonPath.from(response_body).getString("accessToken").isEmpty());

        access_token = "Bearer " + JsonPath.from(response_body).getString("accessToken");

        Log.highlight("Login with username " + uname + " successfully");
        return this;
    }

    public Response add_new_node(String name, String blockchain, String network, String zone){

        String data_url = "";

        switch (blockchain){
            case "eth":
                data_url = Massbit_Route_Config.DATA_URL_ETHEREUM;
                break;
            case "near":
                data_url = Massbit_Route_Config.DATA_URL_NEAR;
                break;
            case "hmny":
                data_url = Massbit_Route_Config.DATA_URL_HARMONY;
                break;
            case "dot":
                data_url = Massbit_Route_Config.DATA_URL_POLKADOT;
                break;
            case "avax":
                data_url = Massbit_Route_Config.DATA_URL_AVALANCHE;
                break;
            case "ftm":
                data_url = Massbit_Route_Config.DATA_URL_FANTOM;
                break;
            case "matic":
                data_url = Massbit_Route_Config.DATA_URL_POLYGON;
                break;
            case "bsc":
                data_url = Massbit_Route_Config.DATA_URL_BSC;
                break;
            case "sol":
                data_url = Massbit_Route_Config.DATA_URL_SOLANA;
                break;

        }


        Log.info("data_source: " + data_url);
        String body = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"blockchain\": \"" + blockchain + "\",\n" +
                "  \"network\": \"" + network + "\",\n" +
                "  \"zone\": \"" + zone + "\",\n" +
                "  \"dataSource\": \"" + data_url + "\"\n" +
                "}";

        Log.info("url: " + utilSteps.getPortalURL()+ Massbit_Route_Endpoint.ADD_NEW_NODE);
        Log.info("body: " + body);
        Log.info("access token: " + access_token);

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.ADD_NEW_NODE);

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_add_new_node(String name, String blockchain, String network, String zone){

        Response response = add_new_node(name, blockchain, network, zone);
        String response_body = response.getBody().asString();
        Log.info("Response of add new portal node: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertEquals(JsonPath.from(response_body).getString("name"), name);
        Assert.assertEquals(JsonPath.from(response_body).getString("blockchain"), blockchain);
        Assert.assertEquals(JsonPath.from(response_body).getString("network"), network);
        Assert.assertEquals(JsonPath.from(response_body).getString("zone"), zone);
        Assert.assertEquals(JsonPath.from(response_body).getString("status"), "created");
        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("userId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("appKey").isEmpty());

        node_info = response_body;
        Log.highlight("Add new portal node successfully");
        return this;
    }

    public String get_install_node_script(){

        String cmd_start = "sudo bash -c \"$(curl -sSfL '";
        String url = utilSteps.getAPIURL() + "/v1/node_install?";
        String id = "id=" + JsonPath.from(node_info.toString()).getString("id");
        String user_id = "&user_id=" + JsonPath.from(node_info.toString()).getString("userId");
        String blockchain = "&blockchain=" + JsonPath.from(node_info.toString()).getString("blockchain");
        String network = "&network=" + JsonPath.from(node_info.toString()).getString("network");
        String zone = "&zone=" + JsonPath.from(node_info.toString()).getString("zone");
        String data_url = "&data_url=" + JsonPath.from(node_info.toString()).getString("dataSource");
        String appKey = "&appKey=" + JsonPath.from(node_info.toString()).getString("appKey");
        String cmd_end = "')\"";

        String install_script = "echo yes|" + cmd_start + url + id + user_id + blockchain + network + zone + data_url + appKey + "&portal_url=" + Massbit_Route_Config.portal_url + cmd_end;

        Log.highlight("install script to register node: " + install_script);

        return install_script;
    }

    public Portal_Community_Node_Steps create_vm_instance_and_register_node(String installScript) throws InterruptedException, IOException {

        UtilSteps.writeToFile(Massbit_Route_Config.PORTAL_NODE_PATH_TERRAFORM_INIT, installScript);

        Thread.sleep(1000);

        UtilSteps.runCommand(Massbit_Route_Config.PORTAL_NODE_PATH_TERRAFORM_APPLY);

        return this;
    }

    public Portal_Community_Node_Steps destroy_vm_instance() throws InterruptedException, IOException {

        Thread.sleep(4000);
        UtilSteps.runCommand(Massbit_Route_Config.PORTAL_NODE_PATH_TERRAFORM_DESTROY);

        Thread.sleep(10000);

        Log.highlight("Destroy VM instance of portal node successfully");

        return this;
    }

    public Response edit_node(String id, String new_name, String new_dataSource){

        String body = "{\n" +
                "  \"name\": \"" + new_name + "\",\n" +
                "  \"dataSource\": \"" + new_dataSource + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .put(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.EDIT_NODE + id);

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_edit_node(String id, String new_name, String new_dataSource){

        Response response = edit_node(id, new_name, new_dataSource);
        String response_body = response.getBody().asString();
        Log.info("Response of edit node: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("name"), new_name);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"), id);
        Assert.assertEquals(JsonPath.from(response_body).getString("dataSource"), new_dataSource);

        Log.highlight("Edit node successfully");
        return this;
    }

    public Response delete_node(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .delete(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.DELETE_NODE + id);

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_delete_node(String id){

        Response response = delete_node(id);
        String response_body = response.getBody().asString();
        Log.info("Response of delete node: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("status"));

        Log.highlight("Delete node successfully");
        return this;
    }

    public Response getNodeByStatus(String status){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_NODE_BY_STATUS + "?status=" + status);

        return response;
    }

    // Not verify yet
    @Step
    public Portal_Community_Node_Steps should_be_able_to_get_node_by_status(String status){

        Response response = getNodeByStatus(status);
        String response_body = response.getBody().asString();
        Log.info("Response of get node by status: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);

        Log.highlight("Get node by status successfully");
        return this;
    }

    public Response getMyNodeList(){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_NODE_LIST);

        return response;
    }

    // Not verify yet
    @Step
    public Portal_Community_Node_Steps should_be_able_to_get_my_node_list(){

        Response response = getMyNodeList();
        String response_body = response.getBody().asString();
        Log.info("Response of get my node list: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);

        Log.highlight("Get my node list successfully");
        return this;
    }

    public Response getNodeInfo(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_NODE_INFO + id);

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_get_node_info(String id){

        Response response = getNodeInfo(id);
        String response_body = response.getBody().asString();
        Log.info("Response of get node info: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"),id);

        Log.highlight("Get node info successfully");
        return this;
    }

}
