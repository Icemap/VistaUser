package com.wqz.vistauser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseActivity;
import com.wqz.base.BaseImmersiveActivity;
import com.wqz.pojo.Manager;
import com.wqz.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LoginActivity extends BaseImmersiveActivity
{
    EditText etUserName,etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    void initUI()
    {
        etUserName = (EditText)findViewById(R.id.et_login_username);
        etPassword = (EditText)findViewById(R.id.et_login_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            OkHttpUtils.get().url(UrlUtils.MANAGER_LOGIN)//
                    .addParams("username",etUserName.getText().toString())
                    .addParams("password",etPassword.getText().toString())
                    .build()//
                    .execute(new MyStringCallback());
        }
    };


    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id)
        {
            if(response == null || response.equals(""))
            {
                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            //把Manager信息放入Application中
            getBaseApplication().setManager(new Gson().fromJson(response,Manager.class));
            String s = getBaseApplication().getManager().toString();
            startActivity(new Intent(LoginActivity.this,SelectProjActivity.class));
            overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }
}
