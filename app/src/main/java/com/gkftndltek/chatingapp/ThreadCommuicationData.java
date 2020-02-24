package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class ThreadCommuicationData implements Serializable {
    private chatdata data;
    private String tokne;

    public chatdata getData() {
        return data;
    }
    public void setData(chatdata data) {
        this.data = data;
    }
    public String getTokne() {
        return tokne;
    }
    public void setTokne(String tokne) {
        this.tokne = tokne;
    }
}
