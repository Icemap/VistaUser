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

    List<Map<String,String>> rlt;
    List<String> urls;
    List<String> questions;
    Integer indexPano1;
    Integer indexPano2;
    Vista[] vl;

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
        seekBar.setMax(98);
        seekBar.setProgress(49);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser)
        {
            tvPano1.setText((98 - progress + 1) + "");
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
        indexPano1 = 0;
        indexPano2 = 1;

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
                        for (Vista item : vl)
                        {
                            urls.add(UrlUtils.HTML_VISTA + "?lon=" +
                                    item.getLon() + "&lat=" + item.getLat());
                            questions.add(item.getUrl());
                        }

                        resetWebViewUrl();
                    }
                });
    }

    void dataLogic()
    {
        insertRlt();

        if(urls.size() - 2 == indexPano1)
        {
            //结束
            getBaseApplication().setRlt(new Gson().toJson(rlt));
            startActivity(new Intent(MainActivity.this, UserActivity.class));
            finish();
        }
        else if(indexPano2 == urls.size() - 1)
        {
            indexPano2 = ++indexPano1 + 1;
        }
        else
        {
            indexPano2++;
        }
    }

    void resetWebViewUrl()
    {
        pano1.loadUrl(urls.get(indexPano1));
        pano2.loadUrl(urls.get(indexPano2));
        titleBar.setTitle(questions.get(indexPano1));
    }

    View.OnClickListener btnOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dataLogic();
            resetWebViewUrl();
            //SeekBar Reset
            seekBar.setProgress(49);
        }
    };

    void insertRlt()
    {
        Map<String,String> map = new HashMap<>();
        map.put("index", vl[indexPano1].getId() + ":" + vl[indexPano2].getId());
        map.put("value", tvPano1.getText().toString() + ":" + tvPano2.getText().toString());
        rlt.add(map);
    }
}
