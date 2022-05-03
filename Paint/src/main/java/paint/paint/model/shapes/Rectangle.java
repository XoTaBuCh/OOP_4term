
package paint.paint.model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import paint.paint.model.Shape;


public class Rectangle extends Shape {
    private double rwidth;
    private double height;

    public Rectangle() {

    }

    public void setRwidth(double rwidth) {
        this.rwidth = rwidth;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getRwidth() {
        return rwidth;
    }

    public double getHeight() {
        return height;
    }

    public Rectangle(Point2D startPos, Point2D endPos, Color strockColor, Color fillColor, int width) {
        super(startPos, endPos, strockColor, fillColor, width);
        rwidth = Math.abs(startPos.getX() - endPos.getX());
        height = Math.abs(startPos.getY() - endPos.getY());
    }

    @Override
    protected void getPropertiesToMap() {
        super.getPropertiesToMap();
        super.addToProperties("width", rwidth);
        super.addToProperties("height", height);
    }

    @Override
    protected void setPropertiesToVariables() {
        super.setPropertiesToVariables();
        rwidth = super.getFromMap("width");
        height = super.getFromMap("height");
    }

    @Override
    public void draw(Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(super.getColor());
        gc.strokeRect(super.getTopLeft().getX(), super.getTopLeft().getY(), rwidth, height);
        gc.setFill(super.getFillColor());
        gc.fillRect(super.getTopLeft().getX(), super.getTopLeft().getY(), rwidth, height);
        gc.setLineWidth(super.getWidth());
    }

}
