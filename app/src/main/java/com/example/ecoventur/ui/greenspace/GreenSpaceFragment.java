package com.example.ecoventur.ui.greenspace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;
import com.example.ecoventur.databinding.FragmentGreenspaceBinding;
import com.example.ecoventur.ui.greenspace.adapter.GreenEventsAdapter;
import com.example.ecoventur.ui.greenspace.adapter.GreenSpacesAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GreenSpaceFragment extends Fragment implements OnMapReadyCallback {

    private String UID = "uaPJZguefgcNGyl0Ig2sy1Yq6tu1";// to be passed from MainActivity during login
    private FragmentGreenspaceBinding binding;
    private SearchView searchView;
    private MapView mapView;
    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    private static final int PARKS_RADIUS_METERS = 5000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGreenspaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = root.findViewById(R.id.SVGreenSpace);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String search = searchView.getQuery().toString();
                List<Address> searchResults = null;
                Geocoder geocoder = new Geocoder(requireContext());
                try {
                    searchResults = geocoder.getFromLocationName(search, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (searchResults == null || searchResults.size() == 0) {
                    Toast.makeText(getContext(), "No results found for \"" + search + "\"", Toast.LENGTH_SHORT).show();
                }
                else {
                    Address address = searchResults.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(search));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    StringBuilder addressText = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressText.append(address.getAddressLine(i)).append(", ");
                    }
                    String formattedAddress = addressText.toString().replaceAll(", $", "");
                    Toast.makeText(getContext(), formattedAddress, Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (googleMap != null){
                    googleMap.clear();
                }
                return false;
            }
        });

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Places.initialize(getActivity().getApplicationContext(), "@string/API_key");

        RecyclerView recyclerViewNearbyGreenSpaces = binding.recyclerViewNearbyGreenSpaces;
        GreenSpacesAdapter greenSpacesAdapter = new GreenSpacesAdapter(new GreenSpacesList().getGreenSpaces());//hardcoded
        recyclerViewNearbyGreenSpaces.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNearbyGreenSpaces.setAdapter(greenSpacesAdapter);

        CardView cardViewGreenEventsHeader = root.findViewById(R.id.CVGreenEventHeader);
        RecyclerView recyclerViewDiscoverGreenEvents = binding.recyclerViewDiscoverGreenEvents;
//        GreenEventsAdapter adapter = new GreenEventsAdapter(new GreenEventsList().getGreenEvents());//hardcoded
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        GreenEventsAdapter eventsAdapter = new GreenEventsAdapter(new GreenEventsList(firestore).getGreenEvents());//firestore
        recyclerViewDiscoverGreenEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDiscoverGreenEvents.setAdapter(eventsAdapter);

        CardView cardViewEventsWishlistHeader = root.findViewById(R.id.CVEventsWishlistHeader);
        RecyclerView recyclerViewMyEventsWishlist = binding.recyclerViewMyEventsWishlist;
        firestore = FirebaseFirestore.getInstance();
        GreenEventsAdapter wishlistAdapter = new GreenEventsAdapter(new GreenEventsList(firestore, UID).getGreenEvents());//user's saved green events wishlist
        recyclerViewMyEventsWishlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMyEventsWishlist.setAdapter(wishlistAdapter);

        Button buttonNearbyGreenSpaces = root.findViewById(R.id.ToggleNearbyGreenSpaces);
        buttonNearbyGreenSpaces.setOnClickListener(view -> toggleVisibility(recyclerViewNearbyGreenSpaces));
        Button buttonDiscoverGreenEvents = root.findViewById(R.id.ToggleDiscoverGreenEvents);
        buttonDiscoverGreenEvents.setOnClickListener(view -> {
            toggleVisibility(cardViewGreenEventsHeader);
            toggleVisibility(recyclerViewDiscoverGreenEvents);
        });
        Button buttonMyEventsWishlist = root.findViewById(R.id.ToggleMyEventsWishlist);
        buttonMyEventsWishlist.setOnClickListener(view -> {
            toggleVisibility(cardViewEventsWishlistHeader);
            toggleVisibility(recyclerViewMyEventsWishlist);
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            googleMap.setMyLocationEnabled(true);
            // hardcoded default location: Malaysia
            LatLng malaysia = new LatLng(4.2105, 101.9758);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malaysia, 5));
//            fetchAndDisplayParks();
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

//    private void fetchAndDisplayParks() {
//        PlacesClient placesClient = Places.createClient(requireContext());
//
//        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(requireActivity(), location -> {
//                    if (location != null) {
//                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//
//                        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
//
//                        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
//
//                        placesClient.findCurrentPlace(request)
//                                .addOnSuccessListener((response) -> {
//                                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                                        Place place = placeLikelihood.getPlace();
//                                        LatLng parkLocation = place.getLatLng();
//
//                                        MarkerOptions markerOptions = new MarkerOptions()
//                                                .position(parkLocation)
//                                                .title(place.getName());
//
//                                        googleMap.addMarker(markerOptions);
//                                    }
//                                })
//                                .addOnFailureListener((exception) -> {
//                                    Log.e(TAG, "Place not found: " + exception.getMessage());
//                                });
//                    }
//                });
//    }
    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
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