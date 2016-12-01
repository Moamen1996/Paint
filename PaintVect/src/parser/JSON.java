package parser;

import java.awt.Color;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ActionManagement.ActionManager;
import colorManagement.ColorBundle;
import geometricShapes.CustomShape;
import geometricShapes.Triangle;
import loader.LoadingClass;

public class JSON extends parser {

  private final static int TRIANGLE = 1;

  public ArrayList<CustomShape> load(String src, LoadingClass ls)
      throws Exception {
    ArrayList<CustomShape> shapes = new ArrayList<CustomShape>();
    JSONParser parser = new JSONParser();
    long cX, cY, rX, rY;
    long fCol, sCol;
    Color fillCol, stkCol;
    long thickness;
    String type;
    boolean hasFill;
    ColorBundle colBundle = new ColorBundle();
    int shapeType;
    boolean invalid = false;
    try {
      Object obj = parser.parse(new FileReader(src));
      JSONObject object = (JSONObject) obj;
      JSONArray jsonArray = new JSONArray();
      jsonArray = (JSONArray) object.get("list");
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject jsonObject;
        jsonObject = (JSONObject) jsonArray.get(i);
        type = (String) jsonObject.get("type");
        shapeType = ls.getIndex(type);
        if (shapeType == -1) {
          invalid = true;
          break;
        }
        CustomShape newObj;
        cX = (long) jsonObject.get("cntrPntX");
        cY = (long) jsonObject.get("cntrPntY");
        rX = (long) jsonObject.get("refPntX");
        rY = (long) jsonObject.get("refPntY");
        fCol = (long) jsonObject.get("fillCol");
        sCol = (long) jsonObject.get("SrokeCol");
        thickness = (long) jsonObject.get("Thickness");
        fillCol = new Color((int) fCol);
        stkCol = new Color((int) sCol);
        hasFill = (boolean) jsonObject.get("HasFill");
        colBundle.setFillCol(fillCol);
        colBundle.setHasFill(hasFill);
        colBundle.setStrokeCol(stkCol);
        colBundle.setStrokeThickness((int) thickness);
        if (shapeType == TRIANGLE) {
          long tX = (long) jsonObject.get("triPntX");
          long tY = (long) jsonObject.get("triPntY");
          newObj = ls.getTriangle(colBundle, new Point((int) cX, (int) cY),
              new Point((int) rX, (int) rY), new Point((int) tX, (int) tY));
        } else {
          newObj = ls.getObject(shapeType, colBundle, new Point((int) cX,
              (int) cY), new Point((int) rX, (int) rY));
        }
        shapes.add(newObj);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if(invalid){
      throw new Exception("Invalid File");
    }
    return shapes;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void save(String src, ArrayList<CustomShape> shapes,
      ActionManager actionManager) throws FileNotFoundException, UnsupportedEncodingException {
    
    PrintWriter writer = new PrintWriter(src + ".json", "UTF-8");
    JSONArray jsonArray = new JSONArray();
    JSONObject obj = new JSONObject();
    for (int i = 0; i < shapes.size(); i++) {
      if (!actionManager.isShapeVisible(i))
        continue;
      String type = shapes.get(i).getClass().getSimpleName();
      obj = new JSONObject();
      obj.put("type", type);
      obj.put("cntrPntX", shapes.get(i).getCntrPoint().x);
      obj.put("cntrPntY", shapes.get(i).getCntrPoint().y);
      obj.put("refPntX", shapes.get(i).getRefPoint().x);
      obj.put("refPntY", shapes.get(i).getRefPoint().y);
      obj.put("fillCol", shapes.get(i).getColorBundle().getFillCol().getRGB());
      obj.put("SrokeCol", shapes.get(i).getColorBundle().getStrokeCol()
          .getRGB());
      obj.put("Thickness", shapes.get(i).getColorBundle().getStrokeThickness());
      obj.put("HasFill", shapes.get(i).getColorBundle().getHasFill());
      if (type.equals("Triangle")) {
        obj.put("triPntX", ((Triangle) shapes.get(i)).getThirdPoint().x);
        obj.put("triPntY", ((Triangle) shapes.get(i)).getThirdPoint().y);
      }
      jsonArray.add(obj);
    }
    JSONObject object = new JSONObject();
    object.put("list", jsonArray);
    writer.println(object);
    writer.close();
  }
}
