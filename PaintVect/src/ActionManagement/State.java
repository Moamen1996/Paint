package ActionManagement;

import java.awt.Point;

import colorManagement.ColorBundle;

public class State {

  public Point firstPoint, secondPoint;
  private ColorBundle colorBundle;
  public boolean visibile;
  public Point triPoint;

  public State(Point firstPoint, Point secondPoint, ColorBundle colorBundle,
      boolean visible) {
    this.firstPoint = new Point(firstPoint);
    this.secondPoint = new Point(secondPoint);
    this.colorBundle = new ColorBundle(colorBundle);
    this.visibile = visible;
  }

  public void setTriPoint(Point triPoint) {
    this.triPoint = new Point(triPoint);
  }

  public ColorBundle getColorBundle() {
    return colorBundle;
  }
}
