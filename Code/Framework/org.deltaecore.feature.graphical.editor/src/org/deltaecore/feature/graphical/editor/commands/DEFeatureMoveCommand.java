package org.deltaecore.feature.graphical.editor.commands;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.eclipse.gef.commands.Command;

public class DEFeatureMoveCommand extends Command {
	private DEFeatureDeleteCommand deleteCommand;
	private DEFeatureCreateCommand createCommand;
	
	public DEFeatureMoveCommand(DEFeature feature, DEFeature dropFeature, boolean droppedBefore) {
		if (droppedBefore) {
			//Dropped before the feature
			DEGroup group = dropFeature.getParentOfFeature();
			
			deleteCommand = new DEFeatureDeleteCommand(feature);
			createCommand = new DEFeatureCreateCommand(feature, group, dropFeature);
		} else {
			//Dropped on the feature
			deleteCommand = new DEFeatureDeleteCommand(feature);
			createCommand = new DEFeatureCreateCommand(feature, dropFeature);
		}
	}
	
	@Override
	public void execute() {
		deleteCommand.execute();
		createCommand.execute();
	}

	@Override
	public void undo() {
		createCommand.undo();
		deleteCommand.undo();
	}
}
