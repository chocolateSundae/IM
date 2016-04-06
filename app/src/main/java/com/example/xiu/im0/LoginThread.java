package com.example.xiu.im0;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

/**
 * Created by xiu on 16/4/5.
 */


public class LoginThread extends Thread {

    private String userName;
    private String userPwd;
    private String token;

    public LoginThread(String userName, String userPwd){
        this.userName = userName;
        this.userPwd = userPwd;
    }

    @Override
    public void run() {

        AVQuery<UserInfo> query = AVQuery.getQuery(UserInfo.class);
        query.whereEqualTo("userName",userName);
        query.whereEqualTo("userPwd",userPwd);
        try {
            UserInfo userInfo = query.getFirst();
            token = userInfo.getToken();
            System.out.println("token:" + token);
        } catch (AVException e) {
            e.printStackTrace();
            System.out.println("login error");
        }
//        final AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
//        query.whereEqualTo("userName", userName);
//        query.whereEqualTo("userPwd",userPwd);
//        query.findInBackground(new FindCallback<AVObject>() {
//            public void done(List<AVObject> avObjects, AVException e) {
//                if (e == null) {
//                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
//                    try {
//                        UserInfo user = avObjects.get(0);
//                    } catch (AVException e1) {
//                        e1.printStackTrace();
//                    }
//                    System.out.println("login Success");
//
//                } else {
//                    Log.d("失败", "查询错误: " + e.getMessage());
//                }
//            }
//        });

//                LoginInfo loginInfo = new LoginInfo(usernameS,userpwdS);
//                RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
//                    @Override
//                    public void onSuccess(LoginInfo loginInfo) {
//                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                    }
//
//                    @Override
//                    public void onFailed(int i) {
//                        System.out.println("loginFailed"+ i);
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//                        System.out.println("loginException" );
//                    }
//                };
//                NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
    }

}
