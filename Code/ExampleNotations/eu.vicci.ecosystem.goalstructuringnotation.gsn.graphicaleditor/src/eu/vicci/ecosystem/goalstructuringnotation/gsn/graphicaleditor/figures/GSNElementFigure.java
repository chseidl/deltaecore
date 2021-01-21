package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.figures;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory.GSNFigureFactory;

public class GSNElementFigure extends Figure {
	private Label idLabel;
//	private Font idLabelFont;
	
	private GSNElementType currentElementType = null;
	
	private Shape mainShape;
	private FlowPage descriptionFlowPage;
	private TextFlow descriptionTextFlow;
	
	private ConnectionAnchor connectionAnchor;
	
	public GSNElementFigure(Shape mainShape) {
		this.mainShape = mainShape;
		assembleUI();
	}

	private void assembleUI() {
		//Assemble UI.
		idLabel = new Label();
		
//		Font font = idLabel.getFont();
//		idLabelFont = deriveBoldFont(font);
//		idLabel.setFont(idLabelFont);
		
		descriptionFlowPage = new FlowPage();
		descriptionTextFlow = new TextFlow();
		descriptionFlowPage.add(descriptionTextFlow);
		descriptionFlowPage.setHorizontalAligment(PositionConstants.CENTER);

		setLayoutManager(new BorderLayout());
		initializeMainShape();
	}

	private void initializeMainShape() {
		mainShape.setAntialias(SWT.ON);
		mainShape.setLayoutManager(new GridLayout());
		mainShape.add(idLabel, new GridData(GridData.FILL_HORIZONTAL));
		mainShape.add(descriptionFlowPage, new GridData(GridData.FILL_BOTH));
		
		add(mainShape, BorderLayout.CENTER);
	}
	
//	public void dispose() {
//		idLabelFont.dispose();
//		idLabelFont = null;
//	}
//	
//	private Font deriveBoldFont(Font font) {
//		Display display = Display.getCurrent();
//		FontData[] fontData = font.getFontData();
//		for (int i = 0; i < fontData.length; i++) {
//			fontData[i].setStyle(SWT.BOLD);
//		}
//		return new Font(display, fontData);
//	}
	
	public void update(GSNConcreteElement element) {
		idLabel.setText(element.getId());
		descriptionTextFlow.setText(element.getDescription());
		
		//Exchange main shape if necessary.
		GSNElementType type = element.getType();
		
		if (currentElementType == null || !currentElementType.equals(type)) {
			Shape newMainShape = GSNFigureFactory.createMainShape(type);
			exchangeMainShape(newMainShape);
			updateConnectionAnchor(type);
			currentElementType = type;
		}
	}
	
	private void exchangeMainShape(Shape newMainShape) {
		mainShape.removeAll();
		remove(mainShape);
		
		mainShape = newMainShape;
		initializeMainShape();
	}
	
	private void updateConnectionAnchor(GSNElementType type) {
		connectionAnchor = GSNFigureFactory.createConnectionAnchor(type, this);
	}
	
	public ConnectionAnchor getConnectionAnchor() {
		if (connectionAnchor == null) {
			updateConnectionAnchor(currentElementType);
		}
		
		return connectionAnchor;
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

				Rectangle labelBounds = descriptionTextFlow.getBounds();
//				Rectangle labelBounds = descriptionTextFlow.getTextBounds().getCopy();
				descriptionTextFlow.translateToAbsolute(labelBounds);
				
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
				String initialLabelText = descriptionTextFlow.getText();
				getCellEditor().setValue(initialLabelText);
			}
		};
		
		return directEditManager;
	}
}
