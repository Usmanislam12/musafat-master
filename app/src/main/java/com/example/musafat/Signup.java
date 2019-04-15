package com.example.musafat;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Signup extends AppCompatActivity {

    private int capture_img = 123;
    private int pick = 456;
    Bitmap bitmap;
    Uri image_uri;
    ImageView username;
    FirebaseAuth auth;
    FirebaseUser User;
    FirebaseDatabase database;
    DatabaseReference datbasereference;
    FirebaseStorage storage;
    StorageReference refrence;
    ProgressDialog dialog;
    EditText usernamee, pass, conpass, email, contact;
    Button signup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean check = false;
    ScrollView container;
    Snackbar snackbar;


    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions signInOptions;
    private int LOGIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        container = findViewById(R.id.container);
        username = findViewById(R.id.signup_img);
        usernamee = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        conpass = findViewById(R.id.confirmpass);
        email = findViewById(R.id.emaill);
        contact = findViewById(R.id.contactno);
        signup = findViewById(R.id.signupbtn);
        database = FirebaseDatabase.getInstance();
        datbasereference = database.getReference("user");
        storage = FirebaseStorage.getInstance();

        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please Wait...");

        testconnectio();
        View view = snackbar.getView();
        view.setBackgroundColor(Color.BLACK);
        view.setMinimumHeight(20);
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check = false;
                String emailText = email.getText().toString();
                String nameText = usernamee.getText().toString();
                String passText = pass.getText().toString();
                String contactText = contact.getText().toString();
                String conpassword = conpass.getText().toString();

                if (nameText == "") {
                    usernamee.setError("This field is mandatory");
                    check = true;
                }
                if (passText == "") {
                    pass.setError("This field is mandatory");
                    check = true;
                }
                if (passText.length() <= 10) {
                    pass.setError("password is short....");
                    check = true;

                }
                if (conpassword == "") {
                    conpass.setError("This field is mandatory");
                    check = true;
                }
                if (!passText.equals(conpassword)) {
                    conpass.setError("password do not match");
                    check = true;
                }

                if (!emailText.matches(emailPattern)) {
                    Toast.makeText(Signup.this, "Email is InValid", Toast.LENGTH_SHORT).show();
                    check = true;
                }
                if (contactText == "") {
                    contact.setError("This field is mandatory");
                    check = true;
                }

                if (!check) {
                    dialog.show();
                    authUser(nameText, emailText, passText, contactText);
                }
            }
        });
    }

    private void testconnectio() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());

        if (info != null && info.isConnected()) {

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                showSnackBar(1);
            } else {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                showSnackBar(2);
            }

        } else {
            showSnackBar(0);
        }

    }


    private void showSnackBar(int check) {

        if (check == 1) {
            snackbar = Snackbar.make(container, "Connected To Wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (check == 2) {
            snackbar = Snackbar.make(container, "Connected To Mobile Data", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            snackbar = Snackbar.make(container, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            snackbar.setAction("RE-TRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testconnectio();
                }
            });
            snackbar.setActionTextColor(Color.BLUE);
        }

    }


    private void authUser(final String nameText, final String emailText, final String passText, final String contactText) {
        auth.createUserWithEmailAndPassword(emailText, passText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    refrence = storage.getReference("images/" + auth.getCurrentUser().getUid());
                    Toast.makeText(Signup.this, "user added", Toast.LENGTH_SHORT).show();
                    uploaddata(nameText, emailText, passText, contactText);

                } else {
                    Toast.makeText(Signup.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploaddata(final String nameText, final String emailText, final String passText, final String contactText) {
        BitmapDrawable drawable = (BitmapDrawable) username.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        refrence.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                refrence.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String image = task.getResult().toString();
                            String uid = auth.getCurrentUser().getUid();

                            savdata(nameText, passText, emailText, contactText, uid, image);


                        }

                    }
                });
                dialog.dismiss();
                Toast.makeText(Signup.this, "upload", Toast.LENGTH_SHORT).show();


            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                dialog.setMessage((int) progress + "% Uploaded");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(Signup.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void savdata(String usernamee, String pass, String email, String contact, String uid, String image) {

        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        user User = new user(image, pass, usernamee, email, contact);
        datbasereference.child(uid).setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Toast.makeText(Signup.this, "added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Signup.this, BaseActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(Signup.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void selectimage(View view) {
        final String[] option = {"Camera", "gallery", "cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SelectOption");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (option[i].equals("Camera")) {
                    Intent imagine = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(imagine, capture_img);

                } else if (option[i].equals("gallery")) {
                    Intent openable = new Intent();
                    openable.setAction(Intent.ACTION_PICK);
                    openable.setType("image/*");
                    startActivityForResult(Intent.createChooser(openable, "selective"), pick);
                } else if (option[i].equals("cancel")) {
                    dialog.dismiss();
                }
            }
        }).show();

    }


  /*  public void signgoogle(View view) {

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        datbasereference = database.getReference("user");


        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("220847839897-amh4uc14a2anohqg4ia8q0443fm1g5i1.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);


        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, LOGIN);


    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            username.setImageBitmap(bitmap);
        } else if (requestCode == 456 && resultCode == RESULT_OK) {
            image_uri = data.getData();
            username.setImageURI(image_uri);
        }

    }
}



