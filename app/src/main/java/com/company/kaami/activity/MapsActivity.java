package com.company.kaami.activity;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.company.kaami.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double countryLat,countryLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder=new Geocoder(MapsActivity.this);

        try {
            String country="India";
            List<Address> countryaddress=geocoder.getFromLocationName(country,10);
            if(countryaddress!=null){
                countryLat=countryaddress.get(0).getLatitude();
                countryLong=countryaddress.get(0).getLongitude();
            }
            else {
                countryLat=-34;
                countryLong=151;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        LatLng sydney = new LatLng(countryLat,countryLong );
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
