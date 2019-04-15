package com.example.musafat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void driver(View view) {
        startActivity(new Intent(this,driverlogin.class));
    }

    public void user(View view) {
        startActivity(new Intent(this,MainActivity.class));

    }

}
