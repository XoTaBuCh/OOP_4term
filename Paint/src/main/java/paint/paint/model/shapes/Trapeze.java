package paint.paint.model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import paint.paint.model.Shape;

public class Trapeze extends Shape {

    public double[] getCoordsX() {
        return coordsX;
    }

    public void setCoordsX(double[] coordsX) {
        this.coordsX = coordsX;
    }

    public double[] getCoordsY() {
        return coordsY;
    }

    public void setCoordsY(double[] coordsY) {
        this.coordsY = coordsY;
    }

    private double[] coordsX = new double[4];
    private double[] coordsY = new double[4];

    public Trapeze(){

    }

    public Trapeze(Point2D startPos, Point2D endPos, Color strockColor, Color fillColor, int width) {
        super(startPos, endPos, strockColor, fillColor, width);
        coordsX[0] = startPos.getX(); coordsY [0] = startPos.getY();
        coordsX[1] = startPos.getX() + (endPos.getX() - startPos.getX()) / 2; coordsY [1] = startPos.getY();
        coordsX[2] = endPos.getX(); coordsY [2] = endPos.getY();
        coordsX[3] = startPos.getX() - (endPos.getX() - startPos.getX()) / 2; coordsY [3] = endPos.getY();

    }

    @Override
    protected void getPropertiesToMap() {
        super.getPropertiesToMap();
        super.addToProperties("coordsX0", coordsX[0]);
        super.addToProperties("coordsX1", coordsX[1]);
        super.addToProperties("coordsX2", coordsX[2]);
        super.addToProperties("coordsX3", coordsX[3]);
        super.addToProperties("coordsY0", coordsY[0]);
        super.addToProperties("coordsY1", coordsY[1]);
        super.addToProperties("coordsY2", coordsY[2]);
        super.addToProperties("coordsY3", coordsY[3]);
    }

    @Override
    protected void setPropertiesToVariables() {
        super.setPropertiesToVariables();
        coordsX[0] = super.getFromMap("coordsX0");
        coordsX[1] = super.getFromMap("coordsX1");
        coordsX[2] = super.getFromMap("coordsX2");
        coordsX[3] = super.getFromMap("coordsX3");
        coordsY[0] = super.getFromMap("coordsY0");
        coordsY[1] = super.getFromMap("coordsY1");
        coordsY[2] = super.getFromMap("coordsY2");
        coordsY[3] = super.getFromMap("coordsY3");

    }

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(super.getColor());
        gc.strokePolygon(coordsX, coordsY, 4);
        gc.setFill(super.getFillColor());
        gc.fillPolygon(coordsX, coordsY, 4);
        gc.setLineWidth(super.getWidth());
    }
}
