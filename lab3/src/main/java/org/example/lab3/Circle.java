package org.example.lab3;

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

    @Override
    public void draw() {
        System.out.println("Circle: " + this);
    }

    @Override
    public String toString() {
        return "radius=" + radius + ", " + super.toString();
    }
}
