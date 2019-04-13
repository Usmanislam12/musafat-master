package com.example.musafat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;


public class help extends Fragment {
    TextView username, pass, email, contact;
    FirebaseAuth auth;
    FirebaseUser User;
    FirebaseDatabase database;
    DatabaseReference datbasereference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        datbasereference = database.getReference("users").child(User.getUid());*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        username = view.findViewById(R.id.help_username);
        pass = view.findViewById(R.id.help_password);
        email = view.findViewById(R.id.help_email);
        contact = view.findViewById(R.id.help_phone);
        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        datbasereference = database.getReference("user").child(User.getUid());

        datbasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user USer = dataSnapshot.getValue(user.class);
                username.setText(USer.getUname());
                email.setText(USer.getEmail());
                pass.setText(USer.getPass());
                contact.setText(USer.getContact());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        username.setClickable(true);

        return view;

    }

}



