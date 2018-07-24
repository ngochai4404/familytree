package com.hai.familytree.model;

import com.google.gson.Gson;

/**
 * Created by Hai on 11/07/2018.
 */

public class Member {
    private int id;
    private String name;
    private int icon;
    private int coupleId = -1;
    private int fatherId = -1;
    private int motherId = -1;
    private int gender;

    public Member(String name, int icon,int gender) {
        this.name = name;
        this.icon = icon;
        this.gender = gender;
    }

    public Member() {
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getCoupleId() {
        return coupleId;
    }

    public void setCoupleId(int coupleId) {
        this.coupleId = coupleId;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public int getMotherId() {
        return motherId;
    }

    public void setMotherId(int motherId) {
        this.motherId = motherId;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
