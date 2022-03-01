package constants;

public interface Massbit_Route_Endpoint {

    // Decentralized API
    String HELLO = "/v1/hello";
    String LOGIN = "/v1?action=user.login";
    String CREATE_API = "/v1?action=api.create";
    String UPDATE_API = "/v1?action=api.update";
    String DELETE_API = "/v1?action=api.delete";
    String GET_API_LIST = "/v1?action=api.list";
    String GET_API_INFO = "/v1?action=api.get";

    // Gateway Community
    String CREATE_GATEWAY = "/v1?action=gateway.create";
    String CHECK_GATEWAY = "/v1?action=gateway.get&id=";
    String GET_GATEWAY_LIST = "/v1?action=gateway.list";

    // Nodes Community
    String CREATE_NODE = "/v1?action=node.create";
    String CHECK_NODE = "/v1?action=node.get&id=";
}
