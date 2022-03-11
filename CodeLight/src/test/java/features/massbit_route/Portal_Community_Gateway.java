package features.massbit_route;

import constants.Massbit_Route_Config;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Portal_Community_Gateway_Steps;
import utilities.DataCSV;
import utilities.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(SerenityParameterizedRunner.class)
public class Portal_Community_Gateway {


    enum Zone{AS,EU,NA,SA,AF,OC}

    @TestData
    public static Collection<Object[]> testData() throws Exception {
        Object[][] data = DataCSV.getAllDataCSV("data/portal_gateway_info.csv");
        return Arrays.asList(data);
    }

    private String gateway_name;
    private String blockchain;
    private String network;

    public Portal_Community_Gateway(String gateway_name, String blockchain, String network){
        this.gateway_name = gateway_name;
        this.blockchain = blockchain;
        this.network = network;
    }

    @Steps
    Portal_Community_Gateway_Steps portal_community_gateway_steps;

    @Before
    public void prepareForTest() throws IOException, InterruptedException {

        Log.info("----------- Start Portal Community Gateway test ----------");

        portal_community_gateway_steps.should_be_able_to_login(Massbit_Route_Config.uname, Massbit_Route_Config.password)
                                      .should_be_able_to_add_new_gateway(gateway_name, blockchain, "AS", network)
                                      .should_be_able_to_add_new_gateway(gateway_name, blockchain, "AS", network)
                                      .should_be_able_to_get_gateway_by_status("created")
                                      .should_be_able_to_get_my_gateway_list()
                                      .should_be_able_to_get_gateway_info(portal_community_gateway_steps.get_gateway_id())
                                      .should_be_able_to_edit_gateway_name(portal_community_gateway_steps.get_gateway_id(),"duong")
                                      .should_be_able_to_delete_gateway(portal_community_gateway_steps.get_gateway_id());
    }

    @Test
    public void add_new_portal_gateway(){

    }
}
