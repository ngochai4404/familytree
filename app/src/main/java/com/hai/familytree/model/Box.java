package com.hai.familytree.model;

/**
 * Created by Hai on 20/07/2018.
 */

public class Box {
    private boolean topLeft;
    private boolean topRight;
    private boolean bottomLeft;
    private boolean bottomRight;
    private int width, height;

    public Box() {
    }

    public Box(BoxBuilder builder) {
        this.topLeft = builder.topLeft;
        this.topRight = builder.topRight;
        this.bottomLeft = builder.bottomLeft;
        this.bottomRight = builder.bottomRight;
        this.width = builder.width;
        this.height = builder.height;
    }

    public void setDirection(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public void setPos(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean istopLeft() {
        return topLeft;
    }

    public boolean istopRight() {
        return topRight;
    }

    public boolean isbottomLeft() {
        return bottomLeft;
    }

    public boolean isbottomRight() {
        return bottomRight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Box{" +
                "topLeft=" + topLeft +
                ", topRight=" + topRight +
                ", bottomLeft=" + bottomLeft +
                ", bottomRight=" + bottomRight +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public static class BoxBuilder {
        private boolean topLeft;
        private boolean topRight;
        private boolean bottomLeft;
        private boolean bottomRight;
        private int width, height;

        public BoxBuilder setDirection(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
            return this;
        }

        public BoxBuilder setPostition(int x, int y) {
            this.width = x;
            this.height = y;
            return this;
        }

        public Box build() {
            return new Box(this);
        }
    }
}
