package com.hai.familytree.model;

import com.google.gson.Gson;

/**
 * Created by Hai on 11/07/2018.
 */

public class Member {
    //data
    private int id;
    private String name;
    private int icon;
    private int coupleId = -1;
    private int fatherId = -1;
    private int motherId = -1;
    private int gender;
    //condition
    private int countTopLeft = 0;
    private int countTopRight = 0;
    private int countBottomLeft = 0;
    private int countBottomRight = 0;
    //draw
    private int width=0;
    private int x=0;
    private int y=0;
    //
    public Member(String name, int icon, int gender) {
        this.name = name;
        this.icon = icon;
        this.gender = gender;
    }

    public Member() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public int getCountTopLeft() {
        return countTopLeft;
    }

    public void setCountTopLeft(int countTopLeft) {
        this.countTopLeft = countTopLeft;
    }

    public int getCountTopRight() {
        return countTopRight;
    }

    public void setCountTopRight(int countTopRight) {
        this.countTopRight = countTopRight;
    }

    public int getCountBottomLeft() {
        return countBottomLeft;
    }

    public void setCountBottomLeft(int countBottomLeft) {
        this.countBottomLeft = countBottomLeft;
    }

    public int getCountBottomRight() {
        return countBottomRight;
    }

    public void setCountBottomRight(int countBottomRight) {
        this.countBottomRight = countBottomRight;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
