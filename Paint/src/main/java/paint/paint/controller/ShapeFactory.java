
package paint.paint.controller;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import paint.paint.model.*;
import paint.paint.model.shapes.*;

import java.util.HashMap;


//Factory DP
public class ShapeFactory {

    public ShapeFactory() {

    }

    public Shape createShape(String type, Point2D start, Point2D end, Color color, Color fillColor, int width) {
        Shape temp = null;
        switch (type) {
            case "Circle":
                temp = new Circle(start, end, color, fillColor, width);
                break;
            case "Ellipse":
                temp = new Ellipse(start, end, color, fillColor, width);
                break;
            case "Rectangle":
                temp = new Rectangle(start, end, color, fillColor, width);
                break;
            case "Square":
                temp = new Square(start, end, color, fillColor, width);
                break;
            case "Line":
                temp = new Line(start, end, color, fillColor, width);
                break;
            case "Triangle":
                temp = new Triangle(start, end, color, fillColor, width);
                break;
        }
        return temp;
    }

    public Shape createShape(String type, HashMap<String, Double> m) {
        Shape temp = null;
        switch (type) {
            case "Circle":
                temp = new Circle();
                break;
            case "Ellipse":
                temp = new Ellipse();
                break;
            case "Rectangle":
                temp = new Rectangle();
                break;
            case "Square":
                temp = new Square();
                break;
            case "Line":
                temp = new Line();
                break;
            case "Triangle":
                temp = new Triangle();
                break;
        }
        temp.setProperties(m);
        return temp;
    }
}
