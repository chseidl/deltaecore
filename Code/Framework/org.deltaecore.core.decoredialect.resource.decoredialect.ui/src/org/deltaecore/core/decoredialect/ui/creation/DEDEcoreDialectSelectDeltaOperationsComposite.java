package org.deltaecore.core.decoredialect.ui.creation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.christophseidl.util.swt.SWTFactory;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DEDEcoreDialectSelectDeltaOperationsComposite extends Composite {
	private Button deselectAllDeltaOperationDefinitionsButton;
	private Button selectAllDeltaOperationDefinitionsButton;
	private Composite verticalButtonsComposite;
	private Composite deltaOperationDefinitionSelectionComposite;
	private ScrolledComposite deltaOperationDefinitionSelectionScrolledComposite;
	private Group initializeDeltaDialectGroup;

	@Override
	protected void checkSubclass() {
		// Overriding checkSubclass allows this class to extend Composite
	}

	public DEDEcoreDialectSelectDeltaOperationsComposite(Composite parent, int style) {
		super(parent, style);

		initGUI();
	}
	
	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			setLayout(thisLayout);
			this.setSize(607, 300);

			{
				initializeDeltaDialectGroup = new Group(this, SWT.NONE);
				GridLayout initializeDeltaDialectGroupLayout = new GridLayout();
				initializeDeltaDialectGroupLayout.numColumns = 2;
				initializeDeltaDialectGroup.setLayout(initializeDeltaDialectGroupLayout);
				GridData initializeDeltaDialectGroupLData = new GridData();
				initializeDeltaDialectGroupLData.horizontalAlignment = GridData.FILL;
				initializeDeltaDialectGroupLData.grabExcessHorizontalSpace = true;
				initializeDeltaDialectGroupLData.verticalAlignment = GridData.FILL;
				initializeDeltaDialectGroupLData.grabExcessVerticalSpace = true;
				initializeDeltaDialectGroup.setLayoutData(initializeDeltaDialectGroupLData);
				initializeDeltaDialectGroup.setText("Select Delta Operations");
				{
					deltaOperationDefinitionSelectionScrolledComposite = new ScrolledComposite(initializeDeltaDialectGroup, SWT.H_SCROLL | SWT.V_SCROLL);
					GridData advicesScrolledCompositeLData = new GridData();
					advicesScrolledCompositeLData.horizontalAlignment = GridData.FILL;
					advicesScrolledCompositeLData.verticalAlignment = GridData.FILL;
					advicesScrolledCompositeLData.grabExcessVerticalSpace = true;
					advicesScrolledCompositeLData.grabExcessHorizontalSpace = true;
					deltaOperationDefinitionSelectionScrolledComposite.setLayoutData(advicesScrolledCompositeLData);
					
					deltaOperationDefinitionSelectionScrolledComposite.setExpandHorizontal(true);
					deltaOperationDefinitionSelectionScrolledComposite.setExpandVertical(true);
					
					{
						deltaOperationDefinitionSelectionComposite = new Composite(deltaOperationDefinitionSelectionScrolledComposite, SWT.NONE);
						deltaOperationDefinitionSelectionScrolledComposite.setContent(deltaOperationDefinitionSelectionComposite);
						GridLayout advicesCompositeLayout = new GridLayout();
						advicesCompositeLayout.numColumns = 5;
						deltaOperationDefinitionSelectionComposite.setLayout(advicesCompositeLayout);
						deltaOperationDefinitionSelectionComposite.setBounds(0, 2, 10, 10);
					}
				}
				{
					verticalButtonsComposite = new Composite(initializeDeltaDialectGroup, SWT.NONE);
					GridLayout verticalButtonsCompositeLayout = new GridLayout();
					verticalButtonsCompositeLayout.makeColumnsEqualWidth = true;
					GridData verticalButtonsCompositeLData = new GridData();
					verticalButtonsCompositeLData.horizontalAlignment = GridData.FILL;
					verticalButtonsCompositeLData.verticalAlignment = GridData.FILL;
					verticalButtonsComposite.setLayoutData(verticalButtonsCompositeLData);
					verticalButtonsComposite.setLayout(verticalButtonsCompositeLayout);
					{
						selectAllDeltaOperationDefinitionsButton = SWTFactory.createDefaultButton(verticalButtonsComposite, "Select All", 85);
					}
					{
						deselectAllDeltaOperationDefinitionsButton = SWTFactory.createDefaultButton(verticalButtonsComposite, "Deselect All", 85);
					}
					{
						SWTFactory.createVerticalDummyComposite(verticalButtonsComposite);
					}
				}
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Button getDeselectAllDeltaOperationDefinitionsButton() {
		return deselectAllDeltaOperationDefinitionsButton;
	}

	public Button getSelectAllDeltaOperationDefinitionsButton() {
		return selectAllDeltaOperationDefinitionsButton;
	}

	public Composite getDeltaOperationDefinitionSelectionComposite() {
		return deltaOperationDefinitionSelectionComposite;
	}

	public Group getInitializeDeltaDialectGroup() {
		return initializeDeltaDialectGroup;
	}

	public ScrolledComposite getDeltaOperationDefinitionSelectionScrolledComposite() {
		return deltaOperationDefinitionSelectionScrolledComposite;
	}
}
