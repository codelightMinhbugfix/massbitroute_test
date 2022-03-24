package constants;

public interface Massbit_Route_Config {

    // Body of request to add entrypoint
    String ENTRYPOINT_MASSBIT = "{\"id\":1642751098000,\"type\":\"MASSBIT\",\"priority\":2,\"status\":1,\"backup\":1}";
    String ENTRYPOINT_MASSBIT_2 = "{\"id\":1642751098001,\"type\":\"MASSBIT\",\"priority\":2,\"status\":1,\"backup\":0}";
    String ENTRYPOINT_INFURA = "{\"id\":1642996420000,\"type\":\"INFURA\",\"priority\":1,\"status\":1,\"project_id\":\"b2e4635e55a448bc8fa9bb651d675208\",\"project_secret\":\"c893942ccbc94960848e4a33e91735d6\"}";
    String ENTRYPOINT_GETBLOCK = "{\"id\":1642996653000,\"type\":\"GETBLOCK\",\"priority\":2,\"status\":1,\"api_key\":\"de025584-88f9-4608-8508-021d0973c2f8\"}";
    String ENTRYPOINT_QUICKNODE = "{\"id\":1642996777000,\"type\":\"QUICKNODE\",\"priority\":1,\"status\":0,\"api_uri\":\"API_URI_Quicknode_fake\"}";
    String ENTRYPOINT_CUSTOM = "{\"id\":1642999215000,\"type\":\"CUSTOM\",\"priority\":4,\"status\":0,\"backup\":1,\"api_uri\":\"api_uri_fake\"}";

    String ENTRYPOINT_MASSBIT_EDIT = "{\"id\":1642751098000,\"type\":\"MASSBIT\",\"priority\":1,\"status\":1,\"backup\":0}";
    String ENTRYPOINT_INFURA_EDIT = "{\"id\":1642996420000,\"type\":\"INFURA\",\"priority\":2,\"status\":0,\"project_id\":\"b2e4635e55a448bc8fa9bb651d675208\",\"project_secret\":\"c893942ccbc94960848e4a33e91735d6\"}";
    String ENTRYPOINT_GETBLOCK_EDIT = "{\"id\":1642996653000,\"type\":\"GETBLOCK\",\"priority\":1,\"status\":0,\"api_key\":\"de025584-88f9-4608-8508-021d0973c2f8\"}";
    String ENTRYPOINT_QUICKNODE_EDIT = "{\"id\":1642996777000,\"type\":\"QUICKNODE\",\"priority\":2,\"status\":1,\"api_uri\":\"edit API_URI_Quicknode_fake\"}";
    String ENTRYPOINT_CUSTOM_EDIT = "{\"id\":1642999215000,\"type\":\"CUSTOM\",\"priority\":2,\"status\":1,\"backup\":0,\"api_uri\":\"edit api_uri_fake\"}";

    String ENTRYPOINT_MASSBIT_DISABLE = "{\"id\":1642751098000,\"type\":\"MASSBIT\",\"priority\":1,\"status\":0,\"backup\":0}";
    String ENTRYPOINT_INFURA_DISABLE = "{\"id\":1642996420000,\"type\":\"INFURA\",\"priority\":2,\"status\":0,\"project_id\":\"b2e4635e55a448bc8fa9bb651d675208\",\"project_secret\":\"c893942ccbc94960848e4a33e91735d6\"}";
    String ENTRYPOINT_GETBLOCK_DISABLE = "{\"id\":1642996653000,\"type\":\"GETBLOCK\",\"priority\":1,\"status\":0,\"api_key\":\"de025584-88f9-4608-8508-021d0973c2f8\"}";
    String ENTRYPOINT_QUICKNODE_DISABLE = "{\"id\":1642996777000,\"type\":\"QUICKNODE\",\"priority\":2,\"status\":0,\"api_uri\":\"edit API_URI_Quicknode_fake\"}";
    String ENTRYPOINT_CUSTOM_DISABLE = "{\"id\":1642999215000,\"type\":\"CUSTOM\",\"priority\":2,\"status\":0,\"backup\":0,\"api_uri\":\"edit api_uri_fake\"}";

    // Body of request to blockchain

