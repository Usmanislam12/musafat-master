package com.example.musafat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class adddriver extends BaseActivity {
    private static int pick_image = 1;
    EditText dname, dcontact, dcarnum, dcar, dcolour,dpass,demail;
    Button add_driver;
    FirebaseAuth auth;
    FirebaseDatabase firebasedatabase;
    FirebaseUser user;
    float average_driver;
    DatabaseReference driverref;
    DatabaseReference driverratingref;
    private Uri imageuri;
    ImageView image;
    FirebaseStorage firebaseStorage;
    StorageReference imageref;
    String driverId;
    String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_adddriver);

        getLayoutInflater().inflate(R.layout.activity_adddriver,frameLayout);

        init();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            driverId = bundle.getString("driverId");

            driverref.child(driverId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    driver Driver = dataSnapshot.getValue(driver.class);
                    average_driver = Driver.getAvgrating();
                    dname.setText(Driver.getDrivername());
                    dcontact.setText(Driver.getDrivercontact());
                    dcarnum.setText(Driver.getCarnumber());
                    dcar.setText(Driver.getCarname());
                    dcolour.setText(Driver.getCarcolour());
                    demail.setText(Driver.getDriveremail());
                    dpass.setText(Driver.getDriverpass());
                    Glide.with(adddriver.this).load(Driver.getImage()).into(image);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        add_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drivername = dname.getText().toString();
                String drivercontact = dcontact.getText().toString();
                String drivercarnum = dcarnum.getText().toString();
                String drivercar = dcar.getText().toString();
                String drivercarcolour = dcolour.getText().toString();
                String driveremail=demail.getText().toString();
                String driverpass=dpass.getText().toString();
                key = driverId;
                if (driverId == null) {
                    key = driverref.push().getKey();

                }
                imageref = firebaseStorage.getReference("driver images/" + key);
                uploadimage(drivername, drivercontact, drivercarnum, drivercar, drivercarcolour,driveremail,driverpass);

            }
        });

    }

    private void init() {
        dname = findViewById(R.id.driver_name);
        dcontact = findViewById(R.id.driver_contact);
        dcolour = findViewById(R.id.driver_color);
        dcarnum = findViewById(R.id.driver_car);
        dcar = findViewById(R.id.driver_Carname);
        demail=findViewById(R.id.driver_email);
        dpass=findViewById(R.id.driver_pass);
        add_driver = findViewById(R.id.add_book_btn);
        image = findViewById(R.id.add_driver_image);
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebasedatabase = FirebaseDatabase.getInstance();
        driverref = firebasedatabase.getReference("drivers");
    }


    private void uploadimage(final String drivername, final String drivercontact, final String drivercarnum, final String drivercar, final String drivercarcolour, final String driveremail, final String driverpass) {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        imageref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {


                        if ((task.isSuccessful())) {
                            String imageUrl = task.getResult().toString();
                            /*driverratingref = driverref.child(key).child("driverrating");
                            if (driverratingref != null) {
                                driverratingref.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        driverrating Driverrating = dataSnapshot.getValue(driverrating.class);
                                        String ratingKey = dataSnapshot.getKey();
                                        driverratingref.child(ratingKey).setValue(Driverrating);
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
                                });*/

                            driver Driver = new driver(key,drivername,drivercar,drivercarcolour,drivercarnum,drivercontact,driveremail,driverpass,imageUrl);
                            driverref.child(key).setValue(Driver);
                           // driverref.child(driverId).child("avgrating").setValue(average_driver);
                          //  startActivity(new Intent(adddriver.this, driverlist.class));


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
    }

    public void opengallery(View view) {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(gallery, "Select"), pick_image);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                imageuri = data.getData();
                image.setImageURI(imageuri);
            }
        }
    }

}
