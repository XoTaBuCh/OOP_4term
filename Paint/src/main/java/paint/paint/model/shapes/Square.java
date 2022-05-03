
package paint.paint.model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public class Square extends Rectangle {

    public Square(Point2D startPos, Point2D endPos, Color strockColor, Color fillColor, int width) {
        super(startPos, endPos, strockColor, fillColor, width);
        if (super.getHeight() < super.getRwidth()) {
            super.setRwidth(super.getHeight());
        } else {
            super.setHeight(super.getRwidth());
        }
    }

    public Square() {

    }

}
