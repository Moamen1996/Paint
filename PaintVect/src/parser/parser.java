package parser;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import ActionManagement.ActionManager;
import geometricShapes.CustomShape;
import loader.LoadingClass;

public abstract class parser {

	public abstract ArrayList<CustomShape> load(String src, LoadingClass ls) throws Exception;

	public abstract void save(String src, ArrayList<CustomShape> shapes, ActionManager actionManager) throws FileNotFoundException, UnsupportedEncodingException;

}
