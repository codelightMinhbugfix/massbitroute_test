package constants;

public interface Massbit_Route_Config {

    // Body of request to add entrypoint
    String ENTRYPOINT_MASSBIT = "{\"id\":1642751098000,\"type\":\"MASSBIT\",\"priority\":2,\"status\":0,\"backup\":1}";
    String ENTRYPOINT_INFURA = "{\"id\":1642996420000,\"type\":\"INFURA\",\"priority\":1,\"status\":1,\"project_id\":\"project id Infura fake\",\"project_secret\":\"project secret Infura fake\"}";
    String ENTRYPOINT_GETBLOCK = "{\"id\":1642996653000,\"type\":\"GETBLOCK\",\"priority\":2,\"status\":1,\"api_key\":\"API key Getblock fake\"}";
    String ENTRYPOINT_QUICKNODE = "{\"id\":1642996777000,\"type\":\"QUICKNODE\",\"priority\":1,\"status\":0,\"api_uri\":\"API_URI_Quicknode_fake\"}";
    String ENTRYPOINT_CUSTOM = "{\"id\":1642999215000,\"type\":\"CUSTOM\",\"priority\":4,\"status\":0,\"backup\":1,\"api_uri\":\"api_uri_fake\"}";

    String ENTRYPOINT_MASSBIT_EDIT = "{\"id\":1642751098000,\"type\":\"MASSBIT\",\"priority\":1,\"status\":1,\"backup\":0}";
    String ENTRYPOINT_INFURA_EDIT = "{\"id\":1642996420000,\"type\":\"INFURA\",\"priority\":2,\"status\":0,\"project_id\":\"edit project id Infura fake\",\"project_secret\":\"edit project secret Infura fake\"}";
    String ENTRYPOINT_GETBLOCK_EDIT = "{\"id\":1642996653000,\"type\":\"GETBLOCK\",\"priority\":1,\"status\":0,\"api_key\":\"edit API key Getblock fake\"}";
    String ENTRYPOINT_QUICKNODE_EDIT = "{\"id\":1642996777000,\"type\":\"QUICKNODE\",\"priority\":2,\"status\":1,\"api_uri\":\"edit API_URI_Quicknode_fake\"}";
    String ENTRYPOINT_CUSTOM_EDIT = "{\"id\":1642999215000,\"type\":\"CUSTOM\",\"priority\":2,\"status\":1,\"backup\":0,\"api_uri\":\"edit api_uri_fake\"}";

    // Body of request to blockchain

    String ETHEREUM = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_getBlockByNumber\",\"params\":[\"latest\",true]}";
    String NEAR = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_blockNumber\",\"params\":[]}";
    String HARMONY = "{\"jsonrpc\": \"2.0\",\"id\": 1,\"method\": \"hmyv2_call\",\"params\": [{\"to\": \"0x08AE1abFE01aEA60a47663bCe0794eCCD5763c19\"},370000]}";
    String POLKADOT = "{\"jsonrpc\":\"2.0\",\"result\":{\"methods\":[\"account_nextIndex\",\"author_hasKey\",\"author_hasSessionKeys\",\"author_insertKey\",\"author_pendingExtrinsics\",\"author_removeExtrinsic\",\"author_rotateKeys\",\"author_submitAndWatchExtrinsic\",\"author_submitExtrinsic\",\"author_unwatchExtrinsic\",\"chain_getBlock\",\"chain_getBlockHash\",\"chain_getFinalisedHead\",\"chain_getFinalizedHead\",\"chain_getHead\",\"chain_getHeader\",\"chain_getRuntimeVersion\",\"chain_subscribeAllHeads\",\"chain_subscribeFinalisedHeads\",\"chain_subscribeFinalizedHeads\",\"chain_subscribeNewHead\",\"chain_subscribeNewHeads\",\"chain_subscribeRuntimeVersion\",\"chain_unsubscribeAllHeads\",\"chain_unsubscribeFinalisedHeads\",\"chain_unsubscribeFinalizedHeads\",\"chain_unsubscribeNewHead\",\"chain_unsubscribeNewHeads\",\"chain_unsubscribeRuntimeVersion\",\"offchain_localStorageGet\",\"offchain_localStorageSet\",\"payment_queryInfo\",\"state_call\",\"state_callAt\",\"state_getChildKeys\",\"state_getChildStorage\",\"state_getChildStorageHash\",\"state_getChildStorageSize\",\"state_getKeys\",\"state_getKeysPaged\",\"state_getKeysPagedAt\",\"state_getMetadata\",\"state_getPairs\",\"state_getRuntimeVersion\",\"state_getStorage\",\"state_getStorageAt\",\"state_getStorageHash\",\"state_getStorageHashAt\",\"state_getStorageSize\",\"state_getStorageSizeAt\",\"state_queryStorage\",\"state_subscribeRuntimeVersion\",\"state_subscribeStorage\",\"state_unsubscribeRuntimeVersion\",\"state_unsubscribeStorage\",\"subscribe_newHead\",\"system_accountNextIndex\",\"system_addReservedPeer\",\"system_chain\",\"system_health\",\"system_name\",\"system_networkState\",\"system_nodeRoles\",\"system_peers\",\"system_properties\",\"system_removeReservedPeer\",\"system_version\",\"unsubscribe_newHead\"],\"version\":1},\"id\":1}";
    String AVALANCHE = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\" :\"eth_baseFee\",\"params\" :[]}";
    String FANTOM = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_getBlockByNumber\",\"params\":[\"latest\", true]}";
    String POLYGON = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"eth_blockNumber\",\"params\":[]}";
    String BSC = "";
    String SOLANA = "{\"jsonrpc\": \"2.0\",\"id\":1,\"method\":\"getBlock\",\"params\":[430, {\"encoding\": \"json\",\"transactionDetails\":\"full\",\"rewards\":false}]}";
}
