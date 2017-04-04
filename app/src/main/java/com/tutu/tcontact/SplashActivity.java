package com.tutu.tcontact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {
    public static final String SESSION = "session";
    public static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        findViewById(R.id.ll_root).postDelayed(new Runnable() {
            @Override
            public void run() {
                onCheakIntent();
            }
        }, 1500);


    }


    private void onCheakIntent() {
        String session = SPUtils.getString(SESSION);

        Intent intent;

        if (TextUtils.isEmpty(session)) {

            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
