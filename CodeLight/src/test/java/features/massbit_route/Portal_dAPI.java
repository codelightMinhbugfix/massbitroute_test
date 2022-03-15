package features.massbit_route;


import constants.Massbit_Route_Config;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.api_massbit_route.Portal_dAPI_Steps;
import utilities.Log;

import java.io.IOException;

@RunWith(SerenityRunner.class)
public class Portal_dAPI {


    enum Blockchain{ETH,NEAR,HMNY,DOT,AVAX,FTM,MATIC,BSC,SOL}
    enum Provider{MASSBIT,INFURA,GETBLOCK,QUICKNODE,CUSTOM}
    enum Network{MAINNET}

    @Steps
    Portal_dAPI_Steps portal_dAPI_steps;

    @Before
    public void prepareForTest() throws IOException, InterruptedException {

        Log.info("----------- Start Portal Community Gateway test ----------");

        portal_dAPI_steps.should_be_able_to_login(Massbit_Route_Config.uname, Massbit_Route_Config.password);

    }

    @Test
    public void create_new_project(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test");
    }

    @Test
    public void create_multiple_project(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project x");
        portal_dAPI_steps.should_be_able_to_create_new_project("project y");
    }


    @Test
    public void get_list_project(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project 1");
        portal_dAPI_steps.should_be_able_to_create_new_project("project 2");
        portal_dAPI_steps.should_be_able_to_create_new_project("project 3");
        portal_dAPI_steps.should_be_able_to_get_list_of_project();
    }

    @Test
    public void create_eth_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_eth_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth massbit");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_eth_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth infura");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_eth_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth getblock");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_eth_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth quicknode");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_eth_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth custom");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_near_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_near_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near massbit");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_near_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near infura");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_near_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near getblock");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_near_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near quicknode");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_near_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near custom");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_hmny_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_hmny_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny massbit");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_hmny_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny infura");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_hmny_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny getblock");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_hmny_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny quicknode");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_hmny_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny custom");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_dot_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_dot_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot massbit");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_dot_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot infura");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_dot_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot getblock");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_dot_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot quicknode");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void create_dot_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot custom");
        portal_dAPI_steps.should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase(), portal_dAPI_steps.getProjectId() );
        portal_dAPI_steps.should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }



}
