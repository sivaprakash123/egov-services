package builders.eGovEIS.hrMaster.position.search;

import entities.responses.eGovEIS.hrMaster.position.search.RequestInfo;
import entities.responses.eGovEIS.hrMaster.position.search.UserInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();
    UserInfo userInfo = new UserInfoBuilder().build();

    public RequestInfoBuilder() {
        requestInfo.setUserInfo(userInfo);
        requestInfo.setApiId("1");
        requestInfo.setVer("1");
        requestInfo.setTs("01-01-2017 01:01:01");
        requestInfo.setDid("");
        requestInfo.setKey("");
        requestInfo.setMsgId("");
        requestInfo.setRequesterId("");
        requestInfo.setAuthToken("6fe58872-869c-4507-b32d-8b0f0b0e4e45");
    }

    public RequestInfoBuilder withUserInfo(UserInfo userInfo) {
        requestInfo.setUserInfo(userInfo);
        return this;
    }

    public RequestInfoBuilder withVer(String ver) {
        requestInfo.setVer(ver);
        return this;
    }

    public RequestInfoBuilder withRequesterId(String requesterId) {
        requestInfo.setRequesterId(requesterId);
        return this;
    }

    public RequestInfoBuilder withAuthToken(String authToken) {
        requestInfo.setAuthToken(authToken);
        return this;
    }

    public RequestInfoBuilder withAction(String action) {
        requestInfo.setAction(action);
        return this;
    }

    public RequestInfoBuilder withMsgId(String msgId) {
        requestInfo.setMsgId(msgId);
        return this;
    }

    public RequestInfoBuilder withApiId(String apiId) {
        requestInfo.setApiId(apiId);
        return this;
    }

    public RequestInfoBuilder withDid(String did) {
        requestInfo.setDid(did);
        return this;
    }

    public RequestInfoBuilder withKey(String key) {
        requestInfo.setKey(key);
        return this;
    }

    public RequestInfoBuilder withTs(String ts) {
        requestInfo.setTs(ts);
        return this;
    }

    public RequestInfo build() {
        return requestInfo;
    }
}
