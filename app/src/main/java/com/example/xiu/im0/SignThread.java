package com.example.xiu.im0;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiu on 16/4/4.
 */
public class SignThread extends Thread {
    String userName;
    String userPwd;

    public SignThread(String userName, String userPwd){
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String signNIMID(String usernameS){
        String token = "unsigned";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "44d53de07b8d3ac285a7b55082b29d85";
        String appSecret = "4ba77eb2f544";
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", usernameS));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 执行请求
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 打印执行结果
        try {
            String userInfoResponse = EntityUtils.toString(response.getEntity());
            try {
                JSONObject userInfoJson = new JSONObject(userInfoResponse);
                if(userInfoJson.getString("code").equals("200")){
                   JSONObject info = userInfoJson.getJSONObject("info");
                   token = info.getString("token");
                }else {
                    System.out.println("token not 200");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    public void signLeancloud(String userName , String userPwd,String token){
        AVObject userInfo = new AVObject("UserInfo");
        userInfo.put("userName", userName);
        userInfo.put("userPwd", userPwd);
        userInfo.put("token",token);
        userInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.d("saved", "signin-success!");
                }
            }
        });
    }

    public boolean exsistUserLeanCloud(String userName){
        boolean exsit = true ;
        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("userName", userName);
        try {
           List<AVObject> userInfos = query.find();
            if(userInfos.isEmpty()){
                exsit = false;
            }else {
                exsit = true;

            }
        } catch (AVException e) {
            e.printStackTrace();
        }

        return exsit;
    }

    @Override
    public void run() {
        if(exsistUserLeanCloud(userName)){
            System.out.println("用户名已存在");
        }else{
            String token = signNIMID(userName);
            signLeancloud(userName,userPwd,token);
        }
    }
}
