package com.example.ecoventur.ui.greenspace;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ecoventur.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.model.LatLng;

public class GreenEventDetailsActivity extends AppCompatActivity {
    private String eventId;
    private final String UID = "uaPJZguefgcNGyl0Ig2sy1Yq6tu1";// to be passed from MainActivity during login
    private GreenEvent event = new GreenEvent();
    private ImageView IVEventImage;
    private TextView TVEventName, TVEventEcoCoins, TVEventDate, TVEventDuration, TVEventFee, TVEventVenue, TVEventDistance, TVEventParticipants, TVEventTnC, TVEventDetails;
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
                initializeWidgets();
                this.event = new GreenEvent(eventId, UID, currentLatLng, new GreenEvent.FirestoreDataListener() {
                    @Override
                    public void onDataLoaded(GreenEvent event) {
                        assignUIWidgets();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            });
        }
    }
    private void initializeWidgets () {
        IVEventImage = findViewById(R.id.IVEventImage);
        TVEventName = findViewById(R.id.TVEventName);
        TVEventEcoCoins = findViewById(R.id.TVEventEcoCoins);
        TVEventDate = findViewById(R.id.TVEventDate);
        TVEventDuration = findViewById(R.id.TVEventDuration);
        TVEventFee = findViewById(R.id.TVEventFee);
        TVEventVenue = findViewById(R.id.TVEventVenue);
        TVEventDistance = findViewById(R.id.TVEventDistance);
        TVEventParticipants = findViewById(R.id.TVEventParticipants);
        TVEventTnC = findViewById(R.id.TVEventTnC);
        TVEventDetails = findViewById(R.id.TVEventDetails);
    }
    private void assignUIWidgets() {
        //        IVEventImage.setImageResource(event.getImage());
        TVEventName.setText(event.getName());
        TVEventEcoCoins.setText(String.valueOf(event.getEcoCoins()));
        TVEventDate.setText(event.getDate());
        TVEventDuration.setText(event.getDuration());
        if (event.getRegistrationFee() == 0.0) TVEventFee.setText("FREE");
        else TVEventFee.setText(String.valueOf(event.getRegistrationFee()));

        if (event.getVenueLink() != null){
            SpannableString spannableVenue = customTabLauncher.makeTextSpannable(event.getVenue() + "\n" + event.getVenueAddress(),event.getVenueLink());
            TVEventVenue.setText(spannableVenue);
            TVEventVenue.setMovementMethod(LinkMovementMethod.getInstance());
            TVEventVenue.setHighlightColor(Color.TRANSPARENT);
        }
        else {
            TVEventVenue.setText(event.getVenue() + "\n" + event.getVenueAddress());
        }

        TVEventDistance.setText(String.format("Approximately %.1fkm from current location", event.getApproxDistance()));
        TVEventParticipants.setText(String.format("%d going, %d interested", event.getGoing(), event.getInterested()));

        if (event.getTncLink() != null){
            SpannableString spannableTnC = customTabLauncher.makeTextSpannable("View terms & conditions", event.getTncLink());
            TVEventTnC.setText(spannableTnC);
            TVEventTnC.setMovementMethod(LinkMovementMethod.getInstance());
            TVEventTnC.setHighlightColor(Color.TRANSPARENT);
        }
        else {
            TVEventTnC.setText("Terms & Conditions\nnot available");
        }

        if (event.getDetailsLink() != null){
            SpannableString spannableDetails = customTabLauncher.makeTextSpannable("View more details", event.getDetailsLink());
            TVEventDetails.setText(spannableDetails);
            TVEventDetails.setMovementMethod(LinkMovementMethod.getInstance());
            TVEventDetails.setHighlightColor(Color.TRANSPARENT);
        }
        else {
            TVEventDetails.setText("Details not available");
        }
    }
}
