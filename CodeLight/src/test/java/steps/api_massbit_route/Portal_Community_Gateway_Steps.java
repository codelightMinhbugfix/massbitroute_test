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

public class Portal_Community_Gateway_Steps {

    public static String mbrid = "";
    public static String access_token = "";
    public static String gw_id = "";
    public static String gw_info = "";


    @Steps
    private UtilSteps utilSteps;


//    public Response hello(){
//        Response response = SerenityRest.rest()
//                .given()
//                .contentType(ContentType.JSON.withCharset("UTF-8"))
//                .header("mbrid", 1)
//                .when()
//                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.HELLO);
//
//        return response;
//    }
//
//    @Step
//    public Portal_Community_Gateway_Steps should_be_able_to_say_hello(){
//
//        Response response = hello();
//        String response_body = response.getBody().asString();
//        Log.info("response of hello is " + response_body);
//        Assert.assertTrue(response.getStatusCode() == 200);
//        Assert.assertFalse(response_body.isEmpty());
//
//        mbrid = response_body;
//
//        return this;
//    }

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
    public Portal_Community_Gateway_Steps should_be_able_to_login(String uname, String password){

        Response response = login(uname, password);
        String response_body = response.getBody().asString();
        Log.info("Response of login: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertFalse(JsonPath.from(response_body).getString("accessToken").isEmpty());

        access_token = "Bearer " + JsonPath.from(response_body).getString("accessToken");

        Log.info("Access token: " + access_token);
        Log.highlight("Login with username " + uname + " successfully");
        return this;
    }

    public Response add_new_gateway(String name, String blockchain, String zone, String network){

        String body = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"blockchain\": \"" + blockchain + "\",\n" +
                "  \"network\": \"" + network + "\",\n" +
                "  \"zone\": \"" + zone + "\"\n" +
                "}";

        Log.info("Body of adding new gateway: " + body);
        Log.info("access token: " + access_token);

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.ADD_NEW_GATEWAY);

        return response;
    }

