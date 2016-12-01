package graphicalInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import parser.JSON;
import parser.XMLParser;
import parser.parser;
import loader.LoadingClass;
import colorManagement.ColorBundle;
import ActionManagement.ActionManager;
import geometricShapes.CustomShape;
import geometricShapes.Triangle;

@SuppressWarnings("serial")
public class DrawingArea extends JComponent {

  private LoadingClass ls;
  private ActionManager actionManager;
  private ArrayList<CustomShape> geomShapes;
  protected static final int MAXX = 700, MAXY = 500;
  private Image grid = createImage(MAXX, MAXY);
  private ColorBundle colorBundle;
  private Point start, end, mid;
  private boolean mouseOn = false;
  public final static int LINE = 0, TRIANGLE = 1, RECTANGLE = 2, SQUARE = 3,
      ELLIPSE = 4, CUSTOM = 5;
  public final static int ADD = 0, RESIZE = 1, MOVE = 2, SELECT = 3;
  private int curShape = 0;
  private int curState = 0;
  private int selectedShape = -1;
  private Point t1, t2;
  private Graphics2D graphic;
  private boolean triangleOn = false, triangleOff = false;
  private int triangleVertex;
  
  public void setStrokeColor(Color strokeCol)
  {
    this.colorBundle.setStrokeCol(strokeCol);
    if(selectedShape != -1)
    {
      geomShapes.get(selectedShape).getColorBundle().setStrokeCol(strokeCol);
      saveCurrentState(selectedShape, true);
    }
    repaint();
  }

  public void setFillColor(Color fillCol)
  {
    this.colorBundle.setFillCol(fillCol);
    this.colorBundle.setHasFill(true);
    if(selectedShape != -1)
    {
      geomShapes.get(selectedShape).getColorBundle().setFillCol(fillCol);
      geomShapes.get(selectedShape).getColorBundle().setHasFill(true);
      saveCurrentState(selectedShape, true);
    }
    repaint();
  }
  
  public void setStrokeThickness(int strokeThickness)
  {
    this.colorBundle.setStrokeThickness(strokeThickness);
    if(selectedShape != -1)
    {
      geomShapes.get(selectedShape).getColorBundle().setStrokeThickness(strokeThickness);
      saveCurrentState(selectedShape, true);
    }
    repaint();
  }
  
  public void removeFill()
  {
    this.colorBundle.setHasFill(false);
    if(selectedShape != -1 && geomShapes.get(selectedShape).getColorBundle().getHasFill())
    {
      geomShapes.get(selectedShape).getColorBundle().setHasFill(false);
      saveCurrentState(selectedShape, true);
    }
  }
  public void saveCurrentState(int stateShape, boolean isVisible) {
    ColorBundle colorBundle;
    Point cntrPnt, refPnt, triPoint;
    colorBundle = geomShapes.get(stateShape).getColorBundle();
    cntrPnt = new Point(geomShapes.get(stateShape).getCntrPoint());
    refPnt = new Point(geomShapes.get(stateShape).getRefPoint());
    if (geomShapes.get(stateShape) instanceof Triangle)
      triPoint = new Point(
          ((Triangle) geomShapes.get(stateShape)).getThirdPoint());
    else
      triPoint = null;
    actionManager.addToHistory(stateShape, cntrPnt, refPnt, triPoint,
        colorBundle, isVisible, geomShapes);
  }

  public void setState(int curState) {
    this.curState = curState;
  }

  public void setShape(int curShape) {
    this.curShape = curShape;
  }

  private CustomShape getShape(Point start, Point end) {
    CustomShape reqShape;
    if (curShape == TRIANGLE && !triangleOn)
      reqShape = ls.getObject(LINE, colorBundle, start, end);
    else if (triangleOn) {
      reqShape = ls.getTriangle(colorBundle, t1, t2, mid);
    } else{
      reqShape = ls.getObject(curShape, colorBundle, start, end);
    }
    return reqShape;
  }

  public void newPaint() {
    geomShapes = new ArrayList<CustomShape>();
    actionManager = new ActionManager();
    selectedShape = -1;
    repaint();
  }

  public DrawingArea(ColorBundle colorBundle) {
    geomShapes = new ArrayList<CustomShape>();
    actionManager = new ActionManager();
    this.colorBundle = new ColorBundle(colorBundle);
    setSize(MAXX, MAXY);
    setBackground(Color.black);
    ls = new LoadingClass();
    repaint();
    forMouseActions();
  }

  public void removeShape() {
    if (selectedShape < 0 || selectedShape >= geomShapes.size())
      errorPop(new String("No shape is selected to be removed.\nPlease Recheck"));
    else {
      saveCurrentState(selectedShape, false);
    }
    selectedShape = -1;
    repaint();
  }

