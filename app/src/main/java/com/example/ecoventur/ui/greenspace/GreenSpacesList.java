package com.example.ecoventur.ui.greenspace;

import static com.google.maps.model.PlaceType.PARK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreenSpacesList {
    private Location currentLocation;
    private int placesCount = 5;
    private PlacesClient placesClient;
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
    public GreenSpacesList(Context context, int placesCount) {
        this.context = context;
        this.placesCount = placesCount;
        Places.initialize(context, "@string/API_key");
        placesClient = Places.createClient(context);
        fetchNearbyGreenSpaces();
    }
    private void fetchNearbyGreenSpaces() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                this.currentLocation = location;
                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                GeoApiContext geoApiContext = new GeoApiContext.Builder()
                        .apiKey("@string/API_key")
                        .build();

                NearbySearchRequest request = PlacesApi.nearbySearchQuery(geoApiContext, currentLatLng);
                request.radius(5000);
                request.type(PARK);

                request.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse result) {
                        for (PlacesSearchResult place: result.results) {
                            LatLng locationLatLng = place.geometry.location;
                            com.google.android.gms.maps.model.LatLng location = new com.google.android.gms.maps.model.LatLng(place.geometry.location.lat, place.geometry.location.lng);
                            GreenSpace space = new GreenSpace(place.placeId, place.name, getApproxDistance(currentLatLng,locationLatLng), place.rating, location, place.openingHours, place.formattedAddress);
                            greenSpaces.add(space);
                            if (greenSpaces.size() >= placesCount) {
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });

//                FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.RATING, Place.Field.PHOTO_METADATAS, Place.Field.TYPES));
//                placesClient.findCurrentPlace(request).addOnSuccessListener(response -> {
//                    List<PlaceLikelihood> placeLikelihoods = response.getPlaceLikelihoods();
//                    for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
//                        Place place = placeLikelihood.getPlace();
//                        List<Place.Type> placeTypes = place.getTypes();
//                        for (Place.Type type: placeTypes) {
//                            if (type == Place.Type.PARK || type == Place.Type.NATURAL_FEATURE) {
//                                String placeName = place.getName();
//                                LatLng placeLatLng = place.getLatLng();
//                                double placeRating = place.getRating();
//                                String placeImage = place.getPhotoMetadatas().get(0).getAttributions();
//                                double approxDistance = getApproxDistance(currentLatLng, placeLatLng);
//
//                                GreenSpace space = new GreenSpace(placeImage, placeName, approxDistance, placeRating);
//                                greenSpaces.add(space);
//                            }
//                        }
//                        if (greenSpaces.size() >= placesCount) {
//                            break;
//                        }
//                    }
//                });
            }
        });
    }
    private double getApproxDistance(LatLng currentLatLng, LatLng placeLatLng) {
        // https://www.movable-type.co.uk/scripts/latlong.html
        // Haversine formula
        double lat1 = currentLatLng.lat;
        double lon1 = currentLatLng.lng;
        double lat2 = placeLatLng.lat;
        double lon2 = placeLatLng.lng;
        double R = 6371e3; // metres
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2-lat1);
        double Δλ = Math.toRadians(lon2-lon1);
        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c / 1000;
    }
    public ArrayList<GreenSpace> getGreenSpaces() {
        return greenSpaces;
    }
}
