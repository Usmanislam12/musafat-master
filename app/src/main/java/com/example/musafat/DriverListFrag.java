package com.example.musafat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverListFrag extends Fragment {

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



    public DriverListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_driver_list, BaseActivity.frameLayout, false);

        driver_rc = view.findViewById(R.id.driver_rv);
        btn = view.findViewById(R.id.add);
        auth = FirebaseAuth.getInstance();
        drivers = new ArrayList<>();
        uid = auth.getCurrentUser().getUid();
        adapter = new driveradapter(drivers, getContext());
        driver_rc.setLayoutManager(new LinearLayoutManager(getContext()));
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), adddriver.class));
            }
        });


        return view;
    }

}
