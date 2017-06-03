package com.mnunez.pagertest.models;

import java.io.Serializable;

/**
 * Created by mnunez on 6/2/17.
 */

public class Role implements Serializable {

    private static final long serialVersionUID = 8027980238937372605L;

    private String id;

    private String description;

    public Role(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}
