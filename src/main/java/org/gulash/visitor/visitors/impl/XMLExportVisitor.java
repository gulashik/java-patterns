package org.gulash.visitor.visitors.impl;

import org.gulash.visitor.elements.*;
import org.gulash.visitor.elements.impl.Circle;
import org.gulash.visitor.elements.impl.CompoundShape;
import org.gulash.visitor.elements.impl.Dot;
import org.gulash.visitor.elements.impl.Rectangle;
import org.gulash.visitor.visitors.Visitor;

public class XMLExportVisitor implements Visitor {

    public String export(Shape... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        for (Shape shape : args) {
            sb.append(shape.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visit(Dot d) {
        return "<dot>" + "\n" +
                "    <id>" + d.getId() + "</id>" + "\n" +
                "    <x>" + d.getX() + "</x>" + "\n" +
                "    <y>" + d.getY() + "</y>" + "\n" +
                "</dot>";
    }

    @Override
    public String visit(Circle c) {
        return "<circle>" + "\n" +
                "    <id>" + c.getId() + "</id>" + "\n" +
                "    <x>" + c.getX() + "</x>" + "\n" +
                "    <y>" + c.getY() + "</y>" + "\n" +
                "    <radius>" + c.getRadius() + "</radius>" + "\n" +
                "</circle>";
    }

    @Override
    public String visit(Rectangle r) {
        return "<rectangle>" + "\n" +
                "    <id>" + r.getId() + "</id>" + "\n" +
                "    <x>" + r.getX() + "</x>" + "\n" +
                "    <y>" + r.getY() + "</y>" + "\n" +
                "    <width>" + r.getWidth() + "</width>" + "\n" +
                "    <height>" + r.getHeight() + "</height>" + "\n" +
                "</rectangle>";
    }

    @Override
    public String visit(CompoundShape cg) {
        return "<compound_graph>" + "\n" +
                "   <id>" + cg.getId() + "</id>" + "\n" +
                _visitCompoundShape(cg) +
                "</compound_graph>";
    }

    private String _visitCompoundShape(CompoundShape cg) {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : cg.getChildren()) {
            String obj = shape.accept(this);
            // Proper indentation for nested elements
            obj = "    " + obj.replace("\n", "\n    ") + "\n";
            sb.append(obj);
        }
        return sb.toString();
    }
}
