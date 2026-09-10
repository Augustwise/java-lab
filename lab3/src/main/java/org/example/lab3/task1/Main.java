package org.example.lab3.task1;

public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
                new Rectangle("Red", 4, 5),
                new Triangle("Blue", 6, 3),
                new Circle("Green", 3),
                new Rectangle("Yellow", 2, 7),
                new Triangle("Black", 8, 4),
                new Circle("White", 2),
                new Rectangle("Orange", 3, 3),
                new Triangle("Purple", 5, 6),
                new Circle("Blue", 4),
                new Rectangle("Green", 10, 2),
                new Triangle("Red", 7, 5),
                new Circle("Yellow", 1)
        };

        ShapeModel model = new ShapeModel(shapes);
        ShapeView view = new ShapeView();
        ShapeController controller = new ShapeController(model, view);

        controller.run();
    }
}
