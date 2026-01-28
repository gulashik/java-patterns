package org.gulash.visitor.visitors;

import org.gulash.visitor.elements.impl.Circle;
import org.gulash.visitor.elements.impl.Dot;
import org.gulash.visitor.elements.impl.Rectangle;
import org.gulash.visitor.elements.impl.CompoundShape;

public interface Visitor {
    String visit(Dot dot);
    String visit(Circle circle);
    String visit(Rectangle rectangle);
    String visit(CompoundShape cg);
}
