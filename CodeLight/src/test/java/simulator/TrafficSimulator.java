package simulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.JsonObject;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import steps.UtilSteps;
import steps.api_massbit_route.Community_Nodes_Steps;
import steps.api_massbit_route.Decentralized_API_Steps;
import steps.api_massbit_route.Gateway_Community_Steps;
import utilities.Log;
import utilities.RestAPI;

@RunWith(SerenityRunner.class)
public class TrafficSimulator {

	public static List<NodeInfo>  listGateways = new ArrayList<>(); ;
    public static List<NodeInfo> listNodes = new ArrayList<>(); ;
    Map<String, JsonObject> mapApiInfos = new HashMap<String, JsonObject>();
    
    @Steps
    UtilSteps utilSteps;
    
    @Steps
    Gateway_Community_Steps gateway_community_steps;

    @Steps
    Decentralized_API_Steps decentralized_api_steps;

    @Steps
    Community_Nodes_Steps community_nodes_steps;

    @Test
    public void simulate_traffic() throws IOException, InterruptedException {
        decentralized_api_steps.should_be_able_to_say_hello();
        decentralized_api_steps.should_be_able_to_login();
        
        //decentralized_api_steps.should_be_able_to_add_entrypoint("MASSBIT");
        long cycle = 0;
        Random random = new Random(123123);
        String domain  = utilSteps.getEnvironmentDomain();
        while(true) {
        	//Reload node lists and gateway list every 10 cycles
        	if (cycle % 10 == 0) {
        		listNodes = getAvailableNodes();
        		listGateways = getAvailableGateways();
        	}
            for (NodeInfo gw : listGateways) {
//            	JsonObject apiInfo = getApiInfo(gw.getBlockchain());
//            	String domain = apiInfo.get("gateway_domain").getAsString();
//            	String secret = apiInfo.get("api_key").getAsString();
            	//decentralized_api_steps.send_api_request_direct_to_gateway(gw.getBlockchain(), gw.getIp());
            	try {
            		RestAPI.getLatestBlockFromGateway(domain, gw);
            	} catch(Exception e) {
            		e.printStackTrace();
            	}
            }
            for (NodeInfo node : listNodes) { 
            	decentralized_api_steps.send_api_request_direct_to_node(node.getBlockchain(), node.getId(), node.getApiKey());
            }
            //Sleep for maximize 60s
            long sleepTime = Math.abs(random.nextLong()) % 60000;
            if (sleepTime < 10000) {
            	sleepTime = 10000;
            }
            Log.info(String.format("Sleep for %d s...", sleepTime));
            Thread.sleep(sleepTime);
        }
    }
    private JsonObject getApiInfo(String blockchain) {
    	JsonObject apiInfo = mapApiInfos.get(blockchain);
    	if (apiInfo == null) {
    		//Create new api
    		decentralized_api_steps.should_be_able_to_create_api("traffic_gateway_test", blockchain, "mainnet");
    		decentralized_api_steps.should_be_able_to_add_entrypoint("MASSBIT");
    		apiInfo = decentralized_api_steps.api_info;
    		mapApiInfos.put(blockchain, apiInfo);
    	}
    	return apiInfo;
    }
    private List<NodeInfo> getAvailableNodes() {
    	List<NodeInfo> listNodes = new ArrayList<NodeInfo>();
    	List<List<String>> list_nodes = community_nodes_steps.get_all_node_in_massbit();
    	if (list_nodes != null && list_nodes.size() > 0) {
        	for (List<String> list : list_nodes) {
                String id = list.get(0);
                String userId = list.get(1);
                String blockchain = list.get(2);
                String ip = list.get(4);
                String x_api_key = list.get(7);
                int status = Integer.parseInt(list.get(8));
                int approved = Integer.parseInt(list.get(9));
                if (status == 1 && approved == 1) {
                	listNodes.add(new NodeInfo(blockchain, id, userId, ip, x_api_key));
                }
        	}
        }
    	Log.info(String.format("Avaiable nodes: %d, Active nodes: %d", list_nodes.size(), listNodes.size()));
    	return listNodes;
    }
    private List<NodeInfo> getAvailableGateways() {
    	List<List<String>> list_gateway = gateway_community_steps.get_all_gateway_in_massbit();
        List<NodeInfo> listGateWays = new ArrayList<NodeInfo>();
        if (list_gateway != null && list_gateway.size() > 0) {
        	for (List<String> list : list_gateway) {
        		String id = list.get(0);
        		String userId = list.get(1);
        		String blockchain = list.get(2);
                String ip = list.get(4);
                String x_api_key = list.get(7);
                int status = Integer.parseInt(list.get(8));
                int approved = Integer.parseInt(list.get(9));
                if (status == 1 && approved == 1) {
                	NodeInfo nodeInfo = new NodeInfo(blockchain, id, userId, ip, x_api_key);
                	nodeInfo.setIp(ip);
                	listGateWays.add(nodeInfo);
                }
        	}
        }
        Log.info(String.format("Avaiable gateways: %d, active ones: %d", list_gateway.size(), listGateWays.size()));
        return listGateWays;
    }
    public static class NodeInfo {
    	String blockchain;
    	String id;
    	String userId;
    	String ip;
    	String apiKey;
    	
		public NodeInfo(String blockchain, String id, String userId, String ip, String apiKey) {
			super();
			this.blockchain = blockchain;
			this.userId = userId;
			this.id = id;
			this.ip = ip;
			this.apiKey = apiKey;
		}
		
		public String getBlockchain() {
			return blockchain;
		}
		public void setBlockchain(String blockchain) {
			this.blockchain = blockchain;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getApiKey() {
			return apiKey;
		}
		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}
    	
    }
}
