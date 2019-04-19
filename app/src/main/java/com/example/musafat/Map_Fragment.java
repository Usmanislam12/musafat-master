package com.example.musafat;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */

public class Map_Fragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    FragmentManager manager;
    private int permission_code = 1;
    double lat;
    double lang;
    Snackbar snackbar;
    RelativeLayout snackContainer;

    boolean wifi;
    boolean data;

    LocationManager locationManager;

    public Map_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ;

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        testconnectio();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);

        snackContainer = view.findViewById(R.id.container);

        Log.e("Fragment", "manager " + manager);
        Log.e("Fragment", "mapFragment " + mapFragment);
        mapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

    /*    Bundle bundle = getArguments();
        double lat = bundle.getDouble("lat");
        double lang = bundle.getDouble("lang");*/
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            LatLng latLng = new LatLng(lat, lang);
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Log.e("Map ", "lat " + location.getLatitude());
                    Log.e("Map ", "lang " + location.getLongitude());

                    lat = location.getLatitude();
                    lang = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }

    private void testconnectio() {

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void getLocation() {




    }


    private void showSnackBar(int check) {

        if (check == 1) {

            wifi = true;
            getLocation();
          /*  snackbar = Snackbar.make(container, "Connected To Wifi", Snackbar.LENGTH_LONG);
            snackbar.show();*/
        } else if (check == 2) {

            data = true;
            getLocation();
            /*snackbar = Snackbar.make(container, "Connected To Mobile Data", Snackbar.LENGTH_LONG);
            snackbar.show();*/
        } else {
            snackbar = Snackbar.make(snackContainer, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            snackbar.setAction("RE-TRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testconnectio();
                }
            });
            snackbar.setActionTextColor(Color.BLUE);
            View viewSnack = snackbar.getView();
            snackbar.getView();

            viewSnack.setBackgroundColor(Color.BLACK);
            viewSnack.setMinimumHeight(20);
            TextView textView = (TextView) viewSnack.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

}

