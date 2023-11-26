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
    private ArrayList<GreenSpace> greenSpaces = new ArrayList<GreenSpace>();
    private Context context;
    public GreenSpacesList() {
        //hard coded data
        greenSpaces.add(new GreenSpace("https://www.google.com/maps/place/Taman+Desa+Playground%2FPark/@3.0823331,101.6580819,3a,75y,90t/data=!3m8!1e2!3m6!1sAF1QipMOGt0vcEoxoEmzUdOl-C8PtwRT3nF0GQdvwadm!2e10!3e12!6shttps:%2F%2Flh5.googleusercontent.com%2Fp%2FAF1QipMOGt0vcEoxoEmzUdOl-C8PtwRT3nF0GQdvwadm%3Dw114-h86-k-no!7i4096!8i3072!4m7!3m6!1s0x31cc4bad9b619d1d:0xbb96f055002c0220!8m2!3d3.1011069!4d101.6829714!10e5!16s%2Fg%2F11fcsrw6h6?entry=ttu#", "Taman Desa Playground", 0.5, 4.3));
        greenSpaces.add(new GreenSpace("https://www.google.com/maps/place/Lake+Garden+@+Bangsar+South/@3.1117648,101.6665649,3a,75y,90t/data=!3m8!1e2!3m6!1sAF1QipOyhlYncOBnfxSPgfjUYeHFReo5zOFTt-10Vjfq!2e10!3e12!6shttps:%2F%2Flh5.googleusercontent.com%2Fp%2FAF1QipOyhlYncOBnfxSPgfjUYeHFReo5zOFTt-10Vjfq%3Dw114-h86-k-no!7i4032!8i3024!4m7!3m6!1s0x31cc4a2a09bf3e0f:0x6c104c7703f1f088!8m2!3d3.1117648!4d101.6665649!10e5!16s%2Fg%2F11f124l8x0?entry=ttu#", "Lake Garden @ Bangsar South", 1.3, 4.4));
        GreenSpace space = new GreenSpace("https://www.google.com/maps/place/KLCC+Park/@3.1545313,101.7151839,3a,75y,90t/data=!3m8!1e2!3m6!1sAF1QipMz6XvewUo1gPa19Tk8FhG_YUvmqzhAb8Xf89QV!2e10!3e12!6shttps:%2F%2Flh5.googleusercontent.com%2Fp%2FAF1QipMz6XvewUo1gPa19Tk8FhG_YUvmqzhAb8Xf89QV%3Dw114-h86-k-no!7i4032!8i3024!4m7!3m6!1s0x31cc37d3dae66605:0xced2781fa7347a4e!8m2!3d3.1545313!4d101.7151839!10e5!16s%2Fm%2F05mr431?entry=ttu#", "KLCC Park", 2.7, 4.6);
        space.setOpeningHours("10 am - 10 pm (Wednesday)");
        space.setAddress("KLCC, Lot No. 241, Level 2, Suria, Kuala Lumpur City Centre, 50088 Kuala Lumpur");
        space.setAdmissionFee(0.00);
        space.setMapsURL("https://maps.app.goo.gl/e9KBcMLR2dGc3PJV7");
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
    public ArrayList<GreenSpace> getGreenSpaces() {
        return greenSpaces;
    }
}
