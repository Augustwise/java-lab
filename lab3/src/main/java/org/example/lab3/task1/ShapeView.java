package org.example.lab3.task1;

public class ShapeView {
    public void printShapes(String title, Shape[] shapes) {
        System.out.println("\n" + title);
        for (Shape shape : shapes) {
            shape.draw();
        }
    }

    public void printArea(String message, double area) {
        System.out.printf("%s: %.2f%n", message, area);
    }
}
