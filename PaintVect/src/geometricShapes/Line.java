package geometricShapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import colorManagement.ColorBundle;

public class Line extends CustomShape {

	public Line(Point start, Point end, ColorBundle colorBundle) {
		setColorBundle(colorBundle);
		this.cntrPnt = new Point(start);
		this.refPnt = new Point(end);
	}

	public boolean isInShape(Point start) {
		int xDif = (int) (refPnt.getX() - cntrPnt.getX());
		int yDif = (int) (refPnt.getY() - cntrPnt.getY());
		if (pointInBetween((int) cntrPnt.getX(), (int) refPnt.getX(), (int) start.getX())) {
			if (Math.abs(xDif) < 10)
				return pointInBetween((int) cntrPnt.getY(), (int) refPnt.getY(), (int) start.getY());
			int curXDif = (int) (start.getX() - cntrPnt.getX());
			int virtY = (int) (cntrPnt.getY() + (curXDif * yDif) / xDif);
			if (Math.abs(start.getY() - virtY) < 10)
				return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		Graphics2D sg = (Graphics2D) g.create();
		if (!isSelected)
			sg.setStroke(new BasicStroke(colorBundle.getStrokeThickness()));
		sg.setColor(colorBundle.getStrokeCol());
		sg.drawLine((int) cntrPnt.getX(), (int) cntrPnt.getY(), refPnt.x, refPnt.y);
		sg.dispose();
	}

}
