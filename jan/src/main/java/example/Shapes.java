package example;

import java.util.ArrayList;

enum ShapeType {CIRCLE, SQUARE};

interface Shape {
    ShapeType getType();
}

class Circle implements Shape {

    @Override
    public ShapeType getType() {
        return ShapeType.CIRCLE;
    }

}

class Square implements Shape {

    @Override
    public ShapeType getType() {
        return ShapeType.SQUARE;
    }

}

public class Shapes {

    void drawAllShapes(ArrayList<Shape> shapes) {
        for (Shape shape : shapes) {
            switch (shape.getType()) {
                case CIRCLE:
                    drawCircle((Circle) shape);
                    break;
                case SQUARE:
                    drawSquare((Square) shape);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    void drawCircle(Circle circle) {
        // draw  circle
    }

    void drawSquare(Square square) {
        // draw square
    }

}

