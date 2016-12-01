package geometricShapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import colorManagement.ColorBundle;

public abstract class CustomShape {
  protected Point cntrPnt;
  protected Point refPnt;
  protected ColorBundle colorBundle;
  protected boolean isSelected;

  public abstract void draw(Graphics2D g);

  public abstract boolean isInShape(Point start);

  public void setColorBundle(ColorBundle colorBundle) {
    this.colorBundle = new ColorBundle(colorBundle);
  }

  public void setPoints(Point cntrPnt, Point refPnt) {
    this.cntrPnt = new Point(cntrPnt);
    this.refPnt = new Point(refPnt);
  }

  public ColorBundle getColorBundle() {
    return colorBundle;
  }

  public Point getCntrPoint() {
    return cntrPnt;
  }

  public Point getRefPoint() {
    return refPnt;
  }

  protected boolean pointInBetween(int x, int y, int z)
  {
    if(z >= Math.min(x,y) - 2 && z <= Math.max(x, y) + 2)
    {
      return true;
    }
    return false;
  }
  
  protected Point getDif(Point start, Point end) {
    Point dif = new Point();
    dif.setLocation(end.getX() - start.getX(), end.getY() - start.getY());
    return dif;
  }

  public void move(Point start, Point end) {
    Point dif = getDif(start, end);
    cntrPnt.setLocation(cntrPnt.getX() + dif.getX(),
        cntrPnt.getY() + dif.getY());
    refPnt.setLocation(refPnt.getX() + dif.getX(), refPnt.getY() + dif.getY());
  }

  public void resize(Point end) {
    this.refPnt = new Point(end);
  }

  protected void drawLine(Point start, Point end, Graphics2D g) {
    g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(),
        (int) end.getY());
  }
  
  public void drawDash(Graphics2D g) {
    Stroke dashed = new BasicStroke(colorBundle.getStrokeThickness(),
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
    Graphics2D sg = (Graphics2D) g.create();
    sg.setStroke(dashed);
    isSelected = true;
    draw(sg);
    isSelected = false;
    sg.dispose();
  }

}