  private void errorPop(String st) {
    JOptionPane.showMessageDialog(GUIPanel.drawingPad, st, "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  private void setSelected(Point start) {
    int curSelectedShape = -1;
    for (int i = geomShapes.size() - 1; i >= 0; i--) {
      if (geomShapes.get(i).isInShape(start) && actionManager.isShapeVisible(i)) {
        curSelectedShape = i;
        break;
      }
    }
    if (curSelectedShape == -1 && selectedShape == -1) {
      errorPop(new String("No shape in this Region.\nPlease recheck."));
    } else if (curSelectedShape == -1 && selectedShape != -1) {
      selectedShape = -1;
    } else if (curSelectedShape != -1) {
      selectedShape = curSelectedShape;
    }
    repaint();
  }

  private void forMouseActions() {

    addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        int currentX = e.getX();
        int currentY = e.getY();
        start = new Point(currentX, currentY);
        if (curState == ADD) {
          selectedShape = -1;
        } else if (curState == RESIZE && selectedShape != -1
            && geomShapes.get(selectedShape) instanceof Triangle) {
          triangleVertex = ((Triangle) geomShapes.get(selectedShape))
              .getNearestPoint(start);
        }
        if (triangleOn) {
          triangleOn = false;
          geomShapes.add(new Triangle(t1, t2, start, colorBundle));
          saveCurrentState(geomShapes.size() - 1, true);
          mouseOn = false;
          triangleOff = true;
          repaint();
        }
        if (curState == SELECT) {
          setSelected(start);
          return;
        }
        mouseOn = true;
      }

      public void mouseReleased(MouseEvent e) {
        if (triangleOff) {
          triangleOff = false;
          mouseOn = false;
          return;
        }
        int currentX = e.getX();
        int currentY = e.getY();
        end = new Point(currentX, currentY);
        if (curState == ADD && curShape == TRIANGLE && mouseOn) {
          t1 = new Point(start);
          t2 = new Point(end);
          mid = new Point(currentX, currentY);
          triangleOn = true;
          return;
        } else if (curState == ADD) {
          geomShapes.add(getShape(start, end));
          saveCurrentState(geomShapes.size() - 1, true);
        } else if (curState == MOVE) {
          if (selectedShape == -1)
            errorPop(new String("No Shape is selected to be Moved."));
          else {
            geomShapes.get(selectedShape).move(start, end);
            saveCurrentState(selectedShape, true);
          }
        } else if (curState == RESIZE) {
          if (selectedShape == -1) {
            errorPop(new String("No Shape is selected to be Resized."));
          } else if (geomShapes.get(selectedShape) instanceof Triangle) {
            ((Triangle) geomShapes.get(selectedShape)).resize(triangleVertex,
                end);
          } else {
            geomShapes.get(selectedShape).resize(end);
            saveCurrentState(selectedShape, true);
          }
        }
        mouseOn = false;
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if (triangleOff)
          return;
        int currentX = e.getX();
        int currentY = e.getY();
        mid = new Point(currentX, currentY);
        if (curState == MOVE && selectedShape != -1) {
          geomShapes.get(selectedShape).move(start, mid);
          start = mid;
        }
        repaint();
      }

      public void mouseMoved(MouseEvent e) {
        int currentX = e.getX();
        int currentY = e.getY();
        mid = new Point(currentX, currentY);
        repaint();
      }
    });
  }

  public void paint(Graphics g) {
    if (grid == null) {
      grid = createImage(getSize().width, getSize().height);
      graphic = (Graphics2D) grid.getGraphics();
      graphic.setStroke(new BasicStroke(1));
      graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      graphic.setClip(0, 0, MAXX, MAXY);
      clear((Graphics2D) grid.getGraphics());
    } else {
      clear((Graphics2D) grid.getGraphics());
      graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      int vSize = geomShapes.size();
      for (int i = 0; i < vSize; i++) {
        if ((curState == RESIZE && mouseOn && selectedShape == i)
            || !actionManager.isShapeVisible(i))
          continue;
        else if (selectedShape == i) {
          geomShapes.get(i).drawDash(graphic);
        } else {
          geomShapes.get(i).draw(graphic);
        }
      }
      if (mouseOn) {
        if (curState == ADD)
          getShape(start, mid).draw(graphic);
        else if (curState == RESIZE && selectedShape != -1) {
          if (geomShapes.get(selectedShape) instanceof Triangle) {
            ((Triangle) geomShapes.get(selectedShape)).resize(triangleVertex,
                mid);
          } else {
            geomShapes.get(selectedShape).resize(mid);
          }
          geomShapes.get(selectedShape).drawDash(graphic);
        }
      }
    }
    g.drawImage(grid, 0, 0, null);
  }

  private void clear(Graphics2D g) {
    g.setPaint(Color.white);
    g.fillRect(0, 0, MAXX, MAXY);
    g.setPaint(Color.black);
  }

  public void redo() {
    selectedShape = -1;
    this.curState = ADD;
    geomShapes = actionManager.redo(geomShapes);
    repaint();
  }

  public void undo() {
    selectedShape = -1;
    this.curState = ADD;
    geomShapes = actionManager.undo(geomShapes);
    repaint();
  }

  public void saveXML(File file) throws FileNotFoundException, UnsupportedEncodingException {
    XMLParser xp = new XMLParser();
    xp.save(file.getPath(), geomShapes, actionManager);
  }

  public void saveJSON(File file) throws FileNotFoundException, UnsupportedEncodingException {
    JSON jp = new JSON();
    jp.save(file.getPath(), geomShapes, actionManager);
  }

  public void load(File file) throws Exception {
    newPaint();
    String str = file.getPath();
    parser p;
    ArrayList<CustomShape> loadedShapes = new ArrayList<CustomShape>();
    if (str.substring(str.length() - 4).equals(".xml")) {
      p = new XMLParser();
    } else if (str.substring(str.length() - 5).equals(".json")) {
      p = new JSON();
    } else {
      throw new Exception("Wrong file Format.");
    }
    loadedShapes = p.load(str, ls);
    for (int i = 0; i < loadedShapes.size(); i++) {
      geomShapes.add(loadedShapes.get(i));
      saveCurrentState(geomShapes.size() - 1, true);
    }
    actionManager.setLoadVal(geomShapes.size() - 1);
  }

  public boolean loadClass(File file) {
    String str = file.getPath();
    String fileName = str.substring(str.lastIndexOf("\\") + 1);
    boolean isLoaded = ls.load(str,
        fileName.substring(0, fileName.length() - 4));
    if (isLoaded)
      return true;
    return false;
  }
}
