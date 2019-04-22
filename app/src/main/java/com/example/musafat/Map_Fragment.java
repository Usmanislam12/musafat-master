    package com.example.musafat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map_Fragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    FragmentManager manager;
    private int permission_code = 1;
    private FusedLocationProviderClient client;
    AutoCompleteTextView pick_up_et;
    AutoCompleteTextView drop_off_et;
    Button confirm;
    View view;
    AutoCompleteAdapter adapter;
    GeoDataClient geoDataClient;

    public Map_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);



        init();
        mapFragment.getMapAsync(this);
        return view;
    }

    private void init() {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);
        pick_up_et = view.findViewById(R.id.pick_up_et);
        drop_off_et = view.findViewById(R.id.dropOff_et);
        confirm = view.findViewById(R.id.confirm_btn);

        geoDataClient= Places.getGeoDataClient(getContext());
        LatLngBounds latLngBounds=new LatLngBounds(
                new LatLng(24.3539 ,61.74681 ),new LatLng(35.91869 ,75.16683)
        );
        adapter=new AutoCompleteAdapter(getContext(),geoDataClient,latLngBounds,null);
        pick_up_et.setAdapter(adapter);

        pick_up_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i== EditorInfo.IME_ACTION_DONE){
                    Toast.makeText(getContext(), "abc", Toast.LENGTH_SHORT).show();
                    getLocation();
                }

                return false;
            }
        });
    }

    private void getLocation() {

        String pick_up=pick_up_et.getText().toString();
        Geocoder geocoder=new Geocoder(getContext());
        List<Address> addresses;

        try {
            addresses=geocoder.getFromLocationName(pick_up,1);
            if (addresses.size()>0){
                Address address=addresses.get(0);
                Log.e("Map ","address "+address.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, permission_code);

        }

        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    Log.e("Map ", "lat " + location.getLatitude());
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19.0f));
                    googleMap.setBuildingsEnabled(true);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.getUiSettings().setMapToolbarEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, permission_code);
                    } else
                        googleMap.setMyLocationEnabled(true);
                }

            }
        });

    }
}
