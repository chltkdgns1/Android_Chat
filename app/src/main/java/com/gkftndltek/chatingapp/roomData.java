package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class roomData implements Serializable {
    private String pid; // 방에 입장한 사람들의 고유아이디
    private int number; //  방에 입장한 사람들의 숫자
    private int time; // 최근 갱신된 대화 내용의 시간
    private String roomName; // 방 제목
    private String story; // 대화 내용
    // private String image; 이미지 파일 url 경로???

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
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getStory() {
        return story;
    }
    public void setStory(String story) {
        this.story = story;
    }

}
