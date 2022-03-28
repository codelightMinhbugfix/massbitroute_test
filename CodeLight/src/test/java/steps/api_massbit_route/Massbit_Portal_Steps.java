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
    public static String username = "";
    public static String user_email = "";
    public static String user_wallet_address = "";
    public static String mbrid = "";
    public static String access_token = "";

    @Steps
    private UtilSteps utilSteps;

    // ----------------------------------------------------------------------------------------------------
    //                                              AUTH

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


    public Response request_login(String walletAddress){

        String body = "{\n" +
                "  \"walletAddress\": \"" + walletAddress + "\"\n" +
                "}";

        user_wallet_address = walletAddress;

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", mbrid)
                .when()
                .body(body)
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.LOGIN_REQUEST);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_send_request_login(String walletAddress){

        Response response = request_login(walletAddress);
        String response_body = response.getBody().asString();
        Log.info("Response of login request: " + response_body);
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(response_body.isEmpty());

        salt = JsonPath.from(response_body).getString("salt");

        return this;
    }

    public Response register(String name, String password, String pass_confirm, String email){

        username = name;
        user_email = email;

        String body = "{\n" +
                "  \"username\": \"" + name + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"passwordConfirm\": \"" + pass_confirm + "\",\n" +
                "  \"email\": \"" + email + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", mbrid)
                .when()
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.REGISTER);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_register(String name, String password, String pass_confirm, String email){

        Response response = register(name, password, pass_confirm, email);
        String response_body = response.getBody().asString();
        Log.info("Response of register: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertEquals(JsonPath.from(response_body).getString("email"), user_email);
        Assert.assertEquals(JsonPath.from(response_body).getString("username"), username);
        Assert.assertEquals(JsonPath.from(response_body).getString("walletAddress"), user_wallet_address);

        return this;
    }

    public Response login(String uname, String password){

        String body = "{\n" +
                "  \"username\": \"" + uname + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", mbrid)
                .when()
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.PORTAL_LOGIN);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_login(String uname, String password){

        Response response = login(uname, password);
        String response_body = response.getBody().asString();
        Log.info("Response of login: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(JsonPath.from(response_body).getString("accessToken").isEmpty());

        access_token = JsonPath.from(response_body).getString("accessToken");

        Log.highlight("Login with username " + uname + " successfully");
        return this;
    }

    public Response changePassword(String old_password, String new_password){

        String body = "{\n" +
                "  \"oldPassword\": \"" + old_password + "\",\n" +
                "  \"password\": \"" + new_password + "\",\n" +
                "  \"passwordConfirm\": \"" + new_password + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .header("mbrid", mbrid)
                .when()
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.PORTAL_CHANGE_PASSWORD);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_change_password(String old_password, String new_password){

        Response response = changePassword(old_password, new_password);
        String response_body = response.getBody().asString();
        Log.info("Response of change password: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("status"));

        Log.highlight("Change password successfully");
        return this;
    }

    public Response request_reset_password(String email){

        String body = "{\n" +
                "  \"email\": \"" + email + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", mbrid)
                .when()
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.REQUEST_RESET_PASSWORD);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_request_reset_password(String email){

        Response response = request_reset_password(email);
        String response_body = response.getBody().asString();
        Log.info("Response of request reset password password: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("status"));

        Log.highlight("Send request reset password successfully");
        return this;
    }

    // Need to get token by click on email
    public Response reset_password(String new_password, String refresh_token){

        String body = "{\n" +
                "  \"password\": \"" + new_password + "\",\n" +
                "  \"passwordConfirm\": \"" + new_password + "\"\n" +
                "}";
        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("mbrid", mbrid)
                .header("Authorization", refresh_token)
                .when()
                .post(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.RESET_PASSWORD);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_reset_password(String new_password, String refresh_token){

        Response response = reset_password(new_password, refresh_token);
        String response_body = response.getBody().asString();
        Log.info("Response of reset password: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertTrue(JsonPath.from(response_body).getBoolean("status"));

        Log.highlight("Reset password successfully");
        return this;
    }

    // ----------------------------------------------------------------------------------------------------
    //                                              USER

    public Response getUserInfo(){

        Response response = SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("Authorization", access_token)
                .header("mbrid", mbrid)
                .when()
                .get(utilSteps.getPortalURL()+ Massbit_Route_Endpoint.GET_USER_INFO);

        return response;
    }

    @Step
    public Massbit_Portal_Steps should_be_able_to_get_user_info(){

        Response response = getUserInfo();
        String response_body = response.getBody().asString();
        Log.info("Response of get user info: " + response_body);

        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertFalse(JsonPath.from(response_body).getString("id").isEmpty());
        Assert.assertTrue(JsonPath.from(response_body).getString("email").equalsIgnoreCase(user_email));
        Assert.assertTrue(JsonPath.from(response_body).getString("username").equalsIgnoreCase(username));

        Log.highlight("Get user info successfully");
        return this;
    }

}
