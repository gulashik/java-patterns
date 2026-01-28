package org.gulash.visitor.elements.impl;

import org.gulash.visitor.elements.Shape;
import org.gulash.visitor.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public class CompoundShape implements Shape {
    private int id;
    private List<Shape> children = new ArrayList<>();

    public CompoundShape(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void add(Shape shape) {
        children.add(shape);
    }

    public List<Shape> getChildren() {
        return children;
    }
}
