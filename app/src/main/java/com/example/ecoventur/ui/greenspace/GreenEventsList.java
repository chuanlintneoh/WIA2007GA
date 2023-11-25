package com.example.ecoventur.ui.greenspace;

import java.util.ArrayList;

public class GreenEventsList {
    ArrayList<GreenEvent> greenEvents = new ArrayList<GreenEvent>();
    public GreenEventsList() {
        greenEvents.add(new GreenEvent("Tree Planting Day", "10 Nov 2023", "Botanical Garden KL", 50));
        GreenEvent event = new GreenEvent("Nature Walks", "11 Nov 2023 - 13 Nov 2023", "Lake Garden KL", 10);
        event.image = null;
        event.duration = "All day";
        event.registrationFee = 0.00;
        event.venueAddress = "Perdana Botanical Gardens, 50480 Kuala Lumpur, Federal Territory of Kuala Lumpur";
        event.venueLink = "https://maps.app.goo.gl/rfVADHhb32FYZBkWA";
        event.going = 23;
        event.interested = 67;
        event.tncLink = "https://www.google.com";
        event.detailsLink = "https://www.google.com";
        greenEvents.add(event);
        greenEvents.add(new GreenEvent("Beach Cleanup", "11 Nov 2023", "Pantai Tanjung Piai", 50));
        greenEvents.add(new GreenEvent("Eco-Workshops", "15 Nov 2023", "Online", 5));
    }
}
