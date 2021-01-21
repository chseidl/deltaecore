package org.deltaecore.feature.graphical.editor.commands;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureFactory;
import org.deltaecore.feature.DEGroup;

public class DEFeatureCreateCommand extends DEFeatureAbstractCreateDeleteCommand {
	private boolean forceCreateParentGroup;
	private boolean hadCreatedParentGroup;
//	private int indexInGroup;
	private DEFeature addBeforeFeature;
	
	public DEFeatureCreateCommand(DEFeature feature, Object parentObject) {
		this(feature, parentObject, false);
	}
	
	public DEFeatureCreateCommand(DEFeature feature, DEGroup parentObject, DEFeature addBeforeFeature) {
		super(feature, parentObject);
		
		initialize(addBeforeFeature, false);
	}
	
	public DEFeatureCreateCommand(DEFeature feature, Object parentObject, boolean forceCreateParentGroup) {
		super(feature, parentObject);
		
		initialize(null, forceCreateParentGroup);
	}
	
	private void initialize(DEFeature addBeforeFeature, boolean forceCreateParentGroup) {
		this.addBeforeFeature = addBeforeFeature;
		this.forceCreateParentGroup = forceCreateParentGroup;
		
		hadCreatedParentGroup = false;
	}
	
	@Override
	public void execute() {
		addToParent();
	}

	@Override
	public void undo() {
		removeFromParent();
	}
	
	@Override
	protected void addToFeature(DEFeature parentFeature) {
		List<DEGroup> groups = parentFeature.getGroups();

		if (groups.isEmpty()) {
			doAddToGroup(null, parentFeature);
		} else {
			super.addToFeature(parentFeature);
		}
	}
	
	@Override
	protected void addToGroup(DEGroup parentGroup) {
		increaseGroupCardinalityIfAppropriate(parentGroup);
		
		DEFeature parentFeature = parentGroup.getParentOfGroup();
		doAddToGroup(parentGroup, parentFeature);
	}
	
	protected void doAddToGroup(DEGroup group, DEFeature parentFeatureIfNeeded) {
		DEGroup groupToAddTo = group;
		
		if (group == null || forceCreateParentGroup) {
			groupToAddTo = createGroup();
		}
		
		DEFeature feature = getFeature();
		List<DEFeature> features = groupToAddTo.getFeatures();
		
		if (addBeforeFeature != null) {
			int indexInGroup = features.indexOf(addBeforeFeature);
			features.add(indexInGroup, feature);
		} else {
			features.add(feature);
		}
		
		//Add group only now to have the right order of updates on the visuals.
		//Otherwise, the group will be created without any features in it and
		//the respective incorrect visual representation.
		if (hadCreatedParentGroup) {
			List<DEGroup> groups = parentFeatureIfNeeded.getGroups();
			groups.add(groupToAddTo);
		}
	}
	
	private DEGroup createGroup() {
		DEGroup group = DEFeatureFactory.eINSTANCE.createDEGroup();
		group.setMinCardinality(0);
		group.setMaxCardinality(1);
		
		hadCreatedParentGroup = true;
		
		return group;
	}
	
	@Override
	protected void removeFromParentGroup(DEGroup parentGroup) {
		super.removeFromParentGroup(parentGroup);
		
		if (hadCreatedParentGroup) {
			DEFeature parentOfGroup = parentGroup.getParentOfGroup();
			
			if (parentOfGroup != null) {
				List<DEGroup> groups = parentOfGroup.getGroups();
				groups.remove(parentGroup);
			}
			
			hadCreatedParentGroup = false;
		}
	}
}
