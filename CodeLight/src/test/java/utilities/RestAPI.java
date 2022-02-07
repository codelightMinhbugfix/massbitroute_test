package utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class RestAPI {

    public static Response postApi(String baseUri, String basePath, String headerName, String headerValue, String body){

        Log.debug("Begin to run");
        Log.highlight("Request URL: " + baseUri + basePath);
        Log.highlight("Request Body: \n" + body);

        Response response = SerenityRest.rest()
                .given().contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .header(headerName,headerValue)
                .body(body)
                .when().post();

        Log.highlight("Status Code is: " + response.statusCode() + " and the Response is:");
        response.body().prettyPrint();

        return response;
    }

    public static Response getApi(String baseUri, String basePath, String headerName, String headerValue, String parameterPath){

        Log.debug("Begin to run");
        Log.highlight("Request URL: " + baseUri + basePath + parameterPath);

        Response response =  SerenityRest.rest()
                .given().contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .header(headerName,headerValue)
                .when().get(parameterPath);

        Log.highlight("Status Code is: " + response.statusCode() + " and the Response is:");
        response.body().prettyPrint();

        return response;
    }

    public static Response postApiFormData(String baseUri, String basePath, String headerName, String headerValue,
                                           String key1, String value1, String key2, String value2){

        Log.debug("Begin to run");
        Log.highlight("Request URL: " + baseUri + basePath);

        Response response = SerenityRest.rest()
                .given().contentType("multipart/form-data")
                .baseUri(baseUri)
                .basePath(basePath)
                .header(headerName,headerValue)
                .multiPart(key1,value1)
                .multiPart(key2,value2)
                .when().post();

        Log.highlight("Status Code is: " + response.statusCode() + " and the Response is:");
        response.body().prettyPrint();

        return response;
    }

    public static Response patchApi(String baseUri, String basePath, String headerName, String headerValue, String body){

        Log.debug("Begin to run");
        Log.highlight("Request URL: " + baseUri + basePath);
        Log.highlight("Request Body: \n" + body);

        Response response = SerenityRest.rest()
                .given().contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .header(headerName,headerValue)
                .body(body)
                .when().patch();

        Log.highlight("Status Code is: " + response.statusCode() + " and the Response is:");
        response.body().prettyPrint();

        return response;
    }
}
