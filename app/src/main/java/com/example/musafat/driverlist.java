package com.example.musafat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class driverlist extends BaseActivity {
    RecyclerView driver_rc;
    driveradapter adapter;
    FirebaseAuth auth;
    ArrayList<driver> drivers;
    FirebaseDatabase database;
    DatabaseReference driverref;
    DatabaseReference adminref;
    Button btn;
    String uid;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_driverlist);

        getLayoutInflater().inflate(R.layout.activity_driverlist,frameLayout);

        driver_rc = findViewById(R.id.driver_rv);
        btn = findViewById(R.id.add);
        auth = FirebaseAuth.getInstance();
        drivers = new ArrayList<>();
        uid = auth.getCurrentUser().getUid();
        adapter = new driveradapter(drivers, this);
        driver_rc.setLayoutManager(new LinearLayoutManager(this));
        driver_rc.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        driverref = database.getReference("drivers");
        adminref = database.getReference().child("admin");

        adminref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminId = dataSnapshot.child("uid").getValue(String.class);
                if (uid.equals(adminId)) {
                    btn.setVisibility(View.VISIBLE);
                    btn.setEnabled(true);


                } else {
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        driverref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                driver Driver = dataSnapshot.getValue(driver.class);
                drivers.add(Driver);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add(View view) {
         startActivity(new Intent(this, adddriver.class));

    }
}
