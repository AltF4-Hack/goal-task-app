package com.altf4.journey.network;

import com.altf4.journey.entity.Updatable;

public class ResponseContainer {
    private String response;
    private final Updatable subscriber;

    public ResponseContainer(Updatable subscriber) {
        this.subscriber = subscriber;
    }

    public void setResponse(String response) {
        this.response = response;
        notifySubscribers();
    }

    public void notifySubscribers() {
        subscriber.updateField(this, response);
    }
}
