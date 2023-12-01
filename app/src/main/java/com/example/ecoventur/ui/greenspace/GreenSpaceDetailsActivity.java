package com.example.ecoventur.ui.greenspace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.ecoventur.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class GreenSpaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GreenSpace space;
    private MapView MVSpaceLocation;
    private GoogleMap googleMap;
    private TextView TVSpaceName, TVSpaceOpeningHours, TVSpaceAddress, TVSpaceDistance, TVSpaceFee, TVSpaceLink;
    private LinearLayout LLSpaceReviews;
    private CardView CVWriteReview, CVShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenspace_details);

        Intent intent = getIntent();
        if (intent != null) {
            String greenSpaceId = intent.getStringExtra("greenSpaceId");
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // move camera to location of green space
//        LatLng location = ;
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
    private void initializeWidgets() {
        MVSpaceLocation = findViewById(R.id.MVSpaceLocation);
        TVSpaceName = findViewById(R.id.TVSpaceName);
        TVSpaceOpeningHours = findViewById(R.id.TVSpaceOpeningHours);
        TVSpaceAddress = findViewById(R.id.TVSpaceAddress);
        TVSpaceDistance = findViewById(R.id.TVSpaceDistance);
        TVSpaceFee = findViewById(R.id.TVSpaceFee);
        TVSpaceLink = findViewById(R.id.TVSpaceLink);
        LLSpaceReviews = findViewById(R.id.LLSpaceReviews);
        CVWriteReview = findViewById(R.id.CVWriteReview);
        CVShare = findViewById(R.id.CVShare);
    }
    private void assignUIWidgets() {
//        MVSpaceLocation
        TVSpaceName.setText(space.getName());
        TVSpaceOpeningHours.setText(space.getOpeningHours());
        TVSpaceAddress.setText(space.getAddress());

        if (space.getApproxDistance() != -1.0){
            TVSpaceDistance.setText(String.format("Approximately %.1fkm from current location", space.getApproxDistance()));
        }
        else {
            TVSpaceDistance.setText("Distance from current location not available");
        }

        if (space.getAdmissionFee() == 0.0) TVSpaceFee.setText("FREE");
        else if (space.getAdmissionFee() == -1.0) TVSpaceFee.setText("Entry Fee Unspecified");
        else TVSpaceFee.setText(String.format("RM %.2f", space.getAdmissionFee()));

        if (space.getMapsURL() != null) {
            SpannableString spannableLink = customTabLauncher.makeTextSpannable("View in Google Maps", space.getMapsURL());
            TVSpaceLink.setText(spannableLink);
            TVSpaceLink.setMovementMethod(LinkMovementMethod.getInstance());
            TVSpaceLink.setHighlightColor(Color.TRANSPARENT);
        }

//        LLSpaceReviews
//        CVWriteReview
        CVShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String shareBody =
                        String.format("\uD83C\uDF3F Look! This is what I found from EcoVentur, a safe, inclusive and accessible, green and public place - '%s'.\n" +
                                        "Location: %s\n%s\n" +
                                        "Download EcoVentur now to find out more!\n%s",
                                space.getName(), space.getAddress(), (space.getMapsURL() == null? "" : space.getMapsURL()), "https://play.google.com/store/apps/details?id=com.example.ecoventur");
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "EcoVentur");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
    }
}
