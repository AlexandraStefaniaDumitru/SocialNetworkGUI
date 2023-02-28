package com.example.socialnetworkgui.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Message extends Entity<Long>{

    private final Long sender;
    private final Long receiver;
    private final String message;
    private final LocalDate date_of;

    private final LocalTime time_of;

    public Message(Long sender, Long receiver, String message, LocalDate date_of, LocalTime time_of) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date_of = date_of;
        this.time_of = time_of;
    }

    public Long getSender() {
        return sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDate_of() {
        return date_of;
    }

    public LocalTime getTime_of() {
        return time_of;
    }
}