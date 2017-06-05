package com.mnunez.pagertest.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mnunez on 6/1/17.
 */

public enum SocketEventEnum {

    USER("user_new"), STATUS("state_change");

    private static final Map<String, SocketEventEnum> lookup = new HashMap<>();

    static {
        for (SocketEventEnum m : SocketEventEnum.values())
            lookup.put(m.getPropertyName(), m);
    }

    private String propertyName;

    SocketEventEnum(String propertyName) {
        this.propertyName = propertyName;
    }

    public static SocketEventEnum get(String propertyName) {
        return lookup.containsKey(propertyName) ? lookup.get(propertyName) : null;
    }

    public String getPropertyName() {
        return this.propertyName;
    }
}
