package features.massbit_route;

import constants.Massbit_Route_Endpoint;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import steps.UtilSteps;
import utilities.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args){

        String id  = "abcxyz";
        String url = "http://" + id + ".node.mbr.massbitroute.com";



        Log.highlight("url: " + url);
//        Response response = SerenityRest.rest()
//                .given().log().all()
//                .header("Content-Type", "application/json").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
//                .when().log().all()
//                .get("https://dapi.massbit.io/deploy/info/gateway/listid");
//
//        String response_body = response.getBody().asString();
//        List<List<String>> ls = UtilSteps.convertCSVFormatToList(response_body);
//        Log.highlight("done");
    }
}
