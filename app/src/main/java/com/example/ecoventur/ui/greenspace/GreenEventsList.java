package com.example.ecoventur.ui.greenspace;

import static com.example.ecoventur.ui.greenspace.approxDistanceBetweenLocation.HaversineFormula;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.model.LatLng;

import java.util.ArrayList;

public class GreenEventsList {
    private final ArrayList<GreenEvent> greenEvents = new ArrayList<>();
    public GreenEventsList() {
        //hardcoded data
        greenEvents.add(new GreenEvent("Tree Planting Day", "10 Nov 2023", "Botanical Garden KL", 50));
        GreenEvent event = new GreenEvent("Nature Walks", "11 Nov 2023 - 13 Nov 2023", "Lake Garden KL", 10);
        event.setImage(null);
        event.setDuration("All day");
        event.setRegistrationFee(0.00);
        event.setVenueAddress("Perdana Botanical Gardens, 50480 Kuala Lumpur, Federal Territory of Kuala Lumpur");
        event.setVenueLink("https://maps.app.goo.gl/rfVADHhb32FYZBkWA");
        event.setGoing(23);
        event.setInterested(67);
        event.setTncLink("https://www.google.com");
        event.setDetailsLink("https://www.google.com");
        greenEvents.add(event);
        greenEvents.add(new GreenEvent("Beach Cleanup", "11 Nov 2023", "Pantai Tanjung Piai", 50));
        greenEvents.add(new GreenEvent("Eco-Workshops", "15 Nov 2023", "Online", 5));
    }
    public GreenEventsList(FirebaseFirestore db) {
        //firestore data
        retrieveFirestoreData(db);
    }
    public GreenEventsList(FirebaseFirestore db, String UID) {
        //user's saved green events wishlist
        retrieveUserWishlist(db, UID);
    }
    private void retrieveFirestoreData(FirebaseFirestore db) {
        //retrieve data from firestore
        db.collection("greenEvents")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            GreenEvent event = new GreenEvent();
                            event.setEventId(document.getId());
                            if (document.contains("name")) event.setName(document.getString("name"));
                            if (document.contains("date")) event.setDate(document.getString("date"));
                            if (document.contains("venue")) event.setVenue(document.getString("venue"));
                            if (document.contains("ecoCoins")) event.setEcoCoins(document.getLong("ecoCoins").intValue());
                            greenEvents.add(event);
                        }
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }
    private void retrieveUserWishlist(FirebaseFirestore db, String UID) {
        //retrieve user's wishlist from firestore
        db.collection("users").document(UID).collection("eventsWishlist")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference eventRef = document.getDocumentReference("eventId");
                            if (eventRef != null) {
                                eventRef.get()
                                        .addOnCompleteListener(eventTask -> {
                                            if (eventTask.isSuccessful()) {
                                                DocumentSnapshot eventSnapshot = eventTask.getResult();
                                                if (eventSnapshot.exists()) {
                                                    GreenEvent event = new GreenEvent();
                                                    event.setEventId(eventSnapshot.getId());
                                                    if (eventSnapshot.contains("name")) event.setName(eventSnapshot.getString("name"));
                                                    if (eventSnapshot.contains("date")) event.setDate(eventSnapshot.getString("date"));
                                                    if (eventSnapshot.contains("venue")) event.setVenue(eventSnapshot.getString("venue"));
                                                    if (eventSnapshot.contains("ecoCoins")) event.setEcoCoins(eventSnapshot.getLong("ecoCoins").intValue());
                                                    greenEvents.add(event);
                                                }
                                            }
                                        });
                            }
                        }
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }
    public ArrayList<GreenEvent> getGreenEvents() {
        return greenEvents;
    }
}
