package features.massbit_route;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.TestData;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.UtilSteps;
import steps.api_massbit_route.Decentralized_API_Steps;
import utilities.DataCSV;
import utilities.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@RunWith(SerenityParameterizedRunner.class)
//@UseTestDataFrom(value="data/api_info.csv")
public class DecentralizedAPI {

    @TestData
    public static Collection<Object[]> testData() throws Exception {
        Object[][] data = DataCSV.getAllDataCSV("data/api_info.csv");
        return Arrays.asList(data);
    }

    private String name;
    private String blockchain;
    private String network;

    public DecentralizedAPI(String name, String blockchain, String network){
        this.name = name;
        this.blockchain = blockchain;
        this.network = network;
    }


    @Before
    public void prepareForTest(){

    }

    @Steps
    Decentralized_API_Steps decentralized_api_steps;

    @Test
    public void massbit_route_call_api() throws IOException, InterruptedException {

        decentralized_api_steps.should_be_able_to_say_hello();
        decentralized_api_steps .should_be_able_to_login();
        decentralized_api_steps.should_be_able_to_create_api(name, blockchain, network);
        decentralized_api_steps.should_be_able_to_add_entrypoint("GETBLOCK");

        for(int i = 0; i < 3; i++){
            Thread.sleep(4000);
            decentralized_api_steps.should_be_able_to_send_api_request(blockchain);
        }

    }
//    @Test
//    public void massbit_route_api_testing() throws IOException, InterruptedException {
//        Log.info("----------- Start Decentralized API test ----------");
//        decentralized_api_steps.should_be_able_to_say_hello()
//                               .should_be_able_to_login()
//                               .should_be_able_to_create_api(name, blockchain, network)
//                               .should_be_able_to_update_api_request_limit_per_day_to_unlimited()
//                               .should_be_able_to_update_api_request_limit_per_day(99999999)
//                               .should_be_able_to_update_api_request_limit_per_day(-2)
//                               .should_be_able_to_update_api_request_limit_per_day(1)
//                               .should_be_able_to_update_api_request_limit_per_sec(999999)
//                               .should_be_able_to_update_api_request_limit_per_sec(0)
//                               .should_be_able_to_update_api_request_limit_per_sec(-2)
//                               .should_be_able_to_update_api_request_limit_per_sec(1)
////                               .should_be_able_to_update_allowlist_api_request_method()
////                               .should_be_able_to_get_api_list()
//                               .should_be_able_to_add_entrypoint("MASSBIT")
//                               .should_be_able_to_send_api_request(blockchain)
//                               .should_be_able_to_edit_entrypoint("MASSBIT_EDIT")
//                               .should_be_able_to_delete_entrypoint("DELETE")
//                               .should_be_able_to_add_entrypoint("INFURA")
//                               .should_be_able_to_send_api_request(blockchain)
//                               .should_be_able_to_edit_entrypoint("INFURA_EDIT")
//                               .should_be_able_to_delete_entrypoint("DELETE")
//                               .should_be_able_to_add_entrypoint("GETBLOCK")
//                               .should_be_able_to_send_api_request(blockchain)
//                               .should_be_able_to_edit_entrypoint("GETBLOCK_EDIT")
//                               .should_be_able_to_delete_entrypoint("DELETE");
////                               .should_be_able_to_add_entrypoint("QUICKNODE")
////                               .should_be_able_to_send_api_request(blockchain)
////                               .should_be_able_to_edit_entrypoint("QUICKNODE_EDIT")
////                               .should_be_able_to_delete_entrypoint("DELETE")
////                               .should_be_able_to_add_entrypoint("CUSTOM")
////                               .should_be_able_to_send_api_request(blockchain)
////                               .should_be_able_to_edit_entrypoint("CUSTOM_EDIT")
////                               .should_be_able_to_delete_entrypoint("DELETE");
//
//    }

}
