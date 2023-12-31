package com.example.eventplannerapp.ui.model.request;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;

public class EventDetailsRequestModel {
    private String title;
    private String description;
    private Date date;
    private long duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