    String ETHEREUM = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_getBlockByNumber\",\"params\":[\"latest\",true]}";
    String NEAR = "{\"method\": \"block\",\"jsonrpc\": \"2.0\",\"id\": \"dontcare\",\"params\": {\"block_id\": 17764600}}";
    String HARMONY = "{\"jsonrpc\": \"2.0\",\"id\": 1,\"method\": \"hmyv2_call\",\"params\": [{\"to\": \"0x08AE1abFE01aEA60a47663bCe0794eCCD5763c19\"},370000]}";
//    String POLKADOT = "{\"jsonrpc\":\"2.0\",\"result\":{\"block\":{\"extrinsics\":[\"0x280402000b50055ee97001\",\"0x1004140000\"],\"header\":{\"digest\":{\"logs\":[\"0x06424142453402af000000937fbd0f00000000\",\"0x054241424501011e38401b0aab22f4d72ebc95329c3798445786b92ca1ae69366aacb6e1584851f5fcdfcc0f518df121265c343059c62ab0a34e8e88fda8578810fbe508b6f583\"]},\"extrinsicsRoot\":\"0x0e354333c062892e774898e7ff5e23bf1cdd8314755fac15079e25c1a7765f06\",\"number\":\"0x16c28c\",\"parentHash\":\"0xe3bf2e8f0e901c292de24d07ebc412d67224ce52a3d1ffae76dc4bd78351e8ac\",\"stateRoot\":\"0xd582f0dfeb6a7c73c47db735ae82d37fbeb5bada67ee8abcd43479df0f8fc8d8\"}},\"justification\":null},\"id\":1}";
//    String POLKADOT = "{\"jsonrpc\": \"2.0\",\"method\": \"chain_getBlockHash\",\"params\": [],\"id\": 1}";
    String POLKADOT = "{\"jsonrpc\": \"2.0\",\"method\": \"chain_getBlockHash\",\"params\": [],\"id\": 1}";
    String AVALANCHE = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\" :\"eth_baseFee\",\"params\" :[]}";
    String FANTOM = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_getBlockByNumber\",\"params\":[\"latest\", true]}";
    String POLYGON = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_blockNumber\",\"params\":[]}";
//    String BSC = "{\"method\": \"broadcast_tx_sync\",\"jsonrpc\": \"2.0\",\"params\": [\"abc\"],\"id\": \"dontcare\"}";
    String BSC = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_getBlockByNumber\",\"params\":[\"latest\",true]}";
    String SOLANA = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"getBlock\",\"params\":[430, {\"encoding\": \"json\",\"transactionDetails\":\"full\",\"rewards\":false}]}";

    // data source of node

    String DATA_URL_ETHEREUM = "http://34.124.230.213:8545";
    String DATA_URL_NEAR = "";
    String DATA_URL_HARMONY = "";
    String DATA_URL_POLKADOT = "https://34.87.170.136";
    String DATA_URL_AVALANCHE = "";
    String DATA_URL_FANTOM = "";
    String DATA_URL_POLYGON = "";
    String DATA_URL_BSC = "";
    String DATA_URL_SOLANA = "";

    // Gateway
    String GW_PATH_TERRAFORM_APPLY = "terraform_gateway/cmTerraformApply.sh";
    String GW_PATH_TERRAFORM_DESTROY = "terraform_gateway/cmTerraformDestroy.sh";
    String GW_PATH_TERRAFORM_INIT = "terraform_gateway/init.sh";
    //install script
    String portal_url = "https://portal.massbitroute.dev&env=dev";

    // Node
    String NODE_PATH_TERRAFORM_APPLY = "terraform_node/cmTerraformApply.sh";
    String NODE_PATH_TERRAFORM_DESTROY = "terraform_node/cmTerraformDestroy.sh";
    String NODE_PATH_TERRAFORM_INIT = "terraform_node/init.sh";

    //User
    String uname = "duong";
    String password = "Duong123";

    String uname_2 = "mison201";
    String password_2 = "Tuong111";

    // Portal Gateway
    String PORTAL_GW_PATH_TERRAFORM_APPLY = "terraform_portal_gateway/cmTerraformApply.sh";
    String PORTAL_GW_PATH_TERRAFORM_DESTROY = "terraform_portal_gateway/cmTerraformDestroy.sh";
    String PORTAL_GW_PATH_TERRAFORM_INIT = "terraform_portal_gateway/init.sh";

    // Portal node
    String PORTAL_NODE_PATH_TERRAFORM_APPLY = "terraform_portal_node/cmTerraformApply.sh";
    String PORTAL_NODE_PATH_TERRAFORM_DESTROY = "terraform_portal_node/cmTerraformDestroy.sh";
    String PORTAL_NODE_PATH_TERRAFORM_INIT = "terraform_portal_node/init.sh";
    String NEW_NAME = "new name";
    String NEW_DATA_SOURCE = "https://127.0.0.1:8545";

    // Portal dApi entrypoint provider
    String PORTAL_ENTRYPOINT_MASSBIT = "{\"provider\":\"MASSBIT\",\"priority\":1,\"status\":1,\"providerConfig\":{}}";
    String PORTAL_ENTRYPOINT_MASSBIT_EDITED = "{\"provider\":\"MASSBIT\",\"priority\":2,\"status\":1,\"providerConfig\":{}}";
    String PORTAL_ENTRYPOINT_INFURA = "{\"provider\":\"INFURA\",\"priority\":1,\"status\":1,\"providerConfig\":{\"project_id\":\"b2e4635e55a448bc8fa9bb651d675208\",\"project_secret\":\"c893942ccbc94960848e4a33e91735d6\"}}";
    String PORTAL_ENTRYPOINT_GETBLOCK = "{\"provider\":\"GETBLOCK\",\"priority\":1,\"status\":1,\"providerConfig\":{\"api_key\":\"de025584-88f9-4608-8508-021d0973c2f8\"}}";
    String PORTAL_ENTRYPOINT_QUICKNODE = "{\"provider\":\"QUICKNODE\",\"priority\":1,\"status\":1,\"providerConfig\":{\"api_uri\":\"API_URI_fake\"}}";
    String PORTAL_ENTRYPOINT_CUSTOM = "{\"provider\":\"CUSTOM\",\"priority\":1,\"status\":1,\"providerConfig\":{\"api_uri\":\"api_uri_fake\"}}";


}
