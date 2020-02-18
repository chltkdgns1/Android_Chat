package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class roomData implements Serializable {
    private String pid; // 방에 입장한 사람들의 고유아이디
    private int number; //  방에 입장한 사람들의 숫자
    private chatdata data;

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public chatdata getData() {
        return data;
    }
    public void setData(chatdata data) {
        this.data = data;
    }
}
