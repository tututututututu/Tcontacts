package com.tutu.tcontact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView tv_name;
    private Button btn_login;

    private String name;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = SPUtils.getString(SplashActivity.NAME);


        initView();
        initProgressBar();
    }

    private void initProgressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登陆中...");
    }


    private void initView() {

        tv_name = (AutoCompleteTextView) findViewById(R.id.tv_name);
        btn_login = (Button) findViewById(R.id.btn_login);

        if (!TextUtils.isEmpty(name)) {
            tv_name.setText(name);
            tv_name.setSelection(name.length());
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProgressBar();
                onLogin();

            }
        });
    }


    private void onLogin() {

        name = tv_name.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShortToast("请输入用户名");
            return;
        }

        progressDialog.show();
        HttpParams httpParams = new HttpParams();
        httpParams.put("card", name);


        OkGo.post("http://dsj.365yama.cn/login.action")
                .tag(this)
                .params(httpParams)
                .execute(new StringCallback() {


                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        JSONObject jsonObject = JSON.parseObject(s);
                        String ret = jsonObject.getString("struts");


                        if (TextUtils.isEmpty(ret)) {
                            ToastUtils.showShortToast("服务器返回值错误");
                            return;
                        }

                        if ("0".equals(ret)) {
                            SPUtils.putString(SplashActivity.NAME,name);
                            ToastUtils.showShortToast("登陆成功");
                            startActivity(intent);
                            finish();

                        } else if ("1".equals(ret)) {
                            ToastUtils.showShortToast("账号有误请重新登录");
                        }
                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onDestroy();
    }
}
