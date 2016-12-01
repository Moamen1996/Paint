package colorManagement;

import java.awt.Color;

public class ColorBundle {

  private Color fillCol, strokeCol;
  private int strokeThickness;
  private boolean hasFill;

  public ColorBundle() {
    this.strokeCol = new Color(Color.BLACK.getRGB());
    this.fillCol = new Color(Color.WHITE.getRGB());
    this.strokeThickness = 2;
    this.hasFill = false;
  }

  public ColorBundle(ColorBundle colorBundle) {
    this.strokeCol = new Color(colorBundle.getStrokeCol().getRGB());
    this.fillCol = new Color(colorBundle.getFillCol().getRGB());
    this.hasFill = colorBundle.getHasFill();
    this.strokeThickness = colorBundle.getStrokeThickness();
  }

  public boolean getHasFill() {
    return hasFill;
  }

  public Color getFillCol() {
    return this.fillCol;
  }

  public Color getStrokeCol() {
    return this.strokeCol;
  }

  public int getStrokeThickness() {
    return this.strokeThickness;
  }

  public void setHasFill(boolean hasFill) {
    this.hasFill = hasFill;
  }

  public void setFillCol(Color fillCol) {
    this.fillCol = new Color(fillCol.getRGB());
  }

  public void setStrokeCol(Color strokeCol) {
    this.strokeCol = new Color(strokeCol.getRGB());
  }

  public void setStrokeThickness(int strokeThickness) {
    this.strokeThickness = strokeThickness;
  }
}
