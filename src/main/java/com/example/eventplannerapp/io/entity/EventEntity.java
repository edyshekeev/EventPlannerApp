package com.example.eventplannerapp.io.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(length = 30, nullable = false)
    private String eventId;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private long duration;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity author;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
