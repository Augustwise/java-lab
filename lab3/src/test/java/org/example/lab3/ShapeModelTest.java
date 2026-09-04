package org.example.lab3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShapeModelTest {
    @Test
    void calculatesAreasOfShapes() {
        assertEquals(20.0, new Rectangle("Red", 4, 5).calcArea());
        assertEquals(9.0, new Triangle("Blue", 6, 3).calcArea());
        assertEquals(Math.PI * 9, new Circle("Green", 3).calcArea());
    }

    @Test
    void calculatesTotalAreaAndAreaByType() {
        Shape[] shapes = {
                new Rectangle("Red", 4, 5),
                new Triangle("Blue", 6, 3),
                new Triangle("Green", 4, 2)
        };
        ShapeModel model = new ShapeModel(shapes);

        assertEquals(33.0, model.calcTotalArea());
        assertEquals(13.0, model.calcTotalAreaByType(Triangle.class));
    }

    @Test
    void sortsShapesByArea() {
        Shape[] shapes = {
                new Rectangle("Red", 4, 5),
                new Circle("Green", 1),
                new Triangle("Blue", 6, 3)
        };
        ShapeModel model = new ShapeModel(shapes);

        model.sort(new ShapeAreaComparator());

        assertEquals(Circle.class, shapes[0].getClass());
        assertEquals(Triangle.class, shapes[1].getClass());
        assertEquals(Rectangle.class, shapes[2].getClass());
    }

    @Test
    void sortsShapesByColor() {
        Shape[] shapes = {
                new Rectangle("Red", 4, 5),
                new Circle("Green", 1),
                new Triangle("Blue", 6, 3)
        };
        ShapeModel model = new ShapeModel(shapes);

        model.sort(new ShapeColorComparator());

        String[] colors = {
                shapes[0].getShapeColor(),
                shapes[1].getShapeColor(),
                shapes[2].getShapeColor()
        };
        assertArrayEquals(new String[]{"Blue", "Green", "Red"}, colors);
    }
}
