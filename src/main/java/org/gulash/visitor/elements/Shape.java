package org.gulash.visitor.elements;

import org.gulash.visitor.visitors.Visitor;

public interface Shape {
    void move(int x, int y);
    void draw();
    String accept(Visitor visitor);
}
