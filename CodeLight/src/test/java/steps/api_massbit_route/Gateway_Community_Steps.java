package steps.api_massbit_route;

import com.google.gson.JsonObject;
import com.google.inject.internal.cglib.core.$CollectionUtils;
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
import java.util.Set;

public class Gateway_Community_Steps {

    public static String mbrid = "";
    public static String sid = "";
    public static JsonObject gateway_info;
    public static ArrayList<ArrayList<String>> listGW = new ArrayList<ArrayList<String>>();

    @Steps
    private UtilSteps utilSteps;

    public Response hello(){
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", 1)
                .when()
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.HELLO);

        return response;
    }

    @Step
    public Gateway_Community_Steps should_be_able_to_say_hello(){

        Response response = hello();
        String response_body = response.getBody().asString();
        Log.info("response of hello is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        mbrid = response_body;

        return this;
    }

    @Step
    public void should_be_able_to_login() throws IOException, InterruptedException {

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

    }

    public Response add_new_gateway(String name, String blockchain, String zone, String network){
        String body = "{\n" +
                "    \"name\":\"" + name + "\",\n" +
                "    \"blockchain\":\"" + blockchain + "\",\n" +
                "    \"zone\":\"" + zone + "\",\n" +
                "    \"network\":\"" + network + "\",\n" +
                "    \"sid\":\"" + sid + "\"\n" +
                "}";

        Log.info("Request body of add new gateway api:" + body);
        Log.info("url: " + utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CREATE_GATEWAY);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(body)
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CREATE_GATEWAY);

        return response;
    }

    @Step
    public Gateway_Community_Steps should_be_able_to_add_new_gateway(String name, String blockchain, String zone, String network){

        Response response = add_new_gateway(name, blockchain, zone, network);
        String response_body = response.getBody().asString();
        Log.info("response of add new gateway of " + blockchain + " is: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));
        Assert.assertFalse(JsonPath.from(response_body).getString("data.id").isEmpty());

        gateway_info = JsonPath.from(response_body).getObject("data", JsonObject.class);

        return this;
    }

    public String get_install_gateway_script(){

        String cmd_start = "bash -c \"$(curl -sSfL '";
        String url = "https://dapi.massbit.io/api/v1/gateway_install?";
        String id = "id=" + JsonPath.from(gateway_info.toString()).getString("id");
        String user_id = "&user_id=" + JsonPath.from(gateway_info.toString()).getString("user_id");
        String blockchain = "&blockchain=" + JsonPath.from(gateway_info.toString()).getString("blockchain");
        String network = "&network=" + JsonPath.from(gateway_info.toString()).getString("network");
        String zone = "&zone=" + JsonPath.from(gateway_info.toString()).getString("zone");
        String cmd_end = "')\"";

        String install_script = cmd_start + url + id + user_id + blockchain + network + zone + cmd_end;


        return install_script;
    }

    public boolean gatewayActive(String id){

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .get(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CHECK_GATEWAY + id + "&sid=" + sid);

        String response_body = response.getBody().asString();
        int status = JsonPath.from(response_body).getInt("data.status");

        Log.info("Gateway info body: " + response_body);
        if(status == 1)
        { return true; }
        else { return false; }

    }

    @Step
    public Gateway_Community_Steps should_be_able_to_activate_gateway_successfully() throws InterruptedException, IOException {
        int i = 0;
        while (!gatewayActive(JsonPath.from(gateway_info.toString()).getString("id")) && i < 20){
            Thread.sleep(30000);
            i++;
            should_be_able_to_login();
        }
        Assert.assertTrue(gatewayActive(JsonPath.from(gateway_info.toString()).getString("id")));
        Log.highlight("Gateway register successfully");
        return this;
    }

    @Step
    public void ping_to_gateway(String id) throws InterruptedException, IOException {

        String url = "https://" + id + ".gw.mbr." + utilSteps.getEnvironmentDomain() +"/ping";
        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .get(url);

        String response_body = response.getBody().asString();
        Assert.assertTrue(response.getStatusCode()==200);
        Assert.assertTrue(response_body.equalsIgnoreCase("pong"));

    }

    public ArrayList<ArrayList<String>> listGateway(){

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                .header("mbrid", mbrid)
                .when()
                .get(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.GET_GATEWAY_LIST + "&sid=" + sid);

        String response_body = response.getBody().asString();

        ArrayList<JsonObject> arr = JsonPath.from(response_body).getJsonObject("data");

        for(Object object : arr){
            if(((HashMap) object).get("status").toString().equals("1")){
                String id = ((HashMap) object).get("id").toString();
                String blockchain = ((HashMap) object).get("blockchain").toString();
                String ip = ((HashMap) object).get("ip").toString();
                ArrayList<String> gw = new ArrayList<>();
                gw.add(id);
                gw.add(blockchain);
                gw.add(ip);
                listGW.add(gw);
            }
            Log.highlight("done");
        }

        return listGW;
    }


    public List<String> listMyGateway(){

        Response response = SerenityRest.rest()
                .given().log().all()
                .header("mbrid", mbrid)
                .header("Content-Type", "application/json").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when().log().all()
                .get(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.GET_GATEWAY_LIST + "&sid=" + sid);

        String response_body = response.getBody().asString();
        List<String> list_gw = JsonPath.from(response_body).getList("data.id");

        return  list_gw;
        }

    public void deleteGW(String id){

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
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.DELETE_GW);

        Log.info(response.getBody().asString());

        Log.highlight(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.DELETE_GW);
        Log.highlight("mbrid: " +body );
        Log.highlight("mbrid: " +mbrid );
        Log.highlight("sid: " + sid );

        Log.highlight("Delete gateway " +id + " success");
    }


    public void disableGateway(){



    }

    public List<List<String>> get_all_gateway_in_massbit(){
        Response response = SerenityRest.rest()
                .given().log().all()
                .header("Content-Type", "application/json").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when().log().all()
                .get("https://dapi.massbit.io/deploy/info/gateway/listid");

        String response_body = response.getBody().asString();
        List<List<String>> ls = UtilSteps.convertCSVFormatToList(response_body);

        return ls;
    }

    public void cleanup_gateway() throws IOException, InterruptedException {

        Log.info("Start to clean up my gateway");
        List<String> lst = listMyGateway();

        for(String gw_id : lst){
            deleteGW(gw_id);
        }

        Log.highlight("clean up my gateway done");
    }

}
