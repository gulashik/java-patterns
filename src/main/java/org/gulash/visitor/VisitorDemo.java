package org.gulash.visitor;

import org.gulash.visitor.elements.*;
import org.gulash.visitor.elements.impl.Circle;
import org.gulash.visitor.elements.impl.CompoundShape;
import org.gulash.visitor.elements.impl.Dot;
import org.gulash.visitor.elements.impl.Rectangle;
import org.gulash.visitor.visitors.impl.XMLExportVisitor;

public class VisitorDemo {
    public static void main(String[] args) {
        Dot dot = new Dot(1, 10, 55);
        Circle circle = new Circle(2, 23, 15, 10);
        Rectangle rect = new Rectangle(3, 10, 17, 20, 30);

        CompoundShape compoundShape = new CompoundShape(4);
        compoundShape.add(dot);
        compoundShape.add(circle);
        compoundShape.add(rect);

        CompoundShape root = new CompoundShape(5);
        root.add(compoundShape);
        root.add(new Dot(6, 100, 100));

        export(circle, root);
    }

    private static void export(Shape... shapes) {
        XMLExportVisitor exportVisitor = new XMLExportVisitor();
        System.out.println(exportVisitor.export(shapes));
    }
}
