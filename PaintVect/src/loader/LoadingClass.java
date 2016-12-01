package loader;

import java.awt.Point;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import colorManagement.ColorBundle;
import geometricShapes.CustomShape;
import geometricShapes.Ellipse;
import geometricShapes.Line;
import geometricShapes.Rectangle;
import geometricShapes.Square;
import geometricShapes.Triangle;
import graphicalInterface.DrawingArea;
import graphicalInterface.GUIPanel;

public class LoadingClass {

  private ArrayList<String> dynamicSymbols;
  private Constructor<?>[][] dynamicConstructor;
  public ClassLoader cl;
  public boolean hasCustom;

  public LoadingClass() {
    dynamicSymbols = new ArrayList<String>();
    dynamicConstructor = new Constructor<?>[10][];
    hasCustom = false;
    initStaticSymbols();
    initStaticConstructors();
  }

  private void initStaticSymbols() {
    dynamicSymbols.add(Line.class.getSimpleName());
    dynamicSymbols.add(Triangle.class.getSimpleName());
    dynamicSymbols.add(Rectangle.class.getSimpleName());
    dynamicSymbols.add(Square.class.getSimpleName());
    dynamicSymbols.add(Ellipse.class.getSimpleName());
  }

  private void initStaticConstructors() {
    dynamicConstructor[DrawingArea.LINE] = Line.class.getConstructors();
    dynamicConstructor[DrawingArea.TRIANGLE] = Triangle.class.getConstructors();
    dynamicConstructor[DrawingArea.RECTANGLE] = Rectangle.class
        .getConstructors();
    dynamicConstructor[DrawingArea.SQUARE] = Square.class.getConstructors();
    dynamicConstructor[DrawingArea.ELLIPSE] = Ellipse.class.getConstructors();
  }

  public boolean hasObject(CustomShape obj) {
    String str = obj.getClass().getName();
    str = str.substring(str.indexOf('.') + 1);
    for (int i = 0; i < dynamicSymbols.size(); i++) {
      if (str.equals(dynamicSymbols.get(i)))
        return true;
    }
    return false;
  }

  public int getIndex(String className) {
    for (int index = 0; index < dynamicSymbols.size(); index++) {
      if (dynamicSymbols.get(index).equals(className)) {
        return index;
      }
    }
    return -1;
  }

  private void instanceError() {
    JOptionPane.showMessageDialog(GUIPanel.drawingPad,
        "The custom shape maybe Corrupted.\nPlease contact the Author.",
        "Error", JOptionPane.ERROR_MESSAGE);
  }

  public boolean load(String classPath, String type) {
    File file = new File(classPath);
    boolean operationDone = false;
    try {
      URL url = file.toURI().toURL();
      URL[] urls = new URL[] { url };
      cl = new URLClassLoader(urls);
      Class<?> cls = cl.loadClass("geometricShapes." + type);
      if(dynamicSymbols.size() == 6)
      {
        dynamicSymbols.remove(5);
      }
      dynamicSymbols.add(cls.getSimpleName());
      Constructor<?>[] curConst = cls.getConstructors();
      dynamicConstructor[DrawingArea.CUSTOM] = curConst;
      operationDone = true;
      hasCustom = true;
    } catch (Exception e) {
      hasCustom = false;
      instanceError();
    }
    return operationDone;
  }

  public CustomShape getObject(int type, ColorBundle colorBundle, Point cntr,
      Point ref) {
    try {
      int sz = dynamicConstructor[type].length;
      Constructor<?> constructor = dynamicConstructor[type][sz - 1];
      Object obj = constructor.newInstance(cntr, ref, colorBundle);
      return (CustomShape) obj;
    } catch (Exception e) {
      instanceError();
    }
    return null;
  }

  public Triangle getTriangle(ColorBundle colorBundle, Point cntr, Point ref,
      Point thirdPnt) {
    try {
      int sz = dynamicConstructor[DrawingArea.TRIANGLE].length;
      Constructor<?> constructor = dynamicConstructor[DrawingArea.TRIANGLE][sz - 1];
      Triangle obj = (Triangle) constructor.newInstance(cntr, ref, thirdPnt,
          colorBundle);
      return obj;
    } catch (Exception e) {
      instanceError();
    }
    return null;
  }
}
