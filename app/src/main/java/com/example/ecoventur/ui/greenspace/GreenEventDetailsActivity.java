package com.example.ecoventur.ui.greenspace;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ecoventur.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.model.LatLng;

public class GreenEventDetailsActivity extends AppCompatActivity {
    private String eventId;
    private final String UID = "uaPJZguefgcNGyl0Ig2sy1Yq6tu1";// to be passed from MainActivity during login
    private GreenEvent event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_event_details);

        Intent intent = getIntent();
        if (intent != null) {
            eventId = intent.getStringExtra("eventId");
        }
        if (eventId != null) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                LatLng currentLatLng = null;
                if (location != null) {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
                this.event = new GreenEvent(eventId, UID, currentLatLng);
            });
        }
    }
}
