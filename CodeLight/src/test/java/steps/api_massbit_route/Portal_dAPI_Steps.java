package steps.api_massbit_route;

import com.google.gson.JsonObject;
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

import java.util.ArrayList;
import java.util.List;

public class Portal_dAPI_Steps {

//    public static String mbrid = "";
    public static String access_token = "";
    public static String project_id = "";
    public static String dAPI_id = "";
    public static String entrypoint_id = "";
    public static List<String> lstProjectId = new ArrayList<>();
    public static List<String> lst_dAPI_id = new ArrayList<>();


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
//    public Portal_dAPI_Steps should_be_able_to_say_hello(){
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

        Log.info(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.PORTAL_LOGIN);
        Log.info(body);
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
//                .header("mbrid", mbrid)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.PORTAL_LOGIN);

        return response;
    }

    @Step
    public Portal_dAPI_Steps should_be_able_to_login(String uname, String password){

        Response response = login(uname, password);
        String response_body = response.getBody().asString();
        Log.info("Response of login " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertFalse(JsonPath.from(response_body).getString("accessToken").isEmpty());

        access_token = "Bearer " + JsonPath.from(response_body).getString("accessToken");

        Log.highlight("Login with username " + uname + " successfully");
        return this;
    }

    public Response create_new_project(String name, String blockchain, String network){

        String body = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"blockchain\": \"" + blockchain + "\",\n" +
                "  \"network\": \"" + network + "\"\n" +
                "}";

        Log.info(body);
        Log.info("url: " +  utilSteps.getPortalURL()+ Massbit_Route_Endpoint.CREATE_NEW_PROJECT);
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.CREATE_NEW_PROJECT);

        return response;
    }

    @Step
    public Portal_dAPI_Steps should_be_able_to_create_new_project(String name, String blockchain, String network){

        Response response = create_new_project(name, blockchain, network);
        String response_body = response.getBody().asString();
        Log.info("Response of create new project " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertEquals(JsonPath.from(response_body).getString("name"),name);
        Assert.assertEquals(JsonPath.from(response_body).getString("blockchain"),blockchain);
        Assert.assertEquals(JsonPath.from(response_body).getString("network"),network);
        Assert.assertTrue(JsonPath.from(response_body).getString("status").equals("created"));
        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("userId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("createdAt").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("updatedAt").isEmpty());

        String id = JsonPath.from(response_body).getString("id");
        project_id = id;
        lstProjectId.add(id);

        Log.highlight("Create new project " + name + " successfully");
        return this;
    }

    public String getProjectId(){
        return project_id;
    }

    public Response get_list_of_project(){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_PROJECT_LIST);

        return response;
    }

    @Step
    public Portal_dAPI_Steps should_be_able_to_get_list_of_project(){

        Response response = get_list_of_project();
        String response_body = response.getBody().asString();
        Log.info("Response of get list of project: " + response_body);

        List<String> lst = JsonPath.from(response_body).getList("id");
        for(String projectId : lstProjectId){
            Assert.assertTrue(lst.contains(projectId));
        }

        Assert.assertTrue(response.getStatusCode() == 200);

        Log.highlight("Get list of project successfully");
        return this;
    }

    //Haven't done yet
    // ----------------------------------------------------------------------------------------------------------
    public Response create_new_dAPI(String name, String projectId){

        String body = "{\n" +
                "\"name\":\"" + name + "\",\n" +
                "\"projectId\":\"" + projectId + "\"\n" +
                "}";
        Log.info("url: " + utilSteps.getPortalURL()+ Massbit_Route_Endpoint.CREATE_NEW_DAPI);
        Log.info("body: " + body);
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.CREATE_NEW_DAPI);

        return response;
    }

    //Haven't done yet
    @Step
    public Portal_dAPI_Steps should_be_able_to_create_new_dAPI(String name, String projectId){

        Response response = create_new_dAPI(name, projectId);
        String response_body = response.getBody().asString();
        Log.info("Response of create new dAPI " + response_body);

        Assert.assertTrue(response.getStatusCode() == 201);

        Assert.assertTrue(JsonPath.from(response_body).getInt("status")==1);
        Assert.assertTrue(JsonPath.from(response_body).getInt("limitRatePerDay")==3000);
        Assert.assertTrue(JsonPath.from(response_body).getInt("limitRatePerSec")==100);
        Assert.assertTrue(JsonPath.from(response_body).getString("name").equals(name));
        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("userId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("projectId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("createdAt").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("updatedAt").isEmpty());

        dAPI_id = JsonPath.from(response_body).getString("id");
        lst_dAPI_id.add(dAPI_id);

        Log.highlight("Create new dAPI " + name + " successfully");
        return this;
    }

    @Step
    public Portal_dAPI_Steps create_new_dAPI_without_staking(String name, String projectId){

        Response response = create_new_dAPI(name, projectId);
        String response_body = response.getBody().asString();
        Log.info("Response of create new dAPI without staking " + response_body);

        Assert.assertTrue(response.getStatusCode() == 400);

        Assert.assertTrue(JsonPath.from(response_body).getString("message").equalsIgnoreCase("Project is not staked!"));
        Assert.assertTrue(JsonPath.from(response_body).getString("error").equalsIgnoreCase("Bad Request"));

        dAPI_id = JsonPath.from(response_body).getString("id");
        lst_dAPI_id.add(dAPI_id);

        Log.highlight("Create new dAPI " + name + " successfully");
        return this;
    }

    public String get_dAPI_id(){
        return dAPI_id;
    }

    public Response edit_dAPI(String dAPI_id){

        String body = "{\n" +
                "  \"name\": \"" + dAPI_id + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.EDIT_NEW_DAPI);

        return response;
    }

    @Step
    public Portal_dAPI_Steps should_be_able_to_edit_dAPI(String dAPI_id){

        Response response = edit_dAPI(dAPI_id);
        String response_body = response.getBody().asString();
        Log.info("Response of edit new dAPI " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);

        Assert.assertTrue(JsonPath.from(response_body).getString("status").equals("1"));

        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("userId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("projectId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("createdAt").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("updatedAt").isEmpty());

        Log.highlight("Edit dAPI successfully");
        return this;
    }

    public Response get_dAPI_info(String dAPI_id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_DAPI_INFO + dAPI_id);

        return response;
    }

    @Step
    public Portal_dAPI_Steps should_be_able_to_get_dAPI_info(String dAPI_id){

        Response response = get_dAPI_info(dAPI_id);
        String response_body = response.getBody().asString();
        Log.info("Response of get dAPI info " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);

        Assert.assertTrue(JsonPath.from(response_body).getString("status").equals("1"));
        Assert.assertEquals(JsonPath.from(response_body).getString("id"), dAPI_id);
        Assert.assertFalse(JsonPath.from(response_body).getString("userId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("projectId").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("createdAt").isEmpty());
        Assert.assertFalse(JsonPath.from(response_body).getString("updatedAt").isEmpty());

        Log.highlight("Get dAPI info of " + dAPI_id + " successfully");
        return this;
    }

    public Response get_dAPI_list_by_projectId(String project_id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_DAPI_LIST_BY_PROJECTID + project_id);

        return response;
    }

    @Step
    public Portal_dAPI_Steps should_be_able_to_get_dAPI_list_by_projectId(String project_id){

        Response response = get_dAPI_list_by_projectId(project_id);
        String response_body = response.getBody().asString();
        Log.info("Response of get dAPI list by projectId " + response_body);

        List<String> lst = JsonPath.from(response_body).getList("id");
        for(String dAPIId : lst_dAPI_id){
            Assert.assertTrue(lst.contains(dAPIId));
        }

        Assert.assertTrue(response.getStatusCode() == 200);



        Log.highlight("Get dAPI list by projectId: " + project_id + " successfully");
        return this;
    }

    public Response add_new_entrypoint(String dAPI_id, String provider){

        String body = "";
        switch (provider){
            case "MASSBIT":
                body = Massbit_Route_Config.PORTAL_ENTRYPOINT_MASSBIT;
                Log.highlight("config: " + Massbit_Route_Config.PORTAL_ENTRYPOINT_MASSBIT);
                break;
            case "INFURA":
                body = Massbit_Route_Config.PORTAL_ENTRYPOINT_INFURA;
                Log.info("Infura config: " + Massbit_Route_Config.PORTAL_ENTRYPOINT_INFURA);
                break;
            case "GETBLOCK":
                body = Massbit_Route_Config.PORTAL_ENTRYPOINT_GETBLOCK;
                Log.info("Getblock config: " + Massbit_Route_Config.PORTAL_ENTRYPOINT_GETBLOCK);
                break;
            case "QUICKNODE":
                body = Massbit_Route_Config.PORTAL_ENTRYPOINT_QUICKNODE;
                Log.info("Quicknode config: " + Massbit_Route_Config.PORTAL_ENTRYPOINT_QUICKNODE);
                break;
            case "CUSTOM":
                body = Massbit_Route_Config.PORTAL_ENTRYPOINT_CUSTOM;
                Log.info("Custom config: " + Massbit_Route_Config.PORTAL_ENTRYPOINT_CUSTOM);
                break;
            default:
                body = Massbit_Route_Config.PORTAL_ENTRYPOINT_MASSBIT;
                Log.highlight("config: " + Massbit_Route_Config.PORTAL_ENTRYPOINT_MASSBIT);
                break;

        }
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL() + Massbit_Route_Endpoint.CREATE_ENTRYPOINT + dAPI_id);

        return response;
    }

    //Haven't done yet
    @Step
    public Portal_dAPI_Steps should_be_able_to_add_entrypoint(String dAPI_id, String provider){

        Response response = add_new_entrypoint(dAPI_id, provider);
        String response_body = response.getBody().asString();
        Log.info("Response of add entrypoint " + response_body);

        String id = JsonPath.from(response_body).getString("id");
        entrypoint_id = id;

        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertFalse(id.isEmpty());


        Log.highlight("Add entrypoint of dAPI " + dAPI_id + " successfully. Entrypoint id: " + id);
        return this;
    }

    public String getEntrypoint_id(){
        return entrypoint_id;
    }

    public Response edit_entrypoint(String entrypoint_id){

        String body = "{\n" +
                "\"id\":\"" + entrypoint_id + "\",\n" +
                "\"provider\":\"GETBLOCK\",\n" +
                "\"priority\":1,\n" +
                "\"status\":1,\n" +
                "\"providerConfig\":{\n" +
                "\"api_key\":\"hgashjja\"\n" +
                "}\n" +
                "}";


        Log.info("entrypoint id: " + entrypoint_id);
        Log.info("url: " + utilSteps.getPortalURL()+ Massbit_Route_Endpoint.EDIT_ENTRYPOINT + entrypoint_id);
        Log.info("body: " + body);
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .body(body)
                .put(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.EDIT_ENTRYPOINT + entrypoint_id);

        return response;
    }

    //Haven't done yet
    @Step
    public Portal_dAPI_Steps should_be_able_to_edit_entrypoint(String entrypoint_id){

        Response response = edit_entrypoint(entrypoint_id);
        String response_body = response.getBody().asString();
        Log.info("Response of edit entrypoint " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);



        Log.highlight("Edit entrypoint of dAPI " + dAPI_id + " successfully");
        return this;
    }

    public Response delete_entrypoint(String entrypoint_id){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .when()
                .delete(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.DELETE_ENTRYPOINT + entrypoint_id);

        return response;
    }

    //Haven't done yet
    @Step
    public Portal_dAPI_Steps should_be_able_to_delete_entrypoint(String entrypoint_id){

        Response response = delete_entrypoint(entrypoint_id);
        String response_body = response.getBody().asString();
        Log.info("Response of delete entrypoint " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);

        Log.highlight("Delete entrypoint of dAPI " + dAPI_id + " successfully. Entrypoint id: " + entrypoint_id);
        return this;
    }

}
