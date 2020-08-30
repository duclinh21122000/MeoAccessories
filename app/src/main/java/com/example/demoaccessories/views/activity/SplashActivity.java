package com.example.demoaccessories.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.demoaccessories.R;

public class SplashActivity extends AppCompatActivity {
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        intentActivity();
    }

    private void intentActivity() {
        thread = new Thread(){
            @Override
            public void run() {
                super.run();
                int waited = 0;
                while (waited < 3000){
                    try {
                        sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    waited +=100;
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
                startActivity(intent);
                finish();
            }
        };
        thread.start();
    }
}