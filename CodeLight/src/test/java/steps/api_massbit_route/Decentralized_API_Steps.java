package steps.api_massbit_route;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Decentralized_API_Steps {

    public static String mbrid = "";
    public static String sid = "";
    public static JsonObject api_info;
    public static ArrayList<String> listAPI = new ArrayList<String>();

    @Steps
    UtilSteps utilSteps;

    public Response hello(){
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", 1)
                .when()
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.HELLO);
//                .then().extract().asString();


        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_say_hello(){

        Response response = hello();
        String response_body = response.getBody().asString();
        Log.info("response of hello is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        mbrid = response_body;

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_login() throws IOException, InterruptedException {

        Log.info("Start to login");
        String body = "\n{\n" +
                "\t\"username\":\"duongvu\",\n" +
                "\t\"password\":\"duongvu\"\n" +
                "}";

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .header("mbrid", mbrid)
                .uri(URI.create(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.LOGIN))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(response.statusCode()==200);
        Assert.assertFalse(response.body().toString().isEmpty());

        String res = response.body().toString();

        sid = JsonPath.from(res).getString("data.sid");

        Log.highlight("sid :" + sid);

        return this;
    }


    public Response createApi(String name, String blockchain, String network){

        Log.info("Create API");
        String body = "{\n" +
                "    \"name\":\"" + name + "\",\n" +
                "    \"blockchain\":\"" + blockchain + "\",\n" +
                "    \"network\":\"" + network + "\",\n" +
                "    \"sid\":\"" + sid + "\"\n" +
                "}";

        Log.info("Body of create api: " + body);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(body)
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CREATE_API);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_create_api(String name, String blockchain, String network){

        Log.info("Start to create api");
        name.replaceAll(" ","");
        blockchain.replaceAll(" ","");
        network.replaceAll(" ","");
        Response response = createApi(name, blockchain, network);
        String response_body = response.getBody().asString();
        Log.info("response of create api of " + blockchain + " is: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));
        Assert.assertFalse(JsonPath.from(response_body).getString("data.id").isEmpty());

        api_info = JsonPath.from(response_body).getObject("data", JsonObject.class);

        return this;
    }


    public Response update_api(JsonObject json){

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(json)
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.UPDATE_API);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_day_to_unlimited(){

        Log.info("Start to update api");
        api_info.getAsJsonObject("security").addProperty("limit_rate_per_day",0);;

        Log.info("api_info: " + api_info);

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();

        Log.info("Response of update api"+ response_body) ;

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_day(int number){

        Log.info("Start to update request limit per day");

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_day",number);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response of update request limit per day is " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_day_to_negative_number(){

        Log.info("Start to update request limit per day to negative number");
        api_info.getAsJsonObject("security").addProperty("limit_rate_per_day",-2);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response of update request limit per day to negative number " + response_body);

//        Assert.assertTrue(response.getStatusCode() == 200);
//        Assert.assertFalse(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_sec(int number){

        Log.info("Start to update api request limit per second ");

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_sec",number);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response of update api request limit per second " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }


    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_sec_to_zero(){

        Log.info("Start to update api request limit per second to zero");

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_sec",0);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response of update api request limit per second to zero " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_sec_to_negative_number(){

        Log.info("Start to update api request limit per second to negative number");

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_sec",-5);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response of update api request limit per second to negative number " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    // ADD ALLOW LIST API REQUEST METHOD INTO DATA FILE
//    @Step
//    public Decentralized_API_Steps should_be_able_to_update_allowlist_api_request_method(){
//
//        api_info.getAsJsonObject("security").addProperty("allow_methods","eth_accounts,eth_getBalance,eth_blockNumber");;
//
//        Response response = update_api(api_info);
//        String response_body = response.getBody().asString();
//        Log.info("response is " + response_body);
//
//        Assert.assertTrue(response.getStatusCode() == 200);
//        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));
//
//        return this;
//    }

    public Response get_api_list(){
        Response response = SerenityRest.rest()
                .given().log().all()
                .header("Content-Type", "application/json").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when().log().all()
                .get(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.GET_API_LIST + "&sid=" + sid);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_get_api_list(){

        Response response = get_api_list();
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());


        ArrayList<JsonObject> arr = JsonPath.from(response_body).getJsonObject("data");

        for(Object object : arr){

                String id = ((HashMap) object).get("id").toString();

                listAPI.add(id);
        }

        return this;
    }

    public JsonObject get_api_info(String id){
        Response response = SerenityRest.rest()
                .given().log().all()
                .header("Content-Type", "application/json").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when().log().all()
                .get(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.GET_API_INFO + "&id=" + id + "&sid=" + sid);

        String response_body = response.getBody().asString();
        JsonObject object = JsonPath.from(response_body).getObject("data", JsonObject.class);
        return object;
    }

    public void deleteAPI(String id){

        String body = "{\n" +
                "    \"id\":\"" + id + "\",\n" +
                "    \"sid\":\"" + sid + "\"\n" +
                "}";

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(body)
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.DELETE_API);

        Log.info(response.getBody().asString());

        Log.highlight(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.DELETE_API);
        Log.highlight("mbrid: " +body );
        Log.highlight("mbrid: " +mbrid );
        Log.highlight("sid: " + sid );

        Log.highlight("Delete api " +id + " success");
    }

    public void disable_api(String id){

        JsonObject api = get_api_info(id);
        api.addProperty("status","0");


    }

    public void disable_all_test_api(ArrayList<String> lst){
        for(String id : lst){
            disable_api(id);
            Log.info("Disable api " + id + "success");
        }

        Log.highlight("Finish to Disable all api test");
    }

    public void delete_all_test_api(ArrayList<String> lst){
        for(String id : lst){
            deleteAPI(id);
            Log.info("Delete api " + id + "success");
        }

        Log.highlight("Finish to delete all api test");
    }

    public Response add_entrypoint(String type){

        Log.info("Start to add entrypoint");

        JsonArray arr = api_info.getAsJsonArray("entrypoints");

        switch (type){
            case "MASSBIT":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_MASSBIT).getObject("",JsonObject.class));
                Log.highlight("config: " + Massbit_Route_Config.ENTRYPOINT_MASSBIT);
                break;
            case "INFURA":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_INFURA).getObject("",JsonObject.class));
                Log.info("Infura config: " + Massbit_Route_Config.ENTRYPOINT_INFURA);
                break;
            case "GETBLOCK":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_GETBLOCK).getObject("",JsonObject.class));
                break;
            case "QUICKNODE":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_QUICKNODE).getObject("",JsonObject.class));
                break;
            case "CUSTOM":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_CUSTOM).getObject("",JsonObject.class));
                break;
            case "MASSBIT_EDIT":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_MASSBIT_EDIT).getObject("",JsonObject.class));
                break;
            case "INFURA_EDIT":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_INFURA_EDIT).getObject("",JsonObject.class));
                break;
            case "GETBLOCK_EDIT":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_GETBLOCK_EDIT).getObject("",JsonObject.class));
                break;
            case "QUICKNODE_EDIT":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_QUICKNODE_EDIT).getObject("",JsonObject.class));
                break;
            case "CUSTOM_EDIT":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_CUSTOM_EDIT).getObject("",JsonObject.class));
                break;
            case "MASSBIT_DISABLE":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_MASSBIT_DISABLE).getObject("",JsonObject.class));
                break;
            case "INFURA_DISABLE":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_INFURA_DISABLE).getObject("",JsonObject.class));
                break;
            case "GETBLOCK_DISABLE":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_GETBLOCK_DISABLE).getObject("",JsonObject.class));
                break;
            case "QUICKNODE_DISABLE":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_QUICKNODE_DISABLE).getObject("",JsonObject.class));
                break;
            case "CUSTOM_DISABLE":
                arr.remove(0);
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_CUSTOM_DISABLE).getObject("",JsonObject.class));
                break;
            case "DELETE":
                arr.remove(0);
                break;
            default:
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_MASSBIT).getObject("",JsonObject.class));
        }

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(api_info)
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.UPDATE_API);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_add_entrypoint(String type){

        Response response = add_entrypoint(type);
        String response_body = response.getBody().asString();
        Log.info("Response of add entrypoint is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_edit_entrypoint(String type){

        Log.info("Start to edit entrypoint");

        type.replaceAll(" ","");
        Response response = add_entrypoint(type);
        String response_body = response.getBody().asString();
        Log.info("response of edit entrypoint " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_delete_entrypoint(String type){

        Log.info("Start to delete entrypoint");

        Response response = add_entrypoint(type);
        String response_body = response.getBody().asString();
        Log.info("response of delete entrypoint is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    public Response send_api_request(String blockchain) throws InterruptedException {

        Log.info("Start to call API");
        String body ="";
        switch (blockchain) {
            case "eth":
                body = Massbit_Route_Config.ETHEREUM;
                break;
            case "near":
                body = Massbit_Route_Config.NEAR;
                break;
            case "hmny":
                body = Massbit_Route_Config.HARMONY;
                break;
            case "dot":
                body = Massbit_Route_Config.POLKADOT;
                break;
            case "avax":
                body = Massbit_Route_Config.AVALANCHE;
                break;
            case "ftm":
                body = Massbit_Route_Config.FANTOM;
                break;
            case "matic":
                body = Massbit_Route_Config.POLYGON;
                break;
            case "bsc":
                body = Massbit_Route_Config.BSC;
                break;
            case "sol":
                body = Massbit_Route_Config.SOLANA;
                break;
            default:
                body = Massbit_Route_Config.ETHEREUM;
        }

        String url = JsonPath.from(api_info.toString()).getString("gateway_http");
        Log.highlight("url: " + url);
        Log.highlight("body: " + body);
        Thread.sleep(30000);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when()
                .body(body)
                .post(url);

        return response;
    }

    public Response send_api_request_direct_gw_by_massbit_dommain(String blockchain, String object, String id) throws InterruptedException {

        Log.info("Start to call API direct to" + object + " from massbit domain");
        String body ="";
        switch (blockchain) {
            case "eth":
                body = Massbit_Route_Config.ETHEREUM;
                break;
            case "near":
                body = Massbit_Route_Config.NEAR;
                break;
            case "hmny":
                body = Massbit_Route_Config.HARMONY;
                break;
            case "dot":
                body = Massbit_Route_Config.POLKADOT;
                break;
            case "avax":
                body = Massbit_Route_Config.AVALANCHE;
                break;
            case "ftm":
                body = Massbit_Route_Config.FANTOM;
                break;
            case "matic":
                body = Massbit_Route_Config.POLYGON;
                break;
            case "bsc":
                body = Massbit_Route_Config.BSC;
                break;
            case "sol":
                body = Massbit_Route_Config.SOLANA;
                break;
            default:
                body = Massbit_Route_Config.ETHEREUM;
        }

        String url = "";
        if(object.equalsIgnoreCase("gateway")){
            url = id + ".gw.mbr.massbitroute.com";
        } else {

            url = id + ".node.mbr.massbitroute.com";
        }
        Thread.sleep(30000);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when()
                .body(body)
                .post(url);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_send_api_request(String blockchain) throws InterruptedException {

        Response response = send_api_request(blockchain);
        String response_body = response.getBody().asString();
        String header_response = response.getHeaders().toString();
        Log.highlight("Header of response: " + header_response);
        Log.info("response of call API is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        return this;
    }

    @Step
    public Decentralized_API_Steps send_api_request_over_request_limit_per_day(String blockchain) throws InterruptedException {

        Response response = send_api_request(blockchain);
        String response_body = response.getBody().asString();
        String header_response = response.getHeaders().toString();
        Log.highlight("Header of response: " + header_response);
        Log.info("response of call API is " + response_body);
        Assert.assertFalse(response.getStatusCode() == 200);
//        Assert.assertFalse(response_body.isEmpty());

        return this;
    }

    @Step
    public Decentralized_API_Steps send_api_request_with_bad_request_body(String blockchain) throws InterruptedException {

        Response response = send_api_request(blockchain);
        String response_body = response.getBody().asString();
        String header_response = response.getHeaders().toString();
        Log.highlight("Header of response: " + header_response);
        Log.info("response of call API is " + response_body);
        Assert.assertFalse(response.getStatusCode() == 200);
//        Assert.assertFalse(response_body.isEmpty());

        return this;
    }

    @Step
    public Decentralized_API_Steps send_api_request_without_entrypoint(String blockchain) throws InterruptedException {

        Response response = send_api_request(blockchain);
        String response_body = response.getBody().asString();
        String header_response = response.getHeaders().toString();
        Log.highlight("Header of response: " + header_response);
        Log.info("response of call API is " + response_body);
        Assert.assertFalse(response.getStatusCode() == 200);
//        Assert.assertFalse(response_body.isEmpty());

        return this;
    }

    public Response send_api_request_direct_to_gateway(String blockchain, String ip) throws InterruptedException {

        Log.info("Start to call API direct to gateway");
        String body ="";
        switch (blockchain) {
            case "eth":
                body = Massbit_Route_Config.ETHEREUM;
                break;
            case "near":
                body = Massbit_Route_Config.NEAR;
                break;
            case "hmny":
                body = Massbit_Route_Config.HARMONY;
                break;
            case "dot":
                body = Massbit_Route_Config.POLKADOT;
                break;
            case "avax":
                body = Massbit_Route_Config.AVALANCHE;
                break;
            case "ftm":
                body = Massbit_Route_Config.FANTOM;
                break;
            case "matic":
                body = Massbit_Route_Config.POLYGON;
                break;
            case "bsc":
                body = Massbit_Route_Config.BSC;
                break;
            case "sol":
                body = Massbit_Route_Config.SOLANA;
                break;
            default:
                body = Massbit_Route_Config.ETHEREUM;
        }

        String b = JsonPath.from(api_info.toString()).getString("gateway_http").split(".com")[0] + ".com";
        String host = b.substring(8);
        String url = "http://" + ip + JsonPath.from(api_info.toString()).getString("gateway_http").split(".com")[1];

        Log.highlight("host: " + host);
        Log.highlight("url: " + url);
        Log.highlight("body: " + body);
        Thread.sleep(30000);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("Host", host)
                .when()
                .body(body)
                .post(url);

        Log.highlight("response of send api direct to gateway: " + response.getBody().asString());

        return response;
    }

    public Response send_api_request_direct_to_node(String blockchain, String id, String x_api_key) throws InterruptedException {

        Log.info("Start to call API direct to node");
        String body ="";
        switch (blockchain) {
            case "eth":
                body = Massbit_Route_Config.ETHEREUM;
                break;
            case "near":
                body = Massbit_Route_Config.NEAR;
                break;
            case "hmny":
                body = Massbit_Route_Config.HARMONY;
                break;
            case "dot":
                body = Massbit_Route_Config.POLKADOT;
                break;
            case "avax":
                body = Massbit_Route_Config.AVALANCHE;
                break;
            case "ftm":
                body = Massbit_Route_Config.FANTOM;
                break;
            case "matic":
                body = Massbit_Route_Config.POLYGON;
                break;
            case "bsc":
                body = Massbit_Route_Config.BSC;
                break;
            case "sol":
                body = Massbit_Route_Config.SOLANA;
                break;
            default:
                body = Massbit_Route_Config.ETHEREUM;
        }

        String url = "http://" + id + ".node.mbr." + utilSteps.getEnvironmentDomain();

        Log.info("url: " + url) ;
        Thread.sleep(30000);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("x-api-key", x_api_key)
                .when()
                .body(body)
                .post(url);

        Log.highlight("response of send api direct to node: " + response.getBody().asString());

        return response;
    }

    public List<String> get_list_my_api(){
        Response response = SerenityRest.rest()
                .given().log().all()
                .header("mbrid", mbrid)
                .header("Content-Type", "application/json").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when().log().all()
                .get("https://dapi.massbit.io/api/v1?action=api.list&sid=" + sid);

        String response_body = response.getBody().asString();
        List<String> list_api = JsonPath.from(response_body).getList("data.id");
        Log.highlight("done");
        return  list_api;
    }

    public void cleanup_dapi() throws IOException, InterruptedException {

        Log.info("Start to clean up my dapi");
        List<String> lst = get_list_my_api();

        for(String api_id : lst){
            deleteAPI(api_id);
        }

        Log.highlight("clean up my dapi done");
    }

}
