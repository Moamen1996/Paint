package parser;

import geometricShapes.CustomShape;
import loader.LoadingClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import ActionManagement.ActionManager;

public class XMLParser extends parser {
  private XStream xs;
  private static final char customLine = '\r';
  private static final char newLine = '\n';
  private static final String xmlSetting = new String("<?xml version=\"1.0\" ?>\r\n");

  public XMLParser() {
    xs = new XStream(new DomDriver());
  }

  @SuppressWarnings("unchecked")
  @Override
  public ArrayList<CustomShape> load(String src, LoadingClass ls) {
    ArrayList<CustomShape> geomShapes = null;
    File xmlFile = new File(src);
    if(ls.hasCustom)
      xs.setClassLoader(ls.cl);
    geomShapes = (ArrayList<CustomShape>) xs.fromXML(xmlFile);
    return geomShapes;
  }

  @Override
  public void save(String src, ArrayList<CustomShape> shapes,
      ActionManager actionManager) throws FileNotFoundException, UnsupportedEncodingException {
    ArrayList<CustomShape> visibleItems = new ArrayList<CustomShape>();
    for (int i = 0; i < shapes.size(); i++) {
      if (actionManager.isShapeVisible(i))
        visibleItems.add(shapes.get(i));
    }
    String xmlData = xs.toXML(visibleItems);
    PrintWriter writer = new PrintWriter(src + ".xml", "UTF-8");
    writer.println(xmlSetting + customLine + newLine);
    for (int i = 0; i < xmlData.length(); i++) {
      if (xmlData.charAt(i) == newLine)
        writer.print(customLine);
      writer.print(xmlData.charAt(i));
    }
    writer.close();
  }
  
  public String toXML(CustomShape obj)
  {
    String xmlData = xs.toXML(obj);
    return xmlData;
  }
  
  public CustomShape toShape(String xmlData)
  {
    CustomShape x = (CustomShape)xs.fromXML(xmlData);
    return x;
  }
}
