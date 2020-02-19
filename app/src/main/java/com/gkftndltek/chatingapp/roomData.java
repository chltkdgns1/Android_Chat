package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class roomData implements Serializable {
    private int number; //  방에 입장한 사람들의 숫자
    private String time; // 최근 갱신된 대화 내용의 시간
    private String roomName; // 방 제목
    private String story; // 대화 내용
    private String rid; // 룸 아이디
    private int inNumber; // 입장한 사람의 수
    // private String image; 이미지 파일 url 경로???

    public String getRid() {
        return rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
    }
    public int getInNumber() {
        return inNumber;
    }
    public void setInNumber(int inNumber) {
        this.inNumber = inNumber;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
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
