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

import org.junit.Assert;

import steps.UtilSteps;
import utilities.Log;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Decentralized_API_Steps {

    public static String mbrid = "";
    public static String sid = "";
    public static JsonObject api_info;

    public Response hello(){
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", 1)
                .when()
                .post(UtilSteps.getAPIURL()+ Massbit_Route_Endpoint.HELLO);
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

        String body = "\n{\n" +
                "\t\"username\":\"massbit\",\n" +
                "\t\"password\":\"massbit123\"\n" +
                "}";

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .header("mbrid", mbrid)
                .uri(URI.create(UtilSteps.getAPIURL()+ Massbit_Route_Endpoint.LOGIN))
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
                .post(UtilSteps.getAPIURL()+ Massbit_Route_Endpoint.CREATE_API);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_create_api(String name, String blockchain, String network){

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
                .post(UtilSteps.getAPIURL()+ Massbit_Route_Endpoint.UPDATE_API);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_day_to_unlimited(){

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_day",0);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_day(){

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_day",999999999);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_day_to_negative_number(){

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_day",-2);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);

//        Assert.assertTrue(response.getStatusCode() == 200);
//        Assert.assertFalse(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_sec(){

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_sec","999999999");;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }


    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_sec_to_zero(){

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_sec",0);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_update_api_request_limit_per_sec_to_negative_number(){

        api_info.getAsJsonObject("security").addProperty("limit_rate_per_sec",-5);;

        Response response = update_api(api_info);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);

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
                .get(UtilSteps.getAPIURL()+ Massbit_Route_Endpoint.GET_API_LIST);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_get_api_list(){

        Response response = get_api_list();
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        mbrid = response_body;

        return this;
    }

    public Response add_entrypoint(String type){

        Log.info("ADD ENTRYPOINT");

        JsonArray arr = api_info.getAsJsonArray("entrypoints");

        switch (type){
            case "MASSBIT":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_MASSBIT).getObject("",JsonObject.class));
                Log.highlight("config: " + Massbit_Route_Config.ENTRYPOINT_MASSBIT);
                break;
            case "INFURA":
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_INFURA).getObject("",JsonObject.class));
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
            case "DELETE":
                arr.remove(0);
                break;
            default:
                arr.add(JsonPath.from(Massbit_Route_Config.ENTRYPOINT_MASSBIT).getObject("",JsonObject.class));
        }




        Log.info(api_info.toString());

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(api_info)
                .post(UtilSteps.getAPIURL()+ Massbit_Route_Endpoint.UPDATE_API);

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_add_entrypoint(String type){

        Response response = add_entrypoint(type);
        String response_body = response.getBody().asString();
        Log.info("response of add entrypoint is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_edit_entrypoint(String type){

        type.replaceAll(" ","");
        Response response = add_entrypoint(type);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));

        return this;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_delete_entrypoint(String type){

        Response response = add_entrypoint(type);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);
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

        Log.highlight("url: " + JsonPath.from(api_info.toString()).getString("gateway_http"));
        Log.highlight("body: " + body);
        Thread.sleep(30000);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when()
                .body(body)
                .post(JsonPath.from(api_info.toString()).getString("gateway_http"));

        return response;
    }

    @Step
    public Decentralized_API_Steps should_be_able_to_send_api_request(String blockchain) throws InterruptedException {

        Response response = send_api_request(blockchain);
        String response_body = response.getBody().asString();
        Log.info("response is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        return this;
    }



}
