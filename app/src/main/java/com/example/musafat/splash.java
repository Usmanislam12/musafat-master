package com.example.musafat;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        VideoView videoView=findViewById(R.id.video);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.musafat);
        videoView.setVideoURI(video);
        videoView.start();
        Handler handler=new Handler();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(splash.this,MainActivity.class));
                finish();
            }
        };

        handler.postDelayed(runnable,12000);



    }



}

