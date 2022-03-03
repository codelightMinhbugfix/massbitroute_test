package utilities;

import constants.Massbit_Route_Config;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
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
    public static Response getLatestBlockFromGateway(String blockchain, String host, String ip, String dapi_secret){

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
        
        String url = String.format("http://%s/%s/",ip, dapi_secret);
        Log.info("url: " + url) ;
        Log.info("host: " + host) ;
        Log.info("body: " + body) ;
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
    public static Response getLatestBlockFromNode(String domain, String blockchain, String id, String x_api_key){

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

        String url = "http://" + id + ".node.mbr." + domain;
        Log.info("url: " + url) ;
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
}
