package features.massbit_route;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Community_Nodes_Steps;
import steps.api_massbit_route.Decentralized_API_Steps;
import steps.api_massbit_route.Gateway_Community_Steps;
import utilities.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SerenityRunner.class)
public class Make_Traffic_For_Gateway {

    public static List<List<String>> list_gateway = new ArrayList<>(); ;
    public static List<List<String>> list_node = new ArrayList<>(); ;

    @Steps
    Gateway_Community_Steps gateway_community_steps;

    @Steps
    Decentralized_API_Steps decentralized_api_steps;

    @Steps
    Community_Nodes_Steps community_nodes_steps;

    @Test
    public void make_traffic_for_gateway() throws IOException, InterruptedException {

        list_gateway = gateway_community_steps.get_all_gateway_in_massbit();

        decentralized_api_steps.should_be_able_to_say_hello();
        decentralized_api_steps.should_be_able_to_login();


        Log.info("list gateway size: " + list_gateway.size());
        for(int i = 0; i <2; i++) {
            for (List<String> list : list_gateway) {
                String blockchain = list.get(2);
                String ip = list.get(4);
                int status = Integer.parseInt(list.get(8));
                int approved = Integer.parseInt(list.get(9));
                if (status == 1 && approved == 1) {
                    decentralized_api_steps.should_be_able_to_create_api("traffic_gateway_test", blockchain, "mainnet");
                    decentralized_api_steps.should_be_able_to_add_entrypoint("MASSBIT");

                    decentralized_api_steps.send_api_request_direct_to_gateway(blockchain, ip);
                }
            }
        }

    }

    @Test
    public void make_traffic_for_nodes() throws IOException, InterruptedException {

        list_node = community_nodes_steps.get_all_node_in_massbit();

        decentralized_api_steps.should_be_able_to_say_hello();
        decentralized_api_steps.should_be_able_to_login();

        Log.info("list node size: " + list_node.size());

        for(int i=0; i<30; i++) {
            for (List<String> list : list_node) {
                String blockchain = list.get(2);
                String id = list.get(0);
                String x_api_key = list.get(7);
                int status = Integer.parseInt(list.get(8));
                int approved = Integer.parseInt(list.get(9));
                if (status == 1 && approved == 1) {
                    decentralized_api_steps.should_be_able_to_create_api("traffic_node_test", blockchain, "mainnet");
                    decentralized_api_steps.should_be_able_to_add_entrypoint("MASSBIT");

                    decentralized_api_steps.send_api_request_direct_to_node(blockchain, id, x_api_key);
                }
            }
        }
    }
}
