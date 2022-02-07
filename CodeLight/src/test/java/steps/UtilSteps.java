package steps;

import utilities.SerenityUtil;

public class UtilSteps {

    public static String getAPIURL(){
        return SerenityUtil.getEnv("api.massbitroute.url");
    }

}
