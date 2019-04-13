package com.example.musafat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class driverdetail extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference driverref;
    DatabaseReference userref;
    DatabaseReference viewref;
    FirebaseAuth auth;
    ImageView image;
    TextView dname;
    TextView dcarname;
    RatingBar ratingBar;
    float rating;
    Button rate_btn;
    float average;
    int count = 0;
    float totalRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverdetail);


        image = findViewById(R.id.detail_image);
        dname = findViewById(R.id.detail_name);
        dcarname = findViewById(R.id.detail_carname);
        rate_btn = findViewById(R.id.rate_btn);
        ratingBar = findViewById(R.id.rating);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        String driverId = bundle.getString("driverID");
        database = FirebaseDatabase.getInstance();
        driverref = database.getReference("drivers").child(driverId);
        viewref = database.getReference("drivers").child(driverId).child("views");
        userref = database.getReference("user").child(auth.getCurrentUser().getUid());


        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = ratingBar.getRating();
                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user User = dataSnapshot.getValue(user.class);
                        String uid = User.getUid();
                        String uname = User.getUname();
                        driverrating Driverrating = new driverrating(uid, uname, rating);
                        driverref.child("driverrating").child(uid).setValue(Driverrating);

                        driverref.child("driverrating").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                driverrating Driverrating1 = dataSnapshot.getValue(driverrating.class);
                                totalRating += Driverrating1.getRating();
                                count++;
                                average = totalRating / count;
                                driverref.child("averagerating").setValue(average);

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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        driverref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driver Driver = dataSnapshot.getValue(driver.class);
                dname.setText(Driver.getDrivername());
                dcarname.setText(Driver.getCarname());
                Glide.with(driverdetail.this).load(Driver.getImage()).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long viewcount = dataSnapshot.getChildrenCount();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue().equals(auth.getCurrentUser().getUid())) {
                        driverref.child("viewscount").setValue(viewcount);
                        return;

                    }
                }
                viewref.push().setValue(auth.getCurrentUser().getUid());
                driverref.child("viewscount").setValue(viewcount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
