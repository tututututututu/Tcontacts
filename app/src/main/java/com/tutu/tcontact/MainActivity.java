package com.tutu.tcontact;


import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<ContactInfo> contactInfos;

    private View btn;

    private Gson gson;



    private String card = "";
    private RotateAnimation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card = SPUtils.getString(SplashActivity.NAME);

        gson = new Gson();
        btn = findViewById(R.id.btn);

        RxPermissions rxPermissions = new RxPermissions(this);

        //initAnimal();


        rxPermissions
                .request(Manifest.permission.READ_CONTACTS)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {


                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    contactInfos = ContactsUtils.getAllContacts();
                                    startAnimal();
                                    upload();

                                }
                            });


                        } else {
                            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startAnimal() {
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1500);//设置动画持续时间
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        btn.startAnimation(rotate);
    }


    private void stopAnimal() {
        if (rotate != null) {
            rotate.cancel();
        }
    }



    public void upload() {


        if (contactInfos.isEmpty()) {
            ToastUtils.showShortToast("通讯录为空");
            return;
        }

        if (TextUtils.isEmpty(card)) {
            ToastUtils.showShortToast("用户名为空,请重新登陆");
            return;
        }


        HashMap<String, String> params = new HashMap<>();
        params.put("datas", gson.toJson(contactInfos));
        params.put("card", card);


        OkGo.post("http://dsj.365yama.cn/upload.action")
                .tag(this)
                .upJson(GsonUtils.mapToJsonStr(params))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功

                        JSONObject jsonObject = JSON.parseObject(s);
                        String ret = jsonObject.getString("struts");


                        if (TextUtils.isEmpty(ret)) {
                            ToastUtils.showShortToast("服务器返回值错误");
                            return;
                        }

                        if ("3".equals(ret)) {
                            showSuccsess();
                        } else if ("1".equals(ret)) {
                            ToastUtils.showShortToast("请重新登录");
                        } else if ("2".equals(ret)) {
                            ToastUtils.showShortToast("上传数据为空");
                        }
                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);


                        stopAnimal();

                    }
                });
    }


    @Override
    protected void onDestroy() {

        stopAnimal();

        super.onDestroy();
    }


    private void showSuccsess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("上传成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();

    }
}
