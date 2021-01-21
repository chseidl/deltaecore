package eu.vicci.ecosystem.sft.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class SFTConnectionFigure extends SFTAbstractFigure {

	private Polyline p;

	public SFTConnectionFigure(String gate, String fault) {
		super();

		setLayoutManager(new XYLayout());

		p = new Polyline();
		Point start = abegoLayouter.getCenterOfPart(gate);
		Point end = abegoLayouter.getCenterOfPart(fault);
		p.setEndpoints(start, end);
		
		add(p);

		setLabel();
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();

		setConstraint(p, new Rectangle(0, 0, r.width, r.height));

		label.invalidate();
		p.invalidate();
	}

	public Polyline getPolyline() {
		return p;
	}
}
