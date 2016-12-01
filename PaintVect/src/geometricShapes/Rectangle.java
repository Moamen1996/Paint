package geometricShapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import colorManagement.ColorBundle;

public class Rectangle extends CustomShape {

  protected int width;
  protected int height;

  public Rectangle(Point cntrPnt, Point refPnt, ColorBundle colorBundle) {
    setColorBundle(colorBundle);
    this.cntrPnt = new Point(cntrPnt);
    this.refPnt = new Point(refPnt);
  }

  private void calculateDimensions() {
    width = Math.abs(this.cntrPnt.x - this.refPnt.x);
    height = Math.abs(this.cntrPnt.y - this.refPnt.y);
    if (this instanceof Square) {
      width = Math.max(width, height);
      height = Math.max(width, height);
    }
    int g1 = (refPnt.x < cntrPnt.x ? -1 : 1);
    int g2 = (refPnt.y < cntrPnt.y ? -1 : 1);
    this.refPnt = new Point(cntrPnt.x + g1 * width, cntrPnt.y + g2 * height);
  }

  @Override
  public boolean isInShape(Point start) {
    if (pointInBetween(cntrPnt.x, refPnt.x, start.x)
        && pointInBetween(cntrPnt.y, refPnt.y, start.y))
      return true;
    return false;
  }

  public void draw(Graphics2D g) {
    calculateDimensions();
    Graphics2D sg = (Graphics2D) g.create();
    if (colorBundle.getHasFill()) {
      sg.setColor(colorBundle.getFillCol());
      sg.fillRect(Math.min(this.cntrPnt.x, this.refPnt.x),
          Math.min(this.cntrPnt.y, this.refPnt.y), width, height);
    }
    if (!isSelected)
      sg.setStroke(new BasicStroke(colorBundle.getStrokeThickness()));
    sg.setColor(colorBundle.getStrokeCol());
    sg.drawRect(Math.min(this.cntrPnt.x, this.refPnt.x),
        Math.min(this.cntrPnt.y, this.refPnt.y), width, height);
    sg.dispose();
  }

}