package com.wqz.vistauser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wqz.base.BaseImmersiveActivity;

public class SplashActivity extends BaseImmersiveActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // login
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);
    }

}
