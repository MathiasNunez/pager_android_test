package com.mnunez.pagertest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mnunez on 5/30/17.
 */

public class User implements Serializable {

    private static final long serialVersionUID = -1174203650547054219L;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("github")
    @Expose
    private String github;

    @SerializedName("role")
    @Expose
    private String roleId;

    private transient Role role;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("languages")
    @Expose
    private String[] languages;

    @SerializedName("tags")
    @Expose
    private String[] tags;

    @SerializedName("location")
    @Expose
    private String location;

    private String status;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role roleDescription) {
        this.role = roleDescription;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
