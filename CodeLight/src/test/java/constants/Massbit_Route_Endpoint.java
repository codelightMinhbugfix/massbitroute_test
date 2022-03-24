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
    String DELETE_GW = "/v1?action=gateway.delete";

    // Nodes Community
    String CREATE_NODE = "/v1?action=node.create";
    String CHECK_NODE = "/v1?action=node.get&id=";
    String GET_NODE_LIST = "/v1?action=node.list";
    String DELETE_NODE = "/v1?action=node.delete";

    // Portal
    String LOGIN_REQUEST = "/auth/request-login";
    String REGISTER = "/auth/register";
    String PORTAL_LOGIN = "/auth/login";
    String PORTAL_CHANGE_PASSWORD = "/auth/change-password";
    String REQUEST_RESET_PASSWORD = "/auth/request-reset-password";
    String RESET_PASSWORD = "/auth/reset-password";
    String GET_USER_INFO = "/user/info";

    // Portal gateway
    String GET_GATEWAY_BY_STATUS = "/mbr/gateway/list/verify";
    String GET_MY_GATEWAY_LIST = "/mbr/gateway/list";
    String GET_GATEWAY_INFO = "/mbr/gateway/";
    String ADD_NEW_GATEWAY = "/mbr/gateway/";
    String EDIT_GATEWAY_NAME = "/mbr/gateway/";
    String DELETE_GATEWAY = "/mbr/gateway/";

    // Portal node
    String GET_NODE_BY_STATUS = "/mbr/node/list/verify";
    String GET_MY_NODE_LIST = "/mbr/node/list";
    String GET_NODE_INFO = "/mbr/node/";
    String ADD_NEW_NODE = "/mbr/node/";
    String EDIT_NODE = "/mbr/node/";
    String DELETE_PORTAL_NODE = "/mbr/node/";

    //Portal Project & dAPI
    String CREATE_NEW_PROJECT = "/mbr/d-apis/project";
    String GET_PROJECT_LIST = "/mbr/d-apis/project/list";
    String CREATE_NEW_DAPI = "/mbr/d-apis";
    String EDIT_NEW_DAPI = "/mbr/d-apis/";
    String GET_DAPI_INFO = "/mbr/d-apis/";
    String GET_DAPI_LIST_BY_PROJECTID = "/mbr/d-apis/list/";
    String CREATE_ENTRYPOINT = "/mbr/d-apis/entrypoint/";
    String EDIT_ENTRYPOINT = "/mbr/d-apis/entrypoint/";
    String DELETE_ENTRYPOINT = "/mbr/d-apis/entrypoint/";
}
