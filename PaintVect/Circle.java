package geometricShapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import colorManagement.ColorBundle;

public class Circle extends CustomShape {
  private int radius;

  public Circle(Point cntrPnt, Point refPnt, ColorBundle colorBundle) {
    this.cntrPnt = new Point(cntrPnt);
    this.refPnt = new Point(refPnt);
    this.colorBundle = new ColorBundle(colorBundle);
  }

  public void calculateDim() {
    int xDif = cntrPnt.x - refPnt.x;
    int yDif = cntrPnt.y - refPnt.y;
    radius = (int) Math.sqrt(xDif * xDif + yDif * yDif);
  }

  public boolean isInShape(Point start) {
    int xDif = start.x - cntrPnt.x;
    int yDif = start.y - cntrPnt.y;
    int dist = (int) (Math.sqrt(xDif * xDif + yDif * yDif));
    System.out.println(radius + " " + dist);
    return dist < radius;
  }

  public void draw(Graphics2D g) {
    calculateDim();
    Graphics2D sg = (Graphics2D) g.create();
    if (colorBundle.getHasFill()) {
      sg.setColor(colorBundle.getFillCol());
      sg.fillOval(cntrPnt.x - radius, cntrPnt.y - radius, 2 * radius,
          2 * radius);
    }
    if (!isSelected)
      sg.setStroke(new BasicStroke(colorBundle.getStrokeThickness()));
    sg.setColor(colorBundle.getStrokeCol());
    sg.drawOval(cntrPnt.x - radius, cntrPnt.y - radius, 2 * radius, 2 * radius);
    sg.dispose();
  }

}