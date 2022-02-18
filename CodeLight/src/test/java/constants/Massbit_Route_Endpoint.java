package constants;

public interface Massbit_Route_Endpoint {

    // Decentralized API
    String HELLO = "/v1/hello";
    String LOGIN = "/v1?action=user.login";
    String CREATE_API = "/v1?action=api.create";
    String UPDATE_API = "/v1?action=api.update";
    String GET_API_LIST = "/v1?action=api.list";

    // Gateway Community
    String CREATE_GATEWAY = "/v1?action=gateway.create";
    String CHECK_GATEWAY = "/v1?action=gateway.get&id=";

    // Nodes Community
    String CREATE_NODE = "/v1?action=node.create";
}
