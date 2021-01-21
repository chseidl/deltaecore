package org.deltaecore.feature.graphical.editor.commands;

import java.security.InvalidParameterException;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.eclipse.gef.commands.Command;

public abstract class DEFeatureAbstractCreateDeleteCommand extends Command {
	private DEFeature feature;
	private Object parentObject;
	
	public DEFeatureAbstractCreateDeleteCommand(DEFeature feature, Object parentObject) {
		this.feature = feature;
		this.parentObject = parentObject;
	}
	
	protected void addToParent() {
		if (parentObject instanceof DEFeatureModel) {
			DEFeatureModel parentFeatureModel = (DEFeatureModel) parentObject;
			addToFeatureModel(parentFeatureModel);
		}
		
		if (parentObject instanceof DEFeature) {
			DEFeature parentFeature = (DEFeature) parentObject;
			addToFeature(parentFeature);
		}
		
		if (parentObject instanceof DEGroup) {
			DEGroup parentGroup = (DEGroup) parentObject;
			addToGroup(parentGroup);
		}
		
		if (parentObject instanceof DEVersion) {
			DEVersion parentVersion = (DEVersion) parentObject;
			addToVersion(parentVersion);
		}
	}

	protected void addToFeatureModel(DEFeatureModel parentFeatureModel) {
		DEFeature rootFeature = parentFeatureModel.getRootFeature();
		
		if (rootFeature == null) {
			parentFeatureModel.setRootFeature(feature);
		} else {
			addToFeature(rootFeature);
		}
	}
	
	protected void addToFeature(DEFeature parentFeature) {
		List<DEGroup> groups = parentFeature.getGroups();
		
		if (groups.isEmpty()) {
			throw new InvalidParameterException();
		}
		
		//Add to last group
		DEGroup parentGroup = groups.get(groups.size() - 1);
		addToGroup(parentGroup);
	}
	
	protected abstract void addToGroup(DEGroup parentGroup);
	
	protected void addToVersion(DEVersion parentVersion) {
		DEFeature parentFeature = parentVersion.getFeature();
		addToFeature(parentFeature);
	}
	
	protected void removeFromParent() {
		Object currentParentObject = feature.eContainer();
		
		if (currentParentObject instanceof DEFeatureModel) {
			DEFeatureModel parentFeatureModel = (DEFeatureModel) currentParentObject;
			removeFromFeatureModel(parentFeatureModel);
		}
		
		if (currentParentObject instanceof DEGroup) {
			DEGroup parentGroup = (DEGroup) currentParentObject;
			removeFromParentGroup(parentGroup);
		}
	}

	protected void removeFromFeatureModel(DEFeatureModel parentFeatureModel) {
		parentFeatureModel.setRootFeature(null);
	}

	protected void removeFromParentGroup(DEGroup parentGroup) {
		decreaseGroupCardinalityIfAppropriate(parentGroup);
		
		List<DEFeature> features = parentGroup.getFeatures();
		features.remove(feature);
	}

	
	protected void increaseGroupCardinalityIfAppropriate(DEGroup parentGroup) {
		if (parentGroup == null) {
			return;
		}
		
		if (parentGroup.isAnd() || parentGroup.isOr()) {
			int maxCardinality = parentGroup.getMaxCardinality(); 
			parentGroup.setMaxCardinality(maxCardinality + 1);
		}
	}
	
	protected void decreaseGroupCardinalityIfAppropriate(DEGroup parentGroup) {
		if (parentGroup.isAnd() || parentGroup.isOr()) {
			int maxCardinality = parentGroup.getMaxCardinality(); 
			parentGroup.setMaxCardinality(maxCardinality - 1);
		}
	}
	
	
	protected DEFeature getFeature() {
		return feature;
	}

	protected Object getParentObject() {
		return parentObject;
	}
}
