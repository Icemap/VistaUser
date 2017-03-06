package com.wqz.vistauser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wqz.base.BaseActivity;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.view.TitleBar;

public class EndActivity extends BaseActivity
{
    TitleBar titleBar;
    Button btnToSelect,btnToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        initUI();
    }

    void initUI()
    {
        titleBar = (TitleBar)findViewById(R.id.end_title_bar);
        btnToSelect = (Button)findViewById(R.id.btn_to_select);
        btnToMain = (Button)findViewById(R.id.btn_to_main);
        btnToMain.setOnClickListener(l);
        btnToSelect.setOnClickListener(l);

        setTitleBarParm();
    }

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(EndActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(EndActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle("项目选择");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(EndActivity.this) / 12);
//
//        titleBar.setActionTextColor(Color.WHITE);
//        titleBar.addAction(new TitleBar.TextAction("添加项目") {
//            @Override
//            public void performAction(View view) {
//            }
//        });
    }

    View.OnClickListener l = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.btn_to_main:
                    startActivity(new Intent(EndActivity.this,MainActivity.class));
                    break;
                case R.id.btn_to_select:
                    startActivity(new Intent(EndActivity.this,SelectProjActivity.class));
                    break;
            }
            EndActivity.this.finish();
        }
    };
}
