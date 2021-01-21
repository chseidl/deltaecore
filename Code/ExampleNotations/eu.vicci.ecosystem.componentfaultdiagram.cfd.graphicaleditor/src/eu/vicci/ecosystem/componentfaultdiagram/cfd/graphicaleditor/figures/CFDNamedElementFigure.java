package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDNamedElement;

public abstract class CFDNamedElementFigure extends CFDElementFigure {
	private Label nameLabel;
	
	public CFDNamedElementFigure() {
	}
	
	protected void initialize() {
		super.initialize();
		nameLabel = createNameLabel();
	}
	
	public DirectEditManager createDirectEditManager(GraphicalEditPart source) {
		CellEditorLocator locator = new CellEditorLocator() {
			@Override
			public void relocate(CellEditor celleditor) {
				Text text = (Text) celleditor.getControl();

				Rectangle figureBounds = getBounds();
				translateToAbsolute(figureBounds);
				
				//Make text at least as big as the underlying label.
				Point preferredTextDimension = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);

				Rectangle labelBounds = nameLabel.getTextBounds().getCopy();
				nameLabel.translateToAbsolute(labelBounds);
				
				//Compensate for space needed by cursor etc. when calculating label size
				int width = Math.max(labelBounds.width, preferredTextDimension.x + 15) + 1;
//				int width = Math.max(figureBounds.width - 2, preferredTextDimension.x) + 1;
				int height = Math.max(labelBounds.height, preferredTextDimension.y) + 1;
				
				//Center text horizontally over figure.
				int x = figureBounds.x + (figureBounds.width - width) / 2;
				int y = labelBounds.y - 1;
				
				text.setBounds(x, y, width, height);
			}
		};
		
		DirectEditManager directEditManager = new DirectEditManager(source, TextCellEditor.class, locator) {
			@Override
			protected void initCellEditor() {
				String initialLabelText = nameLabel.getText();
				getCellEditor().setValue(initialLabelText);
			}
		};
		
		return directEditManager;
	}

	protected Label createNameLabel() {
		return new Label();
	}
	
	protected Label getNameLabel() {
		return nameLabel;
	}
	
	@Override
	public void doUpdate(CFDElement element) {
		super.doUpdate(element);
		
		CFDNamedElement namedElement = (CFDNamedElement) element;
		
		String name = namedElement.getName();
		nameLabel.setText(name);
	}
}
