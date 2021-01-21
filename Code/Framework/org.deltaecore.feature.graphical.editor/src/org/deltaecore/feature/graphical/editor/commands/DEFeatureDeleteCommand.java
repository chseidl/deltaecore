package org.deltaecore.feature.graphical.editor.commands;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;

public class DEFeatureDeleteCommand extends DEFeatureAbstractCreateDeleteCommand {
	private int indexInGroup;
	private DEGroupDeleteCommand deleteCommand;
	
	public DEFeatureDeleteCommand(DEFeature feature) {
		super(feature, feature.eContainer());
	}
	
	@Override
	public void execute() {
		removeFromParent();		
	}

	@Override
	public void undo() {
		addToParent();
	}
	
	@Override
	protected void addToGroup(DEGroup parentGroup) {
		if (deleteCommand != null) {
			deleteCommand.undo();
		}
		
		increaseGroupCardinalityIfAppropriate(parentGroup);
		
		DEFeature feature = getFeature();
		List<DEFeature> features = parentGroup.getFeatures();
		features.add(indexInGroup, feature);
	}
	
	@Override
	protected void removeFromParentGroup(DEGroup parentGroup) {
		DEFeature feature = getFeature();
		List<DEFeature> features = parentGroup.getFeatures();
		
		indexInGroup = features.indexOf(feature);
		
		super.removeFromParentGroup(parentGroup);
		
		if (features.isEmpty()) {
			deleteCommand = new DEGroupDeleteCommand(parentGroup);
			deleteCommand.execute();
		}
	}
}
