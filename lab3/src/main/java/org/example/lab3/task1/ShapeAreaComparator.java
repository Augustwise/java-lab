package org.example.lab3.task1;

import java.util.Comparator;

public class ShapeAreaComparator implements Comparator<Shape> {
    @Override
    public int compare(Shape first, Shape second) {
        return Double.compare(first.calcArea(), second.calcArea());
    }
}
