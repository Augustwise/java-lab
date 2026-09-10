package org.example.lab3.task1;

import java.util.Comparator;

public class ShapeColorComparator implements Comparator<Shape> {
    @Override
    public int compare(Shape first, Shape second) {
        return first.getShapeColor().compareToIgnoreCase(second.getShapeColor());
    }
}
