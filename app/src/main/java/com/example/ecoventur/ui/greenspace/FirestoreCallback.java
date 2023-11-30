package com.example.ecoventur.ui.greenspace;

public interface FirestoreCallback {
    void onDataLoaded (Object object);
    void onFailure(Exception e);
}
