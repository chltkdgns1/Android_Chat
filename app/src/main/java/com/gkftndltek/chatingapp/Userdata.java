package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class Userdata implements Serializable {
    private String username;
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
