package com.example.xiu.im0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle.getBoolean("isLoginSuccess")){
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                System.out.println("Finally loginSucceed");
                String token = bundle.getString("token");
                String userName = bundle.getString("userName");
                editor.putString("userName",userName);
                editor.putString("token", token);
                editor.commit();

                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }else{
                System.out.println("Finally loginFailed");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String account = sharedPreferences.getString("userName",null);
        String token = sharedPreferences.getString("token",null);
        System.out.println(account);
        System.out.println(token);
        if (!TextUtils.isEmpty(account)&& !TextUtils.isEmpty(token)){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            System.out.println("跳转成功");
        }else {
            System.out.println("跳转不成功");
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

                    LoginThread loginThread = new LoginThread(usernameS,userpwdS,handler);
                    loginThread.start();
                }
            });
        }

    }
}