    @Step
    public Portal_Community_Gateway_Steps should_be_able_to_add_new_portal_gateway(String name, String blockchain, String zone, String network){

        Response response = add_new_gateway(name, blockchain, zone, network);
        String response_body = response.getBody().asString();
        Log.info("Response of add new portal gateway: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertEquals(JsonPath.from(response_body).getString("name"), name);
        Assert.assertEquals(JsonPath.from(response_body).getString("blockchain"), blockchain);
        Assert.assertEquals(JsonPath.from(response_body).getString("network"), network);
        Assert.assertEquals(JsonPath.from(response_body).getString("zone"), zone);
        Assert.assertEquals(JsonPath.from(response_body).getString("status"), "created");
        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("userId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("appKey").isEmpty());

        gw_id = JsonPath.from(response_body).getString("id");
        gw_info = response_body;

        Log.highlight("Add new portal gateway successfully");
        return this;
    }

    public String get_gateway_id(){
        return gw_id;
    }


    public Response edit_gateway_name(String id, String new_name){

        String body = "{\n" +
                "  \"name\": \"" + new_name + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .put(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.EDIT_GATEWAY_NAME + id);

        return response;
    }

    @Step
    public Portal_Community_Gateway_Steps should_be_able_to_edit_gateway_name(String id, String new_name){

        Response response = edit_gateway_name(id, new_name);
        String response_body = response.getBody().asString();
        Log.info("Response of edit gateway name: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("name"), new_name);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"), id);

        Log.highlight("Edit gateway name successfully");
        return this;
    }

    public Response delete_gateway(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .delete(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.DELETE_GATEWAY + id);

        return response;
    }

    @Step
    public Portal_Community_Gateway_Steps should_be_able_to_delete_gateway(String id){

        Response response = delete_gateway(id);
        String response_body = response.getBody().asString();
        Log.info("Response of delete gateway: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("status"));

        Log.highlight("Delete gateway successfully");
        return this;
    }



    public Response getGatewayByStatus(String status){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_GATEWAY_BY_STATUS + "?status=" + status);

        return response;
    }

    // Not verify yet
    @Step
    public Portal_Community_Gateway_Steps should_be_able_to_get_gateway_by_status(String status){

        Response response = getGatewayByStatus(status);
        String response_body = response.getBody().asString();
        Log.info("Response of get gateway by status: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);

        Log.highlight("Get gateway by status successfully");
        return this;
    }

    public HttpResponse getMyGatewayList() throws IOException, InterruptedException {

        Log.highlight(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_GATEWAY_LIST + "?limit=5");
        Log.highlight(access_token);

//        Response response = SerenityRest.rest()
//                .given()
//                .header("Authorization", access_token)
//                .when().log().all()
//                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_GATEWAY_LIST+ "?limit=5");
//
//        return response;

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .header("Authorization", access_token)
                .uri(URI.create(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_MY_GATEWAY_LIST+ "?limit=50"))
                .GET()
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(response.statusCode()==200);

        return response;
    }

    // Not verify yet
//    @Step
//    public Portal_Community_Gateway_Steps should_be_able_to_get_my_gateway_list(){
//
//        Response response = getMyGatewayList();
//        String response_body = response.getBody().asString();
//        Log.info("Response of get my gateway list: " + response_body);
//
//        Assert.assertTrue(response.getStatusCode() == 200);
//
//        Log.highlight("Get my gateway list successfully");
//        return this;
//    }


    public Response getGatewayInfo(String id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_GATEWAY_INFO + id);

        Log.highlight("gateway info: " + response.getBody().asString());
        return response;

    }


    @Step
    public Portal_Community_Gateway_Steps should_be_able_to_get_gateway_info(String id){

        Response response = getGatewayInfo(id);
        String response_body = response.getBody().asString();
        Log.info("Response of get gateway info: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(JsonPath.from(response_body).getString("id"),id);

        Log.highlight("Get gateway info successfully");
        return this;
    }

    public String get_install_gateway_script(){

        String cmd_start = "sudo bash -c \"$(curl -sSfL '";
        String url = utilSteps.getAPIURL() + "/v1/gateway_install?";
        String id = "id=" + JsonPath.from(gw_info.toString()).getString("id");
        String user_id = "&user_id=" + JsonPath.from(gw_info.toString()).getString("userId");
        String blockchain = "&blockchain=" + JsonPath.from(gw_info.toString()).getString("blockchain");
        String network = "&network=" + JsonPath.from(gw_info.toString()).getString("network");
        String zone = "&zone=" + JsonPath.from(gw_info.toString()).getString("zone");
        String appKey = "&app_key=" + JsonPath.from(gw_info.toString()).getString("appKey");
        String cmd_end = "')\"";

        String install_script = "echo yes|" + cmd_start + url + id + user_id + blockchain + network + zone + appKey + "&portal_url=" + Massbit_Route_Config.portal_url + cmd_end;

        Log.highlight("install portal gateway script: " + install_script);

        return install_script;
    }

    public Portal_Community_Gateway_Steps create_vm_instance_and_register_gateway(String installScript) throws InterruptedException, IOException {

        UtilSteps.writeToFile(Massbit_Route_Config.PORTAL_GW_PATH_TERRAFORM_INIT, installScript);

        Thread.sleep(1000);

        UtilSteps.runCommand(Massbit_Route_Config.PORTAL_GW_PATH_TERRAFORM_APPLY);

        return this;
    }

    public Portal_Community_Gateway_Steps destroy_vm_instance() throws InterruptedException, IOException {

        Thread.sleep(4000);
        UtilSteps.runCommand(Massbit_Route_Config.PORTAL_GW_PATH_TERRAFORM_DESTROY);

        Thread.sleep(10000);

        Log.highlight("Destroy VM instance of portal gateway successfully");

        return this;
    }

    public boolean gatewayActive(String id){

        Response response_portal_gw_info = getGatewayInfo(id);

        String response_body = response_portal_gw_info.getBody().asString();
        String status = JsonPath.from(response_body).getString("status");

        Log.info("Portal Gateway info body: " + response_body);
        if(status.equalsIgnoreCase("verified"))
        { return true; }
        else { return false; }

    }

    @Step
    public Portal_Community_Gateway_Steps should_be_able_to_activate_gateway_successfully(String id, String username, String password) throws InterruptedException, IOException {
        int i = 0;
        while (!gatewayActive(id) && i < 35){
            Thread.sleep(30000);
            i++;
            if(i==8 || i == 16 || i == 24 || i == 32){
                should_be_able_to_login(username, password);
            }

        }
        Assert.assertTrue(gatewayActive(id)) ;
        Log.highlight("Portal Gateway register successfully");
        return this;
    }



}
