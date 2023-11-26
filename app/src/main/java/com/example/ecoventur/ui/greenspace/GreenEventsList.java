package com.example.ecoventur.ui.greenspace;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class GreenEventsList {
    private ArrayList<GreenEvent> greenEvents = new ArrayList<GreenEvent>();
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
                        for (GreenEvent event : task.getResult().toObjects(GreenEvent.class)) {
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
                                                    GreenEvent event = eventSnapshot.toObject(GreenEvent.class);
                                                    if (event != null){
                                                        greenEvents.add(event);
                                                    }
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
