package com.example.v.ullapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor violet = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);//marcador violeta

        mMap.addMarker(new MarkerOptions().position(new LatLng(28.481298, -16.317419)).title("ULL-Campus Central").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.481704, -16.319994)).title("Facultad de Física y Matemáticas").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.480515, -16.319501)).title("Facultad de Biología").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.481704, -16.319994)).title("Escuela Superior de Ingeniería y Tecnología").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.471235, -16.305918)).title("Facultad de Economia, Empresa y Turismo").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.470179, -16.305092)).title("Facultad de Derecho").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.483719, -16.315983)).title("Facultad de Educación").icon(violet));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.456397, -16.289547)).title("Facultad de Ciencias de la Salud").icon(violet));

        //mMap.addMarker(new MarkerOptions().position(new LatLng(28.471235, -16.305918)).title("Facultad de Humanidades").icon(violet));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(28.471235, -16.305918)).title("Facultad de Ciencias Políticas, Sociales y de la Comunicación").icon(violet));

    }
}
