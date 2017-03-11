package com.tutu.tcontact;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String url = "https://www.baidu.com";

    private List<ContactInfo> contactInfos;

    private Button btn;
    private TextView tv;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv);

        RxPermissions rxPermissions = new RxPermissions(this);


        rxPermissions
                .request(Manifest.permission.READ_CONTACTS)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                            btn.setOnClickListener(MainActivity.this);
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

    @Override
    public void onClick(View v) {
        contactInfos = ContactsUtils.getAllContacts();

        tv.setText(contactInfos.toString());


        HashMap<String, String> params = new HashMap<>();
        params.put("contacts", gson.toJson(contactInfos));
        JSONObject jsonObject = new JSONObject(params);

        Log.e("tag",jsonObject.toString());


        OkGo.post(url)
                .tag(this)
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }
}
