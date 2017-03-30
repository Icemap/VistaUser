package com.wqz.vistauser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseActivity;
import com.wqz.base.BaseApplication;
import com.wqz.pojo.UserHold;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.utils.UrlUtils;
import com.wqz.view.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class UserActivity extends BaseActivity
{
    TitleBar titleBar;
    EditText etHomeAddress,etWorkAddress,etProfession;
    Button btnOK;
    Spinner spAge,spIncome,spLive;
    RadioButton rbMan,rbWoman;

    String[] ageArray= {"20岁以下","21~25岁" ,"26~30岁","31~35岁","36~40岁","41~45岁",
            "46~50岁","51~55岁","55岁以上"};
    String[] incomeArray= {"4k以下","4k~5k" ,"5k~6k","6k~7k","7k~8k","8k~9k",
            "9k~10k","10k以上","不愿透露"};
    String[] liveArray= {"半年以下","半年~一年" ,"一年~两年","两年~三年","三年~四年","四年~五年",
            "五年以上","不愿透露"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initUI();
        setTitleBarParm();
    }

    void initUI()
    {
        spAge = (Spinner) findViewById(R.id.sp_user_age);
        setSpinner(spAge,ageArray);
        spIncome = (Spinner)findViewById(R.id.sp_user_income);
        setSpinner(spIncome,incomeArray);
        spLive = (Spinner)findViewById(R.id.sp_user_live);
        setSpinner(spLive,liveArray);

        etHomeAddress = (EditText)findViewById(R.id.et_user_home_address);
        etWorkAddress = (EditText)findViewById(R.id.et_user_work_address);
        etProfession = (EditText)findViewById(R.id.et_user_profession);
        titleBar = (TitleBar)findViewById(R.id.user_title_bar);
        btnOK = (Button)findViewById(R.id.btn_user_ok);
        btnOK.setOnClickListener(onClickListener);
        rbMan = (RadioButton)findViewById(R.id.rb_user_man);
        rbWoman = (RadioButton)findViewById(R.id.rb_user_woman);
    }

    void setSpinner(Spinner target,String[] itemArray)
    {
        ArrayAdapter<String> aspnCountries;
        aspnCountries = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemArray);
        aspnCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(aspnCountries);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserHold hold = new UserHold();
            hold.sex = rbMan.isChecked() ? "男" : "女";
            hold.live = liveArray[(int)spLive.getSelectedItemId()];
            hold.profession = etProfession.getText().toString();
            BaseApplication app = UserActivity.this.getBaseApplication();
            app.age = ageArray[(int)spAge.getSelectedItemId()];
            app.income = incomeArray[(int)spIncome.getSelectedItemId()];
            app.homeAddress = etHomeAddress.getText().toString();
            app.workAddress = etWorkAddress.getText().toString();
            app.hold = new Gson().toJson(hold);
            UserActivity.this.startActivity(new Intent(UserActivity.this,MainActivity.class));

//            OkHttpUtils.get().url(UrlUtils.USER_CREATE)//
//                    .addParams("age",ageArray[(int)spAge.getSelectedItemId()])
//                    .addParams("income",incomeArray[(int)spIncome.getSelectedItemId()])
//                    .addParams("homeAddress",etHomeAddress.getText().toString())
//                    .addParams("workAddress",etWorkAddress.getText().toString())
//                    .addParams("projId",getBaseApplication().getProj().getId() + "")
//                    .addParams("vistaMatrix",getBaseApplication().getRlt())
//                    .addParams("hold",new Gson().toJson(hold))
//                    .build()//
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//
//                        }
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Boolean rlt = new Gson().fromJson(response,Boolean.class);
//                            if(rlt)
//                            {
//                                Toast.makeText(UserActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
//                                //
//                                UserActivity.this.startActivity(new Intent(UserActivity.this,MainActivity.class));
//                                UserActivity.this.finish();
//                            }
//                            else
//                            {
//                                Toast.makeText(UserActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
        }
    };

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(UserActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(UserActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle("请输入您的信息");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(UserActivity.this) / 12);
//
//        titleBar.setActionTextColor(Color.WHITE);
//        titleBar.addAction(new TitleBar.TextAction("添加项目") {
//            @Override
//            public void performAction(View view) {
//            }
//        });
    }
}
