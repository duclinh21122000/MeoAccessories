package com.example.demoaccessories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demoaccessories.views.frgament.FragmentHome;
import com.example.demoaccessories.views.frgament.FragmentMessenger;
import com.example.demoaccessories.views.frgament.FragmentNotification;
import com.example.demoaccessories.views.frgament.FragmentProfile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rlt_home, rlt_messenger, rlt_notification, rlt_profile;
    ImageView ic_home, ic_messenger, ic_notification, ic_profile;
    TextView tv_home, tv_messenger, tv_notification, tv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new FragmentHome());
        initView();
        setActiveMain();
    }

    private void initView() {
        rlt_home = findViewById(R.id.rlt_home);
        rlt_messenger = findViewById(R.id.rlt_messenger);
        rlt_notification = findViewById(R.id.rlt_notification);
        rlt_profile = findViewById(R.id.rlt_profile);
        ic_home = findViewById(R.id.ic_home);
        ic_messenger = findViewById(R.id.ic_messenger);
        ic_notification = findViewById(R.id.ic_notification);
        ic_profile = findViewById(R.id.ic_profile);
        tv_home = findViewById(R.id.tv_home);
        tv_messenger = findViewById(R.id.tv_messenger);
        tv_notification = findViewById(R.id.tv_notification);
        tv_profile = findViewById(R.id.tv_profile);
        setOnclick();
    }

    private void setOnclick() {
        rlt_home.setOnClickListener(this);
        rlt_messenger.setOnClickListener(this);
        rlt_notification.setOnClickListener(this);
        rlt_profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlt_home:
                setActive(
                        R.drawable.ic_home_1,
                        R.drawable.ic_messenger_2,
                        R.drawable.ic_notification_2,
                        R.drawable.ic_profile_2);
                setTextColor(
                        Color.parseColor("#f96506"),
                        Color.parseColor("#999999"),
                        Color.parseColor("#999999"),
                        Color.parseColor("#999999"));
                loadFragment(new FragmentHome());
                break;
            case R.id.rlt_messenger:
                setActive(
                        R.drawable.ic_home_2,
                        R.drawable.ic_messenger_1,
                        R.drawable.ic_notification_2,
                        R.drawable.ic_profile_2);
                setTextColor(
                        Color.parseColor("#999999"),
                        Color.parseColor("#f96506"),
                        Color.parseColor("#999999"),
                        Color.parseColor("#999999"));
                loadFragment(new FragmentMessenger());
                break;
            case R.id.rlt_notification:
                setActive(
                        R.drawable.ic_home_2,
                        R.drawable.ic_messenger_2,
                        R.drawable.ic_notification_1,
                        R.drawable.ic_profile_2);
                setTextColor(
                        Color.parseColor("#999999"),
                        Color.parseColor("#999999"),
                        Color.parseColor("#f96506"),
                        Color.parseColor("#999999"));
                loadFragment(new FragmentNotification());
                break;
            case R.id.rlt_profile:
                setActive(
                        R.drawable.ic_home_2,
                        R.drawable.ic_messenger_2,
                        R.drawable.ic_notification_2,
                        R.drawable.ic_profile_1);
                setTextColor(
                        Color.parseColor("#999999"),
                        Color.parseColor("#999999"),
                        Color.parseColor("#999999"),
                        Color.parseColor("#f96506"));
                loadFragment(new FragmentProfile());
                break;
        }
    }

    private void setTextColor(int parseColor, int parseColor1, int parseColor2, int parseColor3) {
        tv_home.setTextColor(parseColor);
        tv_messenger.setTextColor(parseColor1);
        tv_notification.setTextColor(parseColor2);
        tv_profile.setTextColor(parseColor3);
    }

    private void setActive(int ic1, int ic2, int ic3, int ic4) {
        ic_home.setBackgroundResource(ic1);
        ic_messenger.setBackgroundResource(ic2);
        ic_notification.setBackgroundResource(ic3);
        ic_profile.setBackgroundResource(ic4);
    }

    private void setActiveMain(){
        setActive(
                R.drawable.ic_home_1,
                R.drawable.ic_messenger_2,
                R.drawable.ic_notification_2,
                R.drawable.ic_profile_2);
        setTextColor(
                Color.parseColor("#f96506"),
                Color.parseColor("#999999"),
                Color.parseColor("#999999"),
                Color.parseColor("#999999"));
    }

    public void loadFragment (Fragment fragment){
        if (getSupportFragmentManager() != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.layout_container, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }
}