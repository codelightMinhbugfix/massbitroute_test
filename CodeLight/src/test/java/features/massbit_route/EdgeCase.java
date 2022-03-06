package features.massbit_route;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Decentralized_API_Steps;

import java.io.IOException;

@RunWith(SerenityRunner.class)
public class EdgeCase {

    @Steps
    Decentralized_API_Steps decentralized_api_steps;

    @Before
    public void prepareForTest() throws IOException, InterruptedException {
        decentralized_api_steps.should_be_able_to_say_hello();
        decentralized_api_steps.should_be_able_to_login();
    }

    @Test
    public void call_api_over_request_limit() throws InterruptedException {
        decentralized_api_steps.should_be_able_to_create_api("test", "eth", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT")
                               .should_be_able_to_update_api_request_limit_per_day(1)
                               .should_be_able_to_send_api_request("eth");

        //Not define verification yet
        decentralized_api_steps.send_api_request_over_request_limit_per_day("eth");
    }

    @Test
    public void call_api_to_ethereum_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "eth", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("dot");
    }

    @Test
    public void call_api_to_near_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "near", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("dot");
    }

    @Test
    public void call_api_to_harmony_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "hmny", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("near");
    }

    @Test
    public void call_api_to_dot_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "dot", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("eth");
    }

    @Test
    public void call_api_to_avalanche_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "avax", "mainnet")
                .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("near");
    }

    @Test
    public void call_api_to_fantom_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "ftm", "mainnet")
                .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("near");
    }

    @Test
    public void call_api_to_polygon_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "matic", "mainnet")
                .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("near");
    }

    @Test
    public void call_api_to_bsc_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "bsc", "mainnet")
                .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("near");
    }

    @Test
    public void call_api_to_solana_with_bad_request_body() throws InterruptedException {

        decentralized_api_steps.should_be_able_to_create_api("test", "sol", "mainnet")
                .should_be_able_to_add_entrypoint("MASSBIT");

        decentralized_api_steps.send_api_request_with_bad_request_body("near");
    }

    @Test
    public void call_api_without_entrypoint() throws InterruptedException {
        decentralized_api_steps.should_be_able_to_create_api("test", "eth", "mainnet");

        //Not define verification yet
        decentralized_api_steps.send_api_request_without_entrypoint("eth");
    }

    @Test
    public void call_api_by_disable_entrypoint() throws InterruptedException {
        decentralized_api_steps.should_be_able_to_create_api("test", "eth", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT_DISABLE");

        //Not define verification yet
        decentralized_api_steps.send_api_request_without_entrypoint("eth");
    }

    @Test
    public void call_api_with_all_entrypoint_disable() throws InterruptedException {
        decentralized_api_steps.should_be_able_to_create_api("test", "eth", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT_DISABLE")
                               .should_be_able_to_add_entrypoint("GETBLOCK_DISABLE");

        //Not define verification yet
        decentralized_api_steps.send_api_request_without_entrypoint("eth");
    }

    @Test
    public void create_api_with_with_2_massbit_entrypoint() throws InterruptedException {
        decentralized_api_steps.should_be_able_to_create_api("test", "eth", "mainnet")
                               .should_be_able_to_add_entrypoint("MASSBIT")
                               .should_be_able_to_add_entrypoint("MASSBIT");

        //Not define verification yet
        decentralized_api_steps.send_api_request_without_entrypoint("eth");
    }


}
