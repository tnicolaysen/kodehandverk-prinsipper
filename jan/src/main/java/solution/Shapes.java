package solution;

import java.util.List;


interface Shape {
    void draw();
}

class Circle implements Shape {

    @Override
    public void draw() {
        // draw circle
    }
}

class Square implements Shape {

    @Override
    public void draw() {
        // draw square
    }
}


public class Shapes {

    void drawAllShapes(List<Shape> shapes) {
        for (Shape shape : shapes) {
           shape.draw();
        }
    }

}

