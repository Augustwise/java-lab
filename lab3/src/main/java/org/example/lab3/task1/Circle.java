package org.example.lab3.task1;

public class Circle extends Shape {
    private final double radius;

    public Circle(String shapeColor, double radius) {
        super(shapeColor);
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        return Math.PI * radius * radius;
    }

    // example: Circle: radius=3.0, color=Green, area=28.27
    @Override
    public void draw() {
        System.out.println("Circle: " + this);
    }

    // example: radius=3.0, color=Green, area=28.27
    @Override
    public String toString() {
        return "radius=" + radius + ", " + super.toString();
    }
}
