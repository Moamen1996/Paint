package geometricShapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import colorManagement.ColorBundle;

public class Triangle extends CustomShape {

  private Point thirdPnt;

  public Triangle(Point cntrPnt, Point refPnt, Point thirdPnt,
      ColorBundle colorBundle) {
    setColorBundle(colorBundle);
    super.cntrPnt = new Point(cntrPnt);
    super.refPnt = new Point(refPnt);
    this.thirdPnt = new Point(thirdPnt);
  }

  public void setThirdPoint(Point thirdPnt) {
    this.thirdPnt = new Point(thirdPnt);
  }

  public Point getThirdPoint() {
    return thirdPnt;
  }

  public int getNearestPoint(Point start) {
    double cntrDist = cntrPnt.distance(start);
    double refDist = refPnt.distance(start);
    double thirdDist = thirdPnt.distance(start);
    if (cntrDist <= refDist && cntrDist <= thirdDist) {
      return 0;
    } else if (refDist <= cntrDist && refDist <= thirdDist) {
      return 1;
    } else {
      return 2;
    }
  }

  public void resize(int index, Point start) {
    if (index == 0) {
      cntrPnt = new Point(start);
    } else if (index == 1) {
      refPnt = new Point(start);
    } else {
      thirdPnt = new Point(start);
    }
  }

  public void move(Point start, Point end) {
    Point dif = getDif(start, end);
    cntrPnt.setLocation(cntrPnt.getX() + dif.getX(),
        cntrPnt.getY() + dif.getY());
    refPnt.setLocation(refPnt.getX() + dif.getX(), refPnt.getY() + dif.getY());
    thirdPnt.setLocation(thirdPnt.getX() + dif.getX(),
        thirdPnt.getY() + dif.getY());
  }

  private double crossProduct(Point p1, Point p2, Point p3) {
    return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
  }

  @Override
  public boolean isInShape(Point start) {
    boolean b1, b2, b3;
    b1 = crossProduct(start, cntrPnt, refPnt) < 0.0;
    b2 = crossProduct(start, refPnt, thirdPnt) < 0.0;
    b3 = crossProduct(start, thirdPnt, cntrPnt) < 0.0;
    return ((b1 == b2) && (b2 == b3));
  }

  @Override
  public void draw(Graphics2D g) {
    Graphics2D sg = (Graphics2D) g.create();
    int[] x = { cntrPnt.x, refPnt.x, thirdPnt.x };
    int[] y = { cntrPnt.y, refPnt.y, thirdPnt.y };
    if (colorBundle.getHasFill()) {
      sg.setColor(colorBundle.getFillCol());
      sg.fillPolygon(x, y, 3);
    }
    if (!isSelected)
      sg.setStroke(new BasicStroke(colorBundle.getStrokeThickness()));
    sg.setColor(colorBundle.getStrokeCol());
    sg.drawPolygon(x, y, 3);
    sg.dispose();
  }

}