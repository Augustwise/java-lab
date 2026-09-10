package org.example.lab3.task1;

import java.util.Arrays;
import java.util.Comparator;

public class ShapeModel {
    private final Shape[] shapes;

    public ShapeModel(Shape[] shapes) {
        this.shapes = shapes;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public double calcTotalArea() {
        double totalArea = 0;

        for (Shape shape : shapes) {
            totalArea += shape.calcArea();
        }

        return totalArea;
    }

    public double calcTotalAreaByType(Class<? extends Shape> shapeType) {
        double totalArea = 0;

        for (Shape shape : shapes) {
            if (shapeType.isInstance(shape)) {
                totalArea += shape.calcArea();
            }
        }

        return totalArea;
    }

    public void sort(Comparator<Shape> comparator) {
        Arrays.sort(shapes, comparator);
    }
}
