package com.example.xiu.im0;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText userpwd = (EditText)findViewById(R.id.userpwd);
        final Button signin = (Button)findViewById(R.id.signin);
        final Button login = (Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameS = username.getText().toString().toLowerCase();
                String userpwdS = userpwd.getText().toString().toLowerCase();

                LoginInfo loginInfo = new LoginInfo(usernameS,userpwdS);
                RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onFailed(int i) {
                        System.out.println("loginFailed"+ i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        System.out.println("loginException" );
                    }
                };
                NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
            }
        });
    }
}
