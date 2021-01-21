package org.deltaecore.feature.graphical.editor.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.gef.commands.Command;

public class DEGroupChangeVariationTypeCommand extends Command {
	public static enum VariationType {
		AND,
		OR,
		ALTERNATIVE
	};
	
	private DEGroup group;
	private VariationType variationType;
	
	private int oldMinCardinality;
	private int oldMaxCardinality;
	
	private Map<DEFeature, Integer> oldFeatureMinCardinalities;
	private Map<DEFeature, Integer> oldFeatureMaxCardinalities;
	
	public DEGroupChangeVariationTypeCommand(DEGroup group, VariationType variationType) {
		this.group = group;
		this.variationType = variationType;
		
		oldFeatureMinCardinalities = new HashMap<DEFeature, Integer>();
		oldFeatureMaxCardinalities = new HashMap<DEFeature, Integer>();
	}
	
	@Override
	public void execute() {
		oldMinCardinality = group.getMinCardinality();
		oldMaxCardinality = group.getMaxCardinality();
		
		storeFeatureCardinalities();
		
		switch(variationType) {
			case AND:
				DEFeatureUtil.makeAnd(group);
				break;
			case OR:
				DEFeatureUtil.makeOr(group);
				break;
			case ALTERNATIVE:
				DEFeatureUtil.makeAlternative(group);
				break;
		}
	}

	@Override
	public void undo() {
		group.setMinCardinality(oldMinCardinality);
		group.setMaxCardinality(oldMaxCardinality);
		
		restoreFeatureCardinalities();
	}
	
	protected void storeFeatureCardinalities() {
		oldFeatureMinCardinalities.clear();
		oldFeatureMaxCardinalities.clear();
		
		List<DEFeature> features = group.getFeatures();
		
		for (DEFeature feature : features) {
			oldFeatureMinCardinalities.put(feature, feature.getMinCardinality());
			oldFeatureMaxCardinalities.put(feature, feature.getMaxCardinality());
		}
	}
	
	protected void restoreFeatureCardinalities() {
		List<DEFeature> features = group.getFeatures();
		
		for (DEFeature feature : features) {
			int minCardinality = oldFeatureMinCardinalities.get(feature);
			int maxCardinality = oldFeatureMaxCardinalities.get(feature);
			
			feature.setMinCardinality(minCardinality);
			feature.setMaxCardinality(maxCardinality);
		}
	}
}
