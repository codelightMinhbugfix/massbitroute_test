package steps.api_massbit_route;

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

public class Massbit_Portal_Steps {


    public static String salt = "";
    public static String mbrid = "";

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
    public Massbit_Portal_Steps should_be_able_to_say_hello(){

        Response response = hello();
        String response_body = response.getBody().asString();
        Log.info("response of hello is " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        mbrid = response_body;

        return this;
    }


    public Response request_login(){
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", 1)
                .when()
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.LOGIN_REQUEST);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_send_request_login(){

        Response response = request_login();
        String response_body = response.getBody().asString();
        Log.info("Response of login request " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        salt = JsonPath.from(response_body).getString("walletAddress");

        return this;
    }

}
