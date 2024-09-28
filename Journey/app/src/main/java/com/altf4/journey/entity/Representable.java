package com.altf4.journey.entity;

import java.util.Map;

public interface Representable {
    Map<String, Object> getDatabaseRepresentation();
}