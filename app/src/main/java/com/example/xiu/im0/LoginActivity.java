package com.example.xiu.im0;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText userpwd = (EditText)findViewById(R.id.userpwd);
        final Button signin = (Button)findViewById(R.id.signin);
        final Button login = (Button)findViewById(R.id.login);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameS = username.getText().toString().toLowerCase();
                String userpwdS = userpwd.getText().toString().toLowerCase();

                SignThread signThread = new SignThread(usernameS,userpwdS);
                signThread.start();
                //网络请求的操作要放到一个新的线程中

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameS = username.getText().toString().toLowerCase();
                String userpwdS = userpwd.getText().toString().toLowerCase();

                AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
                query.whereEqualTo("userName", usernameS);
                query.whereEqualTo("userPwd",userpwdS);
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                        } else {
                            Log.d("失败", "查询错误: " + e.getMessage());
                        }
                    }
                });

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
        });
    }
}
