package com.example.xiu.im0;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by xiu on 16/4/6.
 */
public class LoginThread extends Thread{

    private String userName;
    private String userPwd;
    private String token;

    public LoginThread(String userName,String userPwd){
        this.userName = userName;
        this.userPwd = userPwd;
    }

    @Override
    public void run() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("userName", userName);
        query.whereEqualTo("userPwd", userPwd);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.isEmpty()){
                    System.out.println("用户名密码错误");
                }else {
                    AVObject userInfo = list.get(0);
                    token = userInfo.getString("token");
                    System.out.println("登录成功 token:" + token);
                }
            }
        });
    }
}
