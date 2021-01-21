package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory;

import java.security.InvalidParameterException;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.figures.GSNElementFigure;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes.ShadedEllipseShape;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes.ShadedRectangleShape;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes.ShadedRoundedRectangleShape;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes.ShadedTrapezoidShape;

public class GSNFigureFactory {
	public static GSNElementFigure createFigure(GSNElementType type) {
		Shape mainShape = createMainShape(type);
		return new GSNElementFigure(mainShape);
	}
	
	public static Shape createMainShape(GSNElementType type) {
		switch (type.getValue()) {
			case GSNElementType.CONTEXT_VALUE:
				ShadedRoundedRectangleShape roundedRectangle = new ShadedRoundedRectangleShape();
				roundedRectangle.setCornerDimensions(new Dimension(15, 15));
				return roundedRectangle;
			case GSNElementType.GOAL_VALUE:
				return new ShadedRectangleShape();
			case GSNElementType.SOLUTION_VALUE:
				//TODO: Actually circle, enforce via edit part?!
				return new ShadedEllipseShape();
			case GSNElementType.STRATEGY_VALUE:
				return new ShadedTrapezoidShape();
			case GSNElementType.ASSUMPTION_VALUE:
				return new ShadedEllipseShape();
			case GSNElementType.JUSTIFICATION_VALUE:
				return new ShadedEllipseShape();
		}
		
		throw new InvalidParameterException();
	}
	
	public static ConnectionAnchor createConnectionAnchor(GSNElementType type, IFigure figure) {
		switch (type.getValue()) {
			case GSNElementType.CONTEXT_VALUE:
				//TODO: rounded rectangle
				return new ChopboxAnchor(figure);
			case GSNElementType.GOAL_VALUE:
				return new ChopboxAnchor(figure);
			case GSNElementType.SOLUTION_VALUE:
				//TODO: Actually circle, enforced via figure or edit part - anchor should be ok
				return new EllipseAnchor(figure);
			case GSNElementType.STRATEGY_VALUE:
				//TODO: Trapezoid
				return new ChopboxAnchor(figure);
			case GSNElementType.ASSUMPTION_VALUE:
				return new EllipseAnchor(figure);
			case GSNElementType.JUSTIFICATION_VALUE:
				return new EllipseAnchor(figure);
		}
		
		throw new InvalidParameterException();
	}
}
