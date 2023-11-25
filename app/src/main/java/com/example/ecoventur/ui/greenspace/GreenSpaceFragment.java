package com.example.ecoventur.ui.greenspace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;
import com.example.ecoventur.databinding.FragmentGreenspaceBinding;
import com.example.ecoventur.ui.greenspace.adapter.GreenEventsAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class GreenSpaceFragment extends Fragment implements OnMapReadyCallback {

    private FragmentGreenspaceBinding binding;
    private SearchView searchView;
    private MapView mapView;
    private GoogleMap googleMap;
    private Button buttonNearbyGreenSpaces;
    private Button buttonDiscoverGreenEvents;
    private Button buttonMyEventsWishlist;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PARKS_RADIUS_METERS = 5000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGreenspaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = root.findViewById(R.id.SVGreenSpace);

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapView.getMapAsync(googleMap -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            googleMap.setMyLocationEnabled(true);
            // Add a marker in Sydney and move the camera
            //LatLng sydney = new LatLng(-34, 151);
            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        });

        Places.initialize(getActivity().getApplicationContext(), "@string/API_key");

        RecyclerView recyclerViewNearbyGreenSpaces = binding.recyclerViewNearbyGreenSpaces;

        RecyclerView recyclerViewDiscoverGreenEvents = binding.recyclerViewDiscoverGreenEvents;
        GreenEventsAdapter adapter = new GreenEventsAdapter(new GreenEventsList().greenEvents);
        recyclerViewDiscoverGreenEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDiscoverGreenEvents.setAdapter(adapter);

        RecyclerView recyclerViewMyEventsWishlist = binding.recyclerViewMyEventsWishlist;

        buttonNearbyGreenSpaces = root.findViewById(R.id.ToggleNearbyGreenSpaces);
        buttonNearbyGreenSpaces.setOnClickListener(view -> toggleVisibility(recyclerViewNearbyGreenSpaces));
        buttonDiscoverGreenEvents = root.findViewById(R.id.ToggleDiscoverGreenEvents);
        buttonDiscoverGreenEvents.setOnClickListener(view -> toggleVisibility(recyclerViewDiscoverGreenEvents));
        buttonMyEventsWishlist = root.findViewById(R.id.ToggleMyEventsWishlist);
        buttonMyEventsWishlist.setOnClickListener(view -> toggleVisibility(recyclerViewMyEventsWishlist));

        return root;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            enableMyLocation();
            moveToCurrentLocation();
            fetchAndDisplayParks();
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void moveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void fetchAndDisplayParks() {
        PlacesClient placesClient = Places.createClient(requireContext());

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

                        placesClient.findCurrentPlace(request)
                                .addOnSuccessListener((response) -> {
                                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                        Place place = placeLikelihood.getPlace();
                                        LatLng parkLocation = place.getLatLng();

                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(parkLocation)
                                                .title(place.getName());

                                        googleMap.addMarker(markerOptions);
                                    }
                                })
                                .addOnFailureListener((exception) -> {
                                    Log.e(TAG, "Place not found: " + exception.getMessage());
                                });
                    }
                });
    }
    private void toggleVisibility(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}