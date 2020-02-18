package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class FaceBookUserData implements Serializable {
    public String profile;
    public String email;

    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
