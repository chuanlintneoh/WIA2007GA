package com.example.ecoventur.ui.greenspace;

public class GreenSpace {
    private String image;
    private String name;
    private double approxDistance;
    private double rating;
    private String openingHours;
    private String address;
    private double admissionFee;
    private String mapsURL;

    public GreenSpace() {
        this.image = null;
        this.name = "Unspecified Green Space Name";
        this.approxDistance = 0.0;
        this.rating = 0.0;
        this.openingHours = "Unspecified Opening Hours";
        this.address = "Unspecified Address";
        this.admissionFee = 0.0;
        this.mapsURL = "Unspecified Maps URL";
    }
    public GreenSpace (String imageUrl, String name, double approxDistance, double rating) {
        this.image = imageUrl;
        this.name = name;
        this.approxDistance = approxDistance;
        this.rating = rating;
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
