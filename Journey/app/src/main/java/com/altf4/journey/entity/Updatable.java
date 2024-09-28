package com.altf4.journey.entity;

import com.altf4.journey.network.ResponseContainer;

public interface Updatable {
    boolean updateField(ResponseContainer publisher, String fieldValue);
}
