package com.gkftndltek.chatingapp;

import java.io.Serializable;

public class UserCount implements Serializable { // 나중에 삭제할 자바 소스코드
    private int userCnt; // 페이스북 카카오톡 네이버 로그인을 제외한 유저의 카운트
    // 현재 usercnt 는 이런 주요 로그인 외의 유저들의 uid 가 됨

    public int getUserCnt() {
        return userCnt;
    }
    public void setUserCnt(int userCnt) {
        this.userCnt = userCnt;
    }
}
