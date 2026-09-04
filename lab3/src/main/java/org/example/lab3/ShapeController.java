package org.example.lab3;

public class ShapeController {
    private final ShapeModel model;
    private final ShapeView view;

    public ShapeController(ShapeModel model, ShapeView view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        view.printShapes("Initial data set:", model.getShapes());

        view.printArea("Total area of all shapes", model.calcTotalArea());
        view.printArea("Total area of triangles",
                model.calcTotalAreaByType(Triangle.class));

        model.sort(new ShapeAreaComparator());
        view.printShapes("Shapes sorted by area:", model.getShapes());

        model.sort(new ShapeColorComparator());
        view.printShapes("Shapes sorted by color:", model.getShapes());
    }
}
