package geometricShapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import colorManagement.ColorBundle;

public class Ellipse extends CustomShape {

	private int a;
	private int b;
	private Point ellipseCntr;

	public Ellipse(Point cntrPnt, Point refPnt, ColorBundle colorBundle) {
		setColorBundle(colorBundle);
		this.cntrPnt = new Point(cntrPnt);
		this.refPnt = new Point(refPnt);
	}

	private void calculateDim() {
		a = Math.abs(refPnt.x - cntrPnt.x);
		b = Math.abs(refPnt.y - cntrPnt.y);
		ellipseCntr = new Point();
		ellipseCntr.setLocation((cntrPnt.x + refPnt.x) / 2, (cntrPnt.y + refPnt.y) / 2);
		
	}

	@Override
	public boolean isInShape(Point start) {
		if (start.x < ellipseCntr.x - (a / 2.0) || start.x > ellipseCntr.x + (a / 2.0))
			return false;
		int equ = (int) ((b / 2.0) * Math
				.sqrt(1 - (double) (start.x - ellipseCntr.x) * (start.x - ellipseCntr.x) / (a / 2.0) / (a / 2.0)));
		int up = ellipseCntr.y + equ;
		int down = ellipseCntr.y - equ;
		if (start.y >= down && start.y <= up)
			return true;
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		calculateDim();
		int g1 = (refPnt.x < cntrPnt.x ? -1 : 0);
		int g2 = (refPnt.y < cntrPnt.y ? -1 : 0);
		Graphics2D sg = (Graphics2D) g.create();
		if (colorBundle.getHasFill()) {
			sg.setColor(colorBundle.getFillCol());
			sg.fillOval(cntrPnt.x + g1 * a, cntrPnt.y + g2 * b, a, b);
		}
		if (!isSelected)
			sg.setStroke(new BasicStroke(colorBundle.getStrokeThickness()));
		sg.setColor(colorBundle.getStrokeCol());
		sg.drawOval(cntrPnt.x + g1 * a, cntrPnt.y + g2 * b, a, b);
		sg.dispose();
	}

}
