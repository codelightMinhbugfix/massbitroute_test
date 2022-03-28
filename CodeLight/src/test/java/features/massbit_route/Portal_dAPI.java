package features.massbit_route;


import constants.Massbit_Route_Config;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.After;
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
    public void create_new_project_without_name(){
        portal_dAPI_steps.should_be_able_to_create_new_project("", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase());
    }

    @Test
    public void create_new_project_without_blockchain(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project without blockchain", "", Network.MAINNET.toString().toLowerCase());
    }

    @Test
    public void create_new_project_without_network(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project without network", Blockchain.ETH.toString().toLowerCase(), "");
    }

    @Test
    public void create_new_project(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase());
    }

    @Test
    public void create_multiple_project(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project x", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_project("project y", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase());
    }


    @Test
    public void get_list_project(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project 1", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_project("project 2", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_project("project 3", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_get_list_of_project();
    }

    @Test
    public void create_eth_dAPI_without_staking(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .create_new_dAPI_without_staking("Test dAPI " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_eth_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_eth_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("eth project demo 3", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI eth " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId()  )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_eth_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth infura", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_eth_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth getblock", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_eth_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth quicknode", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_eth_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test eth custom", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.ETH.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_near_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near", Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_near_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near massbit", Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_near_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near infura", Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_near_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near getblock", Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_near_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near quicknode", Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_near_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test near custom", Blockchain.NEAR.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.NEAR.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_hmny_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny", Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_hmny_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny massbit", Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_hmny_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny infura", Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_hmny_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny getblock", Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_hmny_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny quicknode", Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_hmny_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test hmny custom", Blockchain.HMNY.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.HMNY.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_dot_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_dot_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot massbit", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_dot_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot infura", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_dot_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot getblock", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_dot_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot quicknode", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_dot_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dot custom", Blockchain.DOT.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.DOT.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_avax_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test avax", Blockchain.AVAX.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.AVAX.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_avax_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test avax massbit", Blockchain.AVAX.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.AVAX.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_avax_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test avax infura", Blockchain.AVAX.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.AVAX.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_avax_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test avax getblock", Blockchain.AVAX.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.AVAX.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_avax_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test avax quicknode", Blockchain.AVAX.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.AVAX.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_avax_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test avax custom", Blockchain.AVAX.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.AVAX.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_ftm_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test ftm", Blockchain.FTM.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.FTM.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_ftm_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test ftm massbit", Blockchain.FTM.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.FTM.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_ftm_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test ftm infura", Blockchain.FTM.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.FTM.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_ftm_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test ftm getblock", Blockchain.FTM.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.FTM.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_ftm_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test ftm quicknode", Blockchain.FTM.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.FTM.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_ftm_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test ftm custom", Blockchain.FTM.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.FTM.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_matic_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test matic", Blockchain.MATIC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.MATIC.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_matic_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test matic massbit", Blockchain.MATIC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.MATIC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_matic_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test matic infura", Blockchain.MATIC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.MATIC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_matic_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test matic getblock", Blockchain.MATIC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.MATIC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_matic_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test matic quicknode", Blockchain.MATIC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.MATIC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_matic_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test matic custom", Blockchain.MATIC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.MATIC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_bsc_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test bsc", Blockchain.BSC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.BSC.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_bsc_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test bsc massbit", Blockchain.BSC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.BSC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_bsc_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test bsc infura", Blockchain.BSC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.BSC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_bsc_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test bsc getblock", Blockchain.BSC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.BSC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_bsc_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test bsc quicknode", Blockchain.BSC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.BSC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_bsc_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test bsc custom", Blockchain.BSC.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.BSC.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_sol_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test sol", Blockchain.SOL.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.SOL.toString(), portal_dAPI_steps.getProjectId() );
    }

    @Test
    public void create_sol_dAPI_with_massbit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test sol massbit", Blockchain.SOL.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.SOL.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void create_sol_dAPI_with_infura_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test sol infura", Blockchain.SOL.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.SOL.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.INFURA.toString());
    }

    @Test
    public void create_sol_dAPI_with_getblock_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test sol getblock", Blockchain.SOL.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.SOL.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.GETBLOCK.toString());
    }

    @Test
    public void create_sol_dAPI_with_quicknode_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test sol quicknode", Blockchain.SOL.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.SOL.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.QUICKNODE.toString());
    }

    @Test
    public void create_sol_dAPI_with_custom_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test sol custom", Blockchain.SOL.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI " + Blockchain.SOL.toString(), portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.CUSTOM.toString());
    }

    @Test
    public void create_multiple_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test multiple dAPI 1", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_create_new_dAPI("Test multiple dAPI 2", portal_dAPI_steps.getProjectId() );
    }


    @Test
    public void get_list_dAPI(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI 1", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test multiple dAPI 3", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_create_new_dAPI("Test multiple dAPI 4", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_create_new_dAPI("Test multiple dAPI 5", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_create_new_dAPI("Test multiple dAPI 6", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_get_dAPI_list_by_projectId(portal_dAPI_steps.getProjectId());
    }

    @Test
    public void add_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI 2", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI 2", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString());
    }

    @Test
    public void add_multi_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI 3", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI 3", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString())
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(), Provider.GETBLOCK.toString())
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(), Provider.INFURA.toString());
    }

    @Test
    public void edit_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI 2", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI 2", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString())
                         .should_be_able_to_edit_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void delete_entrypoint(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI 2", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI 2",portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString())
                         .should_be_able_to_delete_entrypoint(portal_dAPI_steps.get_dAPI_id());
    }

    @Test
    public void combine_entrypoint_flow(){
        portal_dAPI_steps.should_be_able_to_create_new_project("project test dAPI combination 5", Blockchain.ETH.toString().toLowerCase(), Network.MAINNET.toString().toLowerCase())
                         .should_be_able_to_create_new_dAPI("Test dAPI 3", portal_dAPI_steps.getProjectId() )
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(),Provider.MASSBIT.toString())
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(), Provider.GETBLOCK.toString())
                         .should_be_able_to_add_entrypoint(portal_dAPI_steps.get_dAPI_id(), Provider.INFURA.toString())
                         .should_be_able_to_edit_entrypoint(portal_dAPI_steps.getEntrypoint_id())
                         .should_be_able_to_delete_entrypoint(portal_dAPI_steps.getEntrypoint_id());

    }

    @After
    public void clean_up_data(){

    }



}
