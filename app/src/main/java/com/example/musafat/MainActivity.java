package com.example.musafat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText emaill;
    EditText passs;
    Button login;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions signInOptions;
    private int LOGIN = 1;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        if (user != null) {
            startActivity(new Intent(this, BaseActivity.class));

        }
        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please Wait...");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emaill.getText().toString();
                String pass = passs.getText().toString();
                authUser(email, pass);

            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void authUser(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    startActivity(new Intent(MainActivity.this, BaseActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        emaill = findViewById(R.id.main_email);
        passs = findViewById(R.id.main_pass);
        login = findViewById(R.id.main_logijn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();


    }



    public void signup(View view) {
        startActivity(new Intent(MainActivity.this, Signup.class));
        finish();
    }
    public void onBackPressed() {

        super.onBackPressed();
        System.exit(0);

    }

    public void signgoogle(View view) {

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("user");


        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("220847839897-amh4uc14a2anohqg4ia8q0443fm1g5i1.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);


        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LOGIN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {

                final GoogleSignInAccount acc = task.getResult();
                AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user=auth.getCurrentUser();

                            String name = user.getDisplayName();
                            String uid = user.getUid();
                            String email = user.getEmail();
                            String image = user.getPhotoUrl().toString();
                            savdata(name, "", email, "", uid, image);
                            Toast.makeText(MainActivity.this, "login "+uid , Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(Signup.this,BaseActivity.class));
                        } else
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }
    }

    private void savdata(String usernamee, String pass, String email, String contact, String uid, String image) {

        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        user User = new user(image, pass, usernamee, email, contact);
        databaseReference.child(uid).setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,BaseActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
