package org.gulash.visitor.elements.impl;

import org.gulash.visitor.elements.Shape;
import org.gulash.visitor.visitors.Visitor;

public class Dot implements Shape {
    private int id;
    private int x;
    private int y;

    public Dot() {}

    public Dot(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public void move(int x, int y) {
        // move logic
    }

    @Override
    public void draw() {
        // draw logic
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}
