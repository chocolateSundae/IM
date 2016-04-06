package com.example.xiu.im0;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by xiu on 16/4/6.
 */
@AVClassName("UserInfo")
public class UserInfo extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;

    private String userName;
    private String userPwd;
    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
