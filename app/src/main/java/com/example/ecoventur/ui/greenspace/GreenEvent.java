package com.example.ecoventur.ui.greenspace;

import android.media.Image;

public class GreenEvent {
    public String name = "Unspecified Event Name";
    public String date = "Unspecified Date";
    public String venue = "Unspecified Event Venue";
    public int ecoCoins = 0;
    Image image = null;
    String duration = "Unspecified Duration";
    double registrationFee = 0.0;
    String venueAddress = "Unspecified Venue Address";
    String venueLink = "Unspecified Venue Link";
    double approxDistance = 0.0;
    int going = 0;
    int interested = 0;
    String tncLink = "Unspecified Terms and Conditions Link";
    String detailsLink = "Unspecified Details Link";
    boolean savedToWishlist = false;

    public GreenEvent (String eventName, String date, String venue, int ecoCoins) {
        this.name = eventName;
        this.date = date;
        this.venue = venue;
        this.ecoCoins = ecoCoins;
    }
}
