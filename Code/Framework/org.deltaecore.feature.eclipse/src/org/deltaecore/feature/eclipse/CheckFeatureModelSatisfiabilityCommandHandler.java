package org.deltaecore.feature.eclipse;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.analysis.FeatureModelSatisfiabilityChecker;
import org.deltaecore.feature.util.DEFeatureIOUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;

import de.christophseidl.util.eclipse.ui.JFaceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;

public class CheckFeatureModelSatisfiabilityCommandHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile featureModelFile = DEFeatureIOUtil.getFirstActiveFeatureModelFile();
		
		if (featureModelFile == null || !featureModelFile.exists()) {
			JFaceUtil.alertError("The feature model does not exist.");
			return null;
		}
		
		DEFeatureModel featureModel = EcoreIOUtil.loadModel(featureModelFile);
		checkFeatureModelSatisfiability(featureModel);
		
		return null;
	}
	
	public static void checkFeatureModelSatisfiability(DEFeatureModel featureModel) {
		try {
			if (featureModel == null) {
				JFaceUtil.alertError("The feature model could not be loaded.");
				return;
			}
	
			FeatureModelSatisfiabilityChecker satisfiabilityChecker = new FeatureModelSatisfiabilityChecker();
			boolean satisfiable = satisfiabilityChecker.checkSatisfiability(featureModel);
			
			if (satisfiable) {
				JFaceUtil.alertInformation("The feature model is satisfiable.");
			} else {
				JFaceUtil.alertError("The feature model is NOT satisfiable.");	
			}
		} catch(Exception e) {
			JFaceUtil.alertError("An error occured: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
