package com.example.xiu.im0;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.List;

/**
 * Created by xiu on 16/4/6.
 */
public class LoginThread extends Thread{

    private String userName;
    private String userPwd;
    private Handler handler;

    public LoginThread(String userName,String userPwd ,Handler handler){
        this.userName = userName;
        this.userPwd = userPwd;
        this.handler = handler;
    }

    private String token ;
    public String leanCloudLogin(String userName,String userPwd){

        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("userName", userName);
        query.whereEqualTo("userPwd", userPwd);
        try {
            List<AVObject> avObjects = query.find();
            AVObject userInfo = avObjects.get(0);
            token = userInfo.getString("token");
            System.out.println("登录成功 token:" + token);
        } catch (AVException e) {
            System.out.println("用户名密码错误");
            token = null;
            e.printStackTrace();
        }
        return token;
    }


//

    public void NIMLogin(final String userName, final String token) {
        LoginInfo info = new LoginInfo(userName,token); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用

                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        System.out.println("NIMLogin:success");
                    }

                    @Override
                    public void onFailed(int i) {
                        System.out.println("NIMLogin:failed");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        System.out.println("NIMLogin:exception");
                    }
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }


    public void sendMessageBack(boolean isLoginSuccess,String userName,String token){
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLoginSuccess",isLoginSuccess);
        bundle.putString("token", token);
        bundle.putString("userName",userName);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    boolean isLoginSuccess;
    @Override
    public void run() {
        token = leanCloudLogin(userName,userPwd);
        if (token.equals(null)){
            isLoginSuccess =  false;
            System.out.println("leanCloudLogin false");
        }else {
            System.out.println("leanCloudLogin success");
            NIMLogin(userName,token);
            isLoginSuccess = true;
        }
        sendMessageBack(isLoginSuccess,userName,token);
    }
}
