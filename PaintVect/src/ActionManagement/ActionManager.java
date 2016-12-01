package ActionManagement;

import geometricShapes.CustomShape;
import geometricShapes.Triangle;
import graphicalInterface.GUIPanel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

import colorManagement.ColorBundle;

public class ActionManager {

	private ArrayList<Stack<State>> prev;
	private ArrayList<Stack<State>> next;
	private int[] changesAtThisPoint;
	private boolean[] isVisible;
	private int historyIdx, maxHistoryIdx;
	private int loadVal;

	public ActionManager() {
		prev = new ArrayList<Stack<State>>();
		next = new ArrayList<Stack<State>>();
		changesAtThisPoint = new int[1000];
		isVisible = new boolean[1000];
		historyIdx = -1;
		maxHistoryIdx = -1;
		loadVal = -1;
	}

	public void setLoadVal(int loadVal) {
		this.loadVal = loadVal;
	}

	public void setVisible(int i, boolean curVisibility) {
		isVisible[i] = curVisibility;
	}

	public void addToHistory(int idx, Point cntrPnt, Point refPnt, Point triPoint, ColorBundle colorBundle,
			boolean visible, ArrayList<CustomShape> geomShapes) {
		if (idx >= prev.size()) {
			prev.add(new Stack<State>());
			next.add(new Stack<State>());
			prev.get(idx).add(new State(cntrPnt, refPnt, colorBundle, false));
		}
		State newState = new State(cntrPnt, refPnt, colorBundle, visible);
		if (triPoint != null)
			newState.setTriPoint(triPoint);
		prev.get(idx).add(newState);
		for (int i = 0; i < prev.size(); i++)
			next.get(i).clear();
		changesAtThisPoint[++historyIdx] = idx;
		maxHistoryIdx = historyIdx;
		updateGeomShapes(geomShapes);
	}

	private ArrayList<CustomShape> updateGeomShapes(ArrayList<CustomShape> geom) {
		ArrayList<CustomShape> geomShapes = new ArrayList<CustomShape>(geom);
		ColorBundle colorBundle;
		Point cntPnt = new Point();
		Point refPnt = new Point();
		for (int i = 0; i < geomShapes.size(); i++) {
			if (prev.get(i).peek().visibile == false) {
				isVisible[i] = false;
				continue;
			}
			colorBundle = new ColorBundle(prev.get(i).peek().getColorBundle());
			cntPnt.x = prev.get(i).peek().firstPoint.x;
			cntPnt.y = prev.get(i).peek().firstPoint.y;
			refPnt.x = prev.get(i).peek().secondPoint.x;
			refPnt.y = prev.get(i).peek().secondPoint.y;
			geomShapes.get(i).setColorBundle(colorBundle);
			geomShapes.get(i).setPoints(cntPnt, refPnt);
			if (geomShapes.get(i) instanceof Triangle)
				((Triangle) geomShapes.get(i)).setThirdPoint(prev.get(i).peek().triPoint);
			isVisible[i] = true;
		}
		return geomShapes;
	}

	public ArrayList<CustomShape> undo(ArrayList<CustomShape> geom) {
		ArrayList<CustomShape> geomShapes = new ArrayList<CustomShape>(geom);
		if (historyIdx <= loadVal) {
			JOptionPane.showMessageDialog(GUIPanel.drawingPad, "No action to be undone.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return geomShapes;
		}
		int idx = changesAtThisPoint[historyIdx--];
		next.get(idx).add(prev.get(idx).pop());
		return updateGeomShapes(geomShapes);
	}

	public ArrayList<CustomShape> redo(ArrayList<CustomShape> geom) {
		ArrayList<CustomShape> geomShapes = new ArrayList<CustomShape>(geom);
		if (historyIdx >= maxHistoryIdx) {
			JOptionPane.showMessageDialog(GUIPanel.drawingPad, "No action to be redone.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return geomShapes;
		}
		int idx = changesAtThisPoint[++historyIdx];
		prev.get(idx).add(next.get(idx).pop());
		return updateGeomShapes(geomShapes);
	}

	public boolean isShapeVisible(int index) {
		return isVisible[index];
	}
}
