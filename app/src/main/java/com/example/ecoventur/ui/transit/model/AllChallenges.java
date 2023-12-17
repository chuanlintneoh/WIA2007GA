package com.example.ecoventur.ui.transit.model;

import java.util.List;
import java.util.Date;

public class AllChallenges {

    String imageUrl;
    List <String> tags;
    String title;
    Date startDate;
    Date endDate;
    String description;
    List <String> rules;

    //Constructor
    public AllChallenges() {
    }

    public AllChallenges(String imageUrl, List<String> tags, String title, Date startDate, Date endDate, String description, List<String> rules) {
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.rules = rules;
    }

    //Getter & Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}
