package steps.api_massbit_route;

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

public class Community_Nodes_Steps {

    @Steps
    UtilSteps utilSteps;

    public static String mbrid = "";
    public static String sid = "";
    public static JsonObject node_info;

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
    public Community_Nodes_Steps should_be_able_to_say_hello(){

        Response response = hello();
        String response_body = response.getBody().asString();
        Log.info("response of hello is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        mbrid = response_body;

        return this;
    }

    @Step
    public Community_Nodes_Steps should_be_able_to_login() throws IOException, InterruptedException {

        String body = "\n{\n" +
                "\t\"username\":\"duongqc\",\n" +
                "\t\"password\":\"duongqc\"\n" +
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

    public Response add_new_node(String name, String blockchain, String zone, String network){

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
                "    \"name\":\"" + name + "\",\n" +
                "    \"blockchain\":\"" + blockchain + "\",\n" +
                "    \"zone\":\"" + zone + "\",\n" +
                "    \"data_url\":\"" + data_url + "\",\n" +
                "    \"network\":\"" + network + "\",\n" +
                "    \"sid\":\"" + sid + "\"\n" +
                "}";

        Log.info(body);
        Log.info("url: " + utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CREATE_NODE);

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .body(body)
                .post(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CREATE_NODE);

        return response;
    }

    @Step
    public Community_Nodes_Steps should_be_able_to_add_new_node(String name, String blockchain, String zone, String network){

        Response response = add_new_node(name, blockchain, zone, network);
        String response_body = response.getBody().asString();
        Log.info("response of create node of " + blockchain + " is: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("result"));
        Assert.assertFalse(JsonPath.from(response_body).getString("data.id").isEmpty());

        node_info = JsonPath.from(response_body).getObject("data", JsonObject.class);

        return this;
    }

    public String get_install_node_script(){

        String cmd_start = "bash -c \"$(curl -sSfL '";
        String url = "https://dapi.massbit.io/api/v1/node_install?";
        String id = "id=" + JsonPath.from(node_info.toString()).getString("id");
        String user_id = "&user_id=" + JsonPath.from(node_info.toString()).getString("user_id");
        String blockchain = "&blockchain=" + JsonPath.from(node_info.toString()).getString("blockchain");
        String network = "&network=" + JsonPath.from(node_info.toString()).getString("network");
        String zone = "&zone=" + JsonPath.from(node_info.toString()).getString("zone");
        String data_url = "&data_url=" + JsonPath.from(node_info.toString()).getString("data_url");
        String cmd_end = "')\"";

        String install_script = cmd_start + url + id + user_id + blockchain + network + zone + data_url + cmd_end;


        return install_script;
    }

    public boolean nodeActive(String id){

        Response response = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json").config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("mbrid", mbrid)
                .when()
                .get(utilSteps.getAPIURL()+ Massbit_Route_Endpoint.CHECK_NODE + id + "&sid=" + sid);

        String response_body = response.getBody().asString();
        int status = JsonPath.from(response_body).getInt("data.status");

        Log.info("Node info body: " + response_body);
        if(status == 1)
        { return true; }
        else { return false; }

    }

    @Step
    public Community_Nodes_Steps should_be_able_to_activate_node_successfully() throws InterruptedException, IOException {
        int i = 0;
        while (!nodeActive(JsonPath.from(node_info.toString()).getString("id")) && i < 24){
            Thread.sleep(30000);
            i++;
            should_be_able_to_login();
        }
        Assert.assertTrue(nodeActive(JsonPath.from(node_info.toString()).getString("id")));
        Log.highlight("Node register successfully");
        return this;
    }

}
