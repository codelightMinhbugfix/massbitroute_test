package steps.api_massbit_route;

import constants.Massbit_Route_Config;
import constants.Massbit_Route_Endpoint;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Portal_Community_Node_Steps {


    public static String access_token = "";
    public static String node_info = "";
    public static String node_id = "";

    @Steps
    private UtilSteps utilSteps;

    public Response login(String uname, String password){

        Log.highlight(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.PORTAL_LOGIN);
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
        node_id = JsonPath.from(response_body).getString("id");
        Log.highlight("Add new portal node successfully");
        return this;
    }

    public String getNode_id(){
        return node_id;
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
        String appKey = "&app_key=" + JsonPath.from(node_info.toString()).getString("appKey");
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

    public boolean nodeActive(String id){

        Response response = getNodeInfo(id);

        String response_body = response.getBody().asString();
        String status = JsonPath.from(response_body).getString("status");

        Log.info("Node info body: " + response_body);
        if(status.equalsIgnoreCase("verified"))
        { return true; }
        else { return false; }

    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_activate_node_successfully(String username, String password) throws InterruptedException, IOException {
        int i = 0;
        while (!nodeActive(JsonPath.from(node_info.toString()).getString("id")) && i < 35){
            Thread.sleep(30000);
            i++;
            if(i == 8 || i == 16 || i == 24 || i == 30){
                should_be_able_to_login(username, password);
            }

        }
        Assert.assertTrue(nodeActive(JsonPath.from(node_info.toString()).getString("id")));
        Log.highlight("Node register successfully");
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

        Log.info(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.DELETE_NODE + id);

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .delete(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.DELETE_PORTAL_NODE + id);

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

    public HttpResponse getMyNodeList() throws IOException, InterruptedException {

        Log.info(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_NODE_LIST + "?limit=5");

//        Response response = SerenityRest.rest()
//                .given()
//                .contentType(ContentType.JSON.withCharset("UTF-8"))
//                .header("Authorization", access_token)
//                .when()
//                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_NODE_LIST + "?limit=5");

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .header("Authorization", access_token)
                .uri(URI.create(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_NODE_LIST+ "?limit=50"))
                .GET()
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(response.statusCode()==200);


        return response;
    }

    // Not verify yet
//    @Step
//    public Portal_Community_Node_Steps should_be_able_to_get_my_node_list(){
//
//        Response response = getMyNodeList();
//        String response_body = response.getBody().asString();
//        Log.info("Response of get my node list: " + response_body);
//
//        Assert.assertTrue(response.getStatusCode() == 200);
//
//        Log.highlight("Get my node list successfully");
//        return this;
//    }

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

    // APIs call automatic in install script

    public Response get_geo(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_NODE_INFO + id + "/geo");

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_get_geo(String id){

        Response response = get_geo(id);
        String response_body = response.getBody().asString();
        Log.info("Response of get geo: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"), id);
        Assert.assertEquals(JsonPath.from(response_body).getString("status"), "created");

        Log.highlight("Get geo successfully");
        return this;
    }

    public Response get_register(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_NODE_INFO + id + "/register");

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_get_register(String id){

        Response response = get_register(id);
        String response_body = response.getBody().asString();
        Log.info("Response of get register: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"), id);
        Assert.assertEquals(JsonPath.from(response_body).getString("status"), "installed");

        Log.highlight("Get register successfully");
        return this;
    }

    public Response get_verify(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_NODE_INFO + id + "/verify");

        return response;
    }

    @Step
    public Portal_Community_Node_Steps should_be_able_to_get_verify(String id){

        Response response = get_verify(id);
        String response_body = response.getBody().asString();
        Log.info("Response of get verify: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"), id);
        Assert.assertEquals(JsonPath.from(response_body).getString("status"), "verified");

        Log.highlight("Get verify successfully");
        return this;
    }

    public void delete_all_node() throws IOException, InterruptedException {
        HttpResponse response = getMyNodeList();
        String response_body = response.body().toString();
        List<String> lst_node = JsonPath.from(response_body).getList("nodes.id");
        for(String node_id : lst_node){
            should_be_able_to_delete_node(node_id);
        }
        Log.highlight("All nodes are deleted ");
    }

}
