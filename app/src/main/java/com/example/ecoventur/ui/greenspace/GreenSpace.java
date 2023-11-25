package com.example.ecoventur.ui.greenspace;

import android.media.Image;

public class GreenSpace {
    Image image = null;
    String name = "Unspecified Green Space Name";
    double approxDistance = 0.0;
    double rating = 0.0;
    String openingHours = "Unspecified Opening Hours";
    String address = "Unspecified Address";
    double admissionFee = 0.0;
    String mapsURL = "Unspecified Maps URL";

    public GreenSpace (Image image, String name, double approxDistance, double rating) {
        this.image = image;
        this.name = name;
        this.approxDistance = approxDistance;
        this.rating = rating;
    }
}
