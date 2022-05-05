
package paint.paint.controller;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import paint.paint.model.*;
import paint.paint.model.shapes.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


//Factory DP
public class ShapeFactory {

    public ShapeFactory() {

    }

    /*public Shape createShape(String type, Point2D start, Point2D end, Color color, Color fillColor, int width) {
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
    }*/

    public Shape create(String type, HashMap<String, Double> m) {

        for (Class<Shape> shapeClass : shapeClasses) {
            if (shapeClass.getName().endsWith(type)) {
                try {
                    Constructor constructor = shapeClass.getConstructor();
                    Shape shape = (Shape) constructor.newInstance();
                    shape.setProperties(m);
                    return shape;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;

    }

    public Shape create(String type, Point2D start, Point2D end, Color color, Color fillColor, int width) {

        for (Class<Shape> shapeClass : shapeClasses) {
            if (shapeClass.getName().endsWith(type)) {
                try {
                    Constructor constructor = shapeClass.getConstructor(Point2D.class, Point2D.class, Color.class, Color.class, int.class);
                    Shape shape = (Shape) constructor.newInstance(start, end, color, fillColor, width);
                    return shape;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;

    }

    public List<Class<Shape>> shapeClasses = getActualFigures();

    static String pathToLibs = "/home/ubuntu/Documents/Study/OOP/OOP_4term/Paint/plugins";

    public static List<Class<Shape>> getActualFigures() {

        List<Class<Shape>> figures = new ArrayList<>();
        File folder = new File(Paths.get(pathToLibs).toString());
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles == null) return figures;

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                try {
                    JarFile jarFile = new JarFile(listOfFile.getAbsolutePath());
                    Enumeration<JarEntry> e = jarFile.entries();


                    URL[] urls = {new URL("jar:file:" + listOfFile.getAbsolutePath() + "!/")};
                    URLClassLoader cl = URLClassLoader.newInstance(urls);


                    while (e.hasMoreElements()) {
                        JarEntry je = e.nextElement();
                        if (je.isDirectory() || !je.getName().endsWith(".class")) {
                            continue;
                        }
                        // -6 because of .class
                        String className = je.getName().substring(0, je.getName().length() - 6);
                        className = className.replace('/', '.');

                        Class cls = cl.loadClass(className);

                        if (Shape.class.isAssignableFrom(cls)) {
                            figures.add((Class<Shape>) cls);
                            //System.out.println(cls);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return figures;
    }
}
