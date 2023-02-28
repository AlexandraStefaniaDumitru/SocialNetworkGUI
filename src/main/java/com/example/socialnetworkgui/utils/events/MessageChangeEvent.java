package com.example.socialnetworkgui.utils.events;

import com.example.socialnetworkgui.model.Message;

public class MessageChangeEvent implements Event{
    private final ChangeEventType type;
    private final Message message;
    private Message oldMessage;

    public MessageChangeEvent(ChangeEventType type, Message message) {
        this.type = type;
        this.message = message;
    }

    public MessageChangeEvent(ChangeEventType type, Message message, Message oldMessage) {
        this.type = type;
        this.message = message;
        this.oldMessage = oldMessage;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getMessage() {
        return message;
    }

    public Message getOldMessage() {
        return oldMessage;
    }
}