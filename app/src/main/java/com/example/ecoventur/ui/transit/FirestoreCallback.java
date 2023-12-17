package com.example.ecoventur.ui.transit;

public interface FirestoreCallback {
    void onDataLoaded (Object object);
    void onFailure(Exception e);
}
