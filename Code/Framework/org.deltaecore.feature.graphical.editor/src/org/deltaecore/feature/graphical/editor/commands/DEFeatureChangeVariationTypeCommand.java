package org.deltaecore.feature.graphical.editor.commands;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.gef.commands.Command;

public class DEFeatureChangeVariationTypeCommand extends Command {
	public static enum VariationType {
		MANDATORY,
		OPTIONAL
	};
	
	private DEFeature feature;
	private VariationType variationType;
	
	private int oldMinCardinality;
	private int oldMaxCardinality;
	
	private int oldGroupMinCardinality;
	private int oldGroupMaxCardinality;
	
	public DEFeatureChangeVariationTypeCommand(DEFeature feature, VariationType variationType) {
		this.feature = feature;
		this.variationType = variationType;
	}
	
	@Override
	public void execute() {
		oldMinCardinality = feature.getMinCardinality();
		oldMaxCardinality = feature.getMaxCardinality();
		
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup != null) {
			oldGroupMinCardinality = parentGroup.getMinCardinality();
			oldGroupMaxCardinality = parentGroup.getMaxCardinality();
		}
		
		switch(variationType) {
			case MANDATORY:
				DEFeatureUtil.makeMandatory(feature);
				break;
			case OPTIONAL:
				DEFeatureUtil.makeOptional(feature);
				break;
		}
	}

	@Override
	public void undo() {
		feature.setMinCardinality(oldMinCardinality);
		feature.setMaxCardinality(oldMaxCardinality);
		
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup != null) {
			parentGroup.setMinCardinality(oldGroupMinCardinality);
			parentGroup.setMaxCardinality(oldGroupMaxCardinality);
		}
	}
}
