package com.example.ecoventur.ui.greenspace;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreenSpacesList {
    ArrayList<GreenSpace> greenSpaces = new ArrayList<GreenSpace>();
    private Context context;
    public GreenSpacesList() {
        //hard coded data
        greenSpaces.add(new GreenSpace(null, "Taman Desa Playground", 0.5, 4.3));
        greenSpaces.add(new GreenSpace(null, "Lake Garden @ Bangsar South", 0.5, 4.4));
        GreenSpace space = new GreenSpace(null, "KLCC Park", 2.7, 4.6);
        space.openingHours = "10 am - 10 pm (Wednesday)";
        space.address = "KLCC, Lot No. 241, Level 2, Suria, Kuala Lumpur City Centre, 50088 Kuala Lumpur";
        space.admissionFee = 0.00;
        space.mapsURL = "https://maps.app.goo.gl/e9KBcMLR2dGc3PJV7";
        greenSpaces.add(space);
    }
    public GreenSpacesList(Context context) {
        this.context = context;
        Places.initialize(context, "@string/API_KEY");

        PlacesClient placesClient = Places.createClient(context);

        //Get user's current location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        //Use the location to get the current place
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        //Fields to be requested
                        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.RATING, Place.Field.OPENING_HOURS, Place.Field.ADDRESS, Place.Field.PRICE_LEVEL, Place.Field.PHOTO_METADATAS);

                        //Then fetch the nearby parks
                        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
                        placesClient.findCurrentPlace(request)
                                .addOnSuccessListener((response) -> {
                                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                        Place place = placeLikelihood.getPlace();
                                        //Then display the parks on the map
                                    }
                                });
                    }
                });
    }
}
