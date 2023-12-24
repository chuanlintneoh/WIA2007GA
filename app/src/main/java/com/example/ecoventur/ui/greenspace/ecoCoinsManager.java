package com.example.ecoventur.ui.greenspace;

import com.google.firebase.firestore.FirebaseFirestore;

public class ecoCoinsManager {
    public static void addEcoCoins(String UID, int ecoCoins, Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(UID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("ecocoin")) {
                            int currentEcoCoins = documentSnapshot.getLong("ecocoin").intValue();
                            int updatedEcoCoins = currentEcoCoins + ecoCoins;
                            db.collection("users").document(UID)
                                    .update("ecocoin", updatedEcoCoins)
                                    .addOnSuccessListener(aVoid -> callback.onDataLoaded(updatedEcoCoins))
                                    .addOnFailureListener(callback::onFailure);
                        }
                        else {
                            db.collection("users").document(UID).update("ecocoin", ecoCoins);
                        }
                    }
                    else {
                        callback.onFailure(new Exception("User does not exist."));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    public static void deductEcoCoins(String UID, int ecoCoins, Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(UID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("ecocoin")) {
                            int currentEcoCoins = documentSnapshot.getLong("ecocoin").intValue();
                            int updatedEcoCoins = currentEcoCoins - ecoCoins;
                            db.collection("users").document(UID)
                                    .update("ecocoin", updatedEcoCoins)
                                    .addOnSuccessListener(aVoid -> callback.onDataLoaded(updatedEcoCoins))
                                    .addOnFailureListener(callback::onFailure);;
                        }
                        else {
                            db.collection("users").document(UID).update("ecocoin", -ecoCoins);
                        }
                    }
                    else {
                        callback.onFailure(new Exception("User does not exist."));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    public static void updateEcoCoins(String UID, int ecoCoins, Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(UID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection("users").document(UID)
                                .update("ecocoin", ecoCoins)
                                .addOnSuccessListener(aVoid -> callback.onDataLoaded(ecoCoins))
                                .addOnFailureListener(callback::onFailure);;
                    }
                    else {
                        callback.onFailure(new Exception("User does not exist."));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
}
