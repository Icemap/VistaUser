package com.wqz.vistauser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseActivity;
import com.wqz.base.BaseApplication;
import com.wqz.pojo.Vista;
import com.wqz.utils.RandomUtils;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.utils.UrlUtils;
import com.wqz.view.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends BaseActivity
{
    TitleBar titleBar;
    SeekBar seekBar;
    WebView pano1, pano2;
    TextView tvPano1, tvPano2;
    Button btnOK;
    TitleBar.Action action;

    List<Map<String,String>> rlt;
    List<String> urls;
    List<String> questions;
    Integer indexPano;
    Vista[] vl;

    Integer max = 8,center = 4;

    List<Integer> indexPano1List,indexPano2List;
    List<Integer> panoIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    void initView()
    {
        seekBar = (SeekBar) findViewById(R.id.vsb_like);
        pano1 = (WebView)findViewById(R.id.wv_pano1);
        pano2 = (WebView)findViewById(R.id.wv_pano2);
        tvPano1 = (TextView)findViewById(R.id.tv_pano1);
        tvPano2 = (TextView)findViewById(R.id.tv_pano2);
        btnOK = (Button)findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(btnOkListener);
        titleBar = (TitleBar)findViewById(R.id.main_title_bar);

        titleBarInit();
        webViewInit();
        seekBarInit();
    }

    void titleBarInit()
    {
        titleBar.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(MainActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle("");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(MainActivity.this) / 12);
        titleBar.setActionTextColor(Color.WHITE);
    }

    void webViewInit()
    {
        pano1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        WebSettings settings = pano1.getSettings();
        settings.setJavaScriptEnabled(true);

        pano2.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        settings = pano2.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    void seekBarInit()
    {
        seekBar.setMax(max);
        seekBar.setProgress(center);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser)
        {
            tvPano1.setText((max - progress + 1) + "");
            tvPano2.setText((progress + 1) + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    void initData()
    {
        indexPano = 0;

        rlt = new ArrayList<>();

        OkHttpUtils.get().url(UrlUtils.VISTA_SELECT_BY_PROJID)//
                .addParams("projId",getBaseApplication().getProj().getId().toString())
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(response.equals("[]"))
                        {
                            Toast.makeText(MainActivity.this,"该工程没有配置全景",Toast.LENGTH_SHORT).show();
                            MainActivity.this.finish();
                        }

                        vl = new Gson().fromJson(response,Vista[].class);
                        if(vl.length < 2)
                        {
                            Toast.makeText(MainActivity.this,"该工程全景数量不足两个，无法比较",Toast.LENGTH_SHORT).show();
                            MainActivity.this.finish();
                        }


                        urls = new ArrayList<>();
                        questions = new ArrayList<>();
                        panoIdList = new ArrayList<>();

                        for (Vista item : vl)
                        {
                            urls.add(UrlUtils.HTML_VISTA + "?lon=" +
                                    item.getLon() + "&lat=" + item.getLat());
                            questions.add(item.getUrl());
                            panoIdList.add(item.getId());
                        }

                        indexPano1List = new ArrayList<>();
                        indexPano2List = new ArrayList<>();
                        for(int i = 0;i < panoIdList.size() - 1;i++)
                        {
                            for(int j = i + 1;j < panoIdList.size();j++)
                            {
                                indexPano1List.add(i);
                                indexPano2List.add(j);
                            }
                        }
                        //
                        RandomUtils.randomTwoList(indexPano1List,indexPano2List);

                        resetWebViewUrl();
                    }
                });
    }

    boolean lastIsAdd = false;
    void dataLogic()
    {
        if(indexPano == indexPano1List.size() - 1)
        {
            if(!lastIsAdd)
            {
                insertRlt();
                lastIsAdd = true;
            }
            endProj();
        }
        else
        {
            insertRlt();
            indexPano++;
        }
    }

    void resetWebViewUrl()
    {
        Toast.makeText(MainActivity.this,indexPano1List.get(indexPano) + ":" + indexPano2List.get(indexPano),Toast.LENGTH_SHORT).show();
        pano1.loadUrl(urls.get(indexPano1List.get(indexPano)));
        pano2.loadUrl(urls.get(indexPano2List.get(indexPano)));
        titleBar.setTitle(questions.get(indexPano1List.get(indexPano)));
    }

    View.OnClickListener btnOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dataLogic();
            resetWebViewUrl();
            //SeekBar Reset
            seekBar.setProgress(center);
        }
    };

    void insertRlt()
    {
            Map<String,String> map = new HashMap<>();
            map.put("index", vl[indexPano1List.get(indexPano)].getId() + ":" + vl[indexPano2List.get(indexPano)].getId());
            map.put("value", tvPano1.getText().toString() + ":" + tvPano2.getText().toString());
            rlt.add(map);
    }

    void endProj()
    {
//        if(rlt.isEmpty())
//        {
//            Toast.makeText(MainActivity.this,"在一个答案都没选的时候是不能结束的=。=",Toast.LENGTH_SHORT).show();
//            return;
//        }

        BaseApplication app = MainActivity.this.getBaseApplication();
        OkHttpUtils.get().url(UrlUtils.USER_CREATE)//
                    .addParams("age",app.age)
                    .addParams("income",app.income)
                    .addParams("homeAddress",app.homeAddress)
                    .addParams("workAddress",app.workAddress)
                    .addParams("projId",app.getProj().getId() + "")
                    .addParams("vistaMatrix",new Gson().toJson(rlt))
                    .addParams("hold",app.hold)
                    .build()//
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            Toast.makeText(MainActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
                            btnOK.setVisibility(View.VISIBLE);

                            if(action == null)
                            {
                                action =  new TitleBar.TextAction("重新提交") {
                                    @Override
                                    public void performAction(View view) {
                                        endProj();
                                    }
                                };
                                titleBar.addAction(action);
                            }
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Boolean rlt = new Gson().fromJson(response,Boolean.class);
                            if(rlt)
                            {
                                Toast.makeText(MainActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                                //
                                MainActivity.this.startActivity(new Intent(MainActivity.this,EndActivity.class));
                                MainActivity.this.finish();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
                                btnOK.setVisibility(View.VISIBLE);
                                if(action == null)
                                {
                                    action =  new TitleBar.TextAction("重新提交") {
                                        @Override
                                        public void performAction(View view) {
                                            endProj();
                                        }
                                    };
                                    titleBar.addAction(action);
                                }
                            }
                        }
                    });
    }
}
