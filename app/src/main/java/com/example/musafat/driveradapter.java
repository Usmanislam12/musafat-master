package com.example.musafat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class driveradapter extends RecyclerView.Adapter<driverviewholder> {
    ArrayList<driver> drivers;
    Context context;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference driverrefrence;
    ImageView image;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference adminref;
    String adminId;
    String uid;
    private int permission_code = 1;


    public driveradapter(ArrayList<driver> drivers, Context context) {

        this.drivers = drivers;
        this.context = context;

    }


    @NonNull
    @Override
    public driverviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.driveritem, viewGroup, false);
        driverviewholder driverViewholder = new driverviewholder(view);


        return driverViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final driverviewholder driverviewholder, final int i) {

        final driver Driver = drivers.get(i);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("drivers");
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        adminref = database.getReference().child("admin");
        adminref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminId = dataSnapshot.child("uid").getValue(String.class);
                if (uid.equals(adminId)) {
                    driverviewholder.imgmenu.setVisibility(View.VISIBLE);
                    driverviewholder.imgmenu.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        driverrefrence = reference.child(Driver.getDriverid());
        if (driverrefrence != null) {
            driverrefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    driverviewholder.drivername.setText(dataSnapshot.child("drivername").getValue(String.class));
                    driverviewholder.driver_carName.setText(dataSnapshot.child("carname").getValue(String.class));
                   /* DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    if (dataSnapshot.hasChild("avgrating")) {
                        float avg = dataSnapshot.child("avgrating").getValue(Float.class);
                        // driverviewholder.avgrating.setText(decimalFormat.format(avg));
                        driverviewholder.avgrating.setText(decimalFormat.format(avg));
                    }*/
                    driverviewholder.views.setText(String.valueOf(dataSnapshot.child("viewscount").getValue(Long.class)));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        Glide.with(context).load(Driver.getImage()).into(driverviewholder.driverimage);
        driverviewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("driverID", Driver.getDriverid());
                Intent intent = new Intent(context, driverdetail.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        driverviewholder.imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupmenu(driverviewholder.imgmenu, Driver.getDriverid(), i);
            }
        });
      /*  driverviewholder.callimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)


                    ActivityCompat.requestPermissions(context.get, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, permission_code);

            }

            Intent intent = new Intent(Intent.ACTION_CALL);

            Intent intent1 = new Intent(Intent.ACTION_CALL);
                 intent1.setData(Uri.parse("tel:"+Driver.getDrivercontact());
                context.startActivity(intent1);


        });*/

      driverviewholder.callimage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent =new Intent(Intent.ACTION_CALL);
              intent.setData(Uri.parse("tel:"+Driver.getDrivercontact()));
              context.startActivity(intent);
          }
      });

    }

    private void popupmenu(ImageView imgmenu, final String Driverid, final int i) {
        PopupMenu popupMenu = new PopupMenu(context, imgmenu);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.edit, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_menu:
                        update(Driverid);
                        break;
                    case R.id.delete_menun:
                        delete(i);
                }

                return false;
            }
        });
    }

    private void delete(int i) {
        driverrefrence.setValue(null);
        drivers.remove(i);
        notifyDataSetChanged();

    }

    private void update(String driverid) {
        Intent intent = new Intent(context, adddriver.class);
        Bundle bundle = new Bundle();
        bundle.putString("driverID", driverid);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    @Override
    public int getItemCount() {
        return drivers.size();
    }
}

