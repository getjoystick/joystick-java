package com.getjoystick.examples.model;

import java.util.Collections;
import java.util.Map;

public class ClientConfigDto {
    private String userId = "";
    private String semVer;
    private Map<Object, Object> params = Collections.emptyMap();

    public ClientConfigDto() {
        this.userId = "";
        this.params = Collections.emptyMap();
    }

    public ClientConfigDto(String userId, String semVer, Map<Object, Object> params) {
        this.setUserId(userId);
        this.semVer = semVer;
        this.setParams(params);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId != null ? userId : "";
    }

    public String getSemVer() {
        return semVer;
    }

    public void setSemVer(String semVer) {
        this.semVer = semVer;
    }

    public Map<Object, Object> getParams() {
        return params;
    }

    public void setParams(Map<Object, Object> params) {
        this.params = params != null ? params : Collections.emptyMap();
    }

}
