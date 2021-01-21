package org.deltaecore.feature.graphical.editor.commands;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.eclipse.gef.commands.Command;

public abstract class DEGroupAbstractCreateDeleteCommand extends Command {
	private DEGroup group;
	private DEFeature parentFeature;
	private int indexInFeature;
	
	public DEGroupAbstractCreateDeleteCommand(DEGroup group, DEFeature parentFeature) {
		this.group = group;
		this.parentFeature = parentFeature;
		
		indexInFeature = -1;
	}
	
	protected void addToParent() {
		List<DEGroup> parentGroups = parentFeature.getGroups();

		if (indexInFeature == -1) {
			parentGroups.add(group);
		} else {
			parentGroups.add(indexInFeature, group);
		}
	}
	
	protected void removeFromParent() {
		DEFeature parentFeature = group.getParentOfGroup();
		
		if (parentFeature != null) {
			List<DEGroup> groups = parentFeature.getGroups();
			indexInFeature = groups.indexOf(group);
			groups.remove(group);
		}
	}
}
