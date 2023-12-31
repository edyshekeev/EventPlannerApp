package com.example.eventplannerapp.ui.model.response;

import com.example.eventplannerapp.shared.dto.UserDto;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class EventsRest extends RepresentationModel<EventsRest> {
    private String eventId;
    private String title;
    private String description;
    private Date date;
    private long duration;
    private UserDto author;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

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

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }
}
