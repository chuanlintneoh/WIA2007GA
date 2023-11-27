package com.example.ecoventur.ui.greenspace;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.Geometry;
import com.google.maps.model.OpeningHours;

public class GreenSpace {
    private String placeId = null;
    private String image = null;
    private String name = "Unspecified Green Space Name";
    private double approxDistance = -1.0;
    private double rating = -1.0;
    private LatLng location = null;
    private String openingHours = "Unspecified Opening Hours";
    private String address = "Unspecified Address";
    private double admissionFee = -1.0;
    private String mapsURL = null;
    public GreenSpace (String imageUrl, String name, double approxDistance, double rating) {
        // for hard coded data
        this.image = imageUrl;
        this.name = name;
        this.approxDistance = approxDistance;
        this.rating = rating;
    }
    public GreenSpace (String placeId, String name, double approxDistance, float rating, LatLng location, OpeningHours openingHours, String formattedAddress) {
        // for Google Places API
        if (placeId != null) {
            this.placeId = placeId;
            this.mapsURL = "https://www.google.com/maps/place/?q=place_id:" + placeId;
        }
        if (name != null) {
            this.name = name;
        }
        if (rating >= 1.0 && rating <= 5.0){
            this.rating = rating;
        }
        if (location != null) {
            this.location = location;
            this.approxDistance = approxDistance;
        }
        if (openingHours != null) {
            this.openingHours = openingHours.toString();
        }
        if (formattedAddress != null){
            this.address = formattedAddress;
        }
//        this.admissionFee = admissionFee;
    }
    public String getPlaceId() {
        return placeId;
    }
    public String getImage() {
        return image;
    }
    public String getName() {
        return name;
    }
    public double getApproxDistance() {
        return approxDistance;
    }
    public double getRating() {
        return rating;
    }
    public LatLng getLocation() {
        return location;
    }
    public String getOpeningHours() {
        return openingHours;
    }
    public String getAddress() {
        return address;
    }
    public double getAdmissionFee() {
        return admissionFee;
    }
    public String getMapsURL() {
        return mapsURL;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setApproxDistance(double approxDistance) {
        this.approxDistance = approxDistance;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setLocation(LatLng location) {
        this.location = location;
    }
    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setAdmissionFee(double admissionFee) {
        this.admissionFee = admissionFee;
    }
    public void setMapsURL(String mapsURL) {
        this.mapsURL = mapsURL;
    }
}
