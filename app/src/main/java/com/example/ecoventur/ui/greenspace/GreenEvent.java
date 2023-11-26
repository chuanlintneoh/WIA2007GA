package com.example.ecoventur.ui.greenspace;

import android.media.Image;

public class GreenEvent {
    private String name;
    private String date;
    private String venue;
    private int ecoCoins;
    private Image image;
    private String duration;
    private double registrationFee;
    private String venueAddress;
    private String venueLink;
    private double approxDistance;
    private int going;
    private int interested;
    private String tncLink;
    private String detailsLink;
    private boolean savedToWishlist;

    public GreenEvent() {
        this.name = "Unspecified Event Name";
        this.date = "Unspecified Date";
        this.venue = "Unspecified Event Venue";
        this.ecoCoins = 0;
        this.image = null;
        this.duration = "Unspecified Duration";
        this.registrationFee = 0.0;
        this.venueAddress = "Unspecified Venue Address";
        this.venueLink = "Unspecified Venue Link";
        this.approxDistance = 0.0;
        this.going = 0;
        this.interested = 0;
        this.tncLink = "Unspecified Terms and Conditions Link";
        this.detailsLink = "Unspecified Details Link";
        this.savedToWishlist = false;
    }
    public GreenEvent (String eventName, String date, String venue, int ecoCoins) {
        this.name = eventName;
        this.date = date;
        this.venue = venue;
        this.ecoCoins = ecoCoins;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getVenue() {
        return venue;
    }
    public int getEcoCoins() {
        return ecoCoins;
    }
    public Image getImage() {
        return image;
    }
    public String getDuration() {
        return duration;
    }
    public double getRegistrationFee() {
        return registrationFee;
    }
    public String getVenueAddress() {
        return venueAddress;
    }
    public String getVenueLink() {
        return venueLink;
    }
    public double getApproxDistance() {
        return approxDistance;
    }
    public int getGoing() {
        return going;
    }
    public int getInterested() {
        return interested;
    }
    public String getTncLink() {
        return tncLink;
    }
    public String getDetailsLink() {
        return detailsLink;
    }
    public boolean isSavedToWishlist() {
        return savedToWishlist;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setVenue(String venue) {
        this.venue = venue;
    }
    public void setEcoCoins(int ecoCoins) {
        this.ecoCoins = ecoCoins;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }
    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }
    public void setVenueLink(String venueLink) {
        this.venueLink = venueLink;
    }
    public void setApproxDistance(double approxDistance) {
        this.approxDistance = approxDistance;
    }
    public void setGoing(int going) {
        this.going = going;
    }
    public void setInterested(int interested) {
        this.interested = interested;
    }
    public void setTncLink(String tncLink) {
        this.tncLink = tncLink;
    }
    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }
    public void setSavedToWishlist(boolean savedToWishlist) {
        this.savedToWishlist = savedToWishlist;
    }
}
