package com.example.musafat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class driverviewholder extends RecyclerView.ViewHolder {
    ImageView driverimage;
    TextView drivername;
    TextView avgrating;
    TextView views;
    ImageView imgmenu;
    TextView driver_carName;

    public driverviewholder(@NonNull View itemView) {
        super(itemView);
        driverimage = itemView.findViewById(R.id.driver_image);
        drivername = itemView.findViewById(R.id.driver_name);
        //avgrating = itemView.findViewById(R.id.driver_carname);
        views = itemView.findViewById(R.id.driver_views);
        imgmenu = itemView.findViewById(R.id.img_menu);
        driver_carName=itemView.findViewById(R.id.driver_carname);

    }
}
