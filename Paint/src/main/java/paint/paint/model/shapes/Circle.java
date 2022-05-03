
package paint.paint.model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public class Circle extends Ellipse {

    public Circle(Point2D startPos, Point2D endPos, Color strockColor, Color fillColor, int width) {
        super(startPos, endPos, strockColor, fillColor, width);
        if (super.gethRadius() < super.getvRadius()) {
            super.setvRadius(super.gethRadius());
        } else {
            super.sethRadius(super.getvRadius());
        }

    }

    public Circle() {

    }


}
