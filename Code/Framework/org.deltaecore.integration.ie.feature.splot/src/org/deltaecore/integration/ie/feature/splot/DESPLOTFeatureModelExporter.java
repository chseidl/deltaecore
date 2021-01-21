package org.deltaecore.integration.ie.feature.splot;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.util.DEExpressionToCNFConverter;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.deltaecore.integration.ie.feature.DEFeatureModelExporter;
import org.deltaecore.integration.ie.feature.splot.data.DESPLOTFeatureModelImportExportData;
import org.deltaecore.integration.ie.feature.splot.util.DEExpressionToPropositionalFormulaConverter;

import constraints.PropositionalFormula;
import fm.FeatureGroup;
import fm.FeatureModel;
import fm.FeatureModelException;
import fm.FeatureTreeNode;
import fm.NodeRenderer;
import fm.RootNode;
import fm.SolitaireFeature;

public class DESPLOTFeatureModelExporter extends DEFeatureModelExporter<DESPLOTFeatureModelImportExportData> {
	//NOTE: No idea why you would need a NodeRenderer but this is probably due to the
	//questionable integration of data model with UI code in the SPLOT feature model.
	private static final NodeRenderer nodeRenderer = null;
	
	private Map<DEFeature, String> deFeatureToIdMapping;
	
	public DESPLOTFeatureModelExporter() {
		deFeatureToIdMapping = new HashMap<DEFeature, String>();
	}
	
	@Override
	protected void reset() {
		super.reset();
		
		deFeatureToIdMapping.clear();
	}
	
	@Override
	protected DESPLOTFeatureModelImportExportData doExportFeatureModel(DEFeatureModel deFeatureModel, DEConstraintModel deConstraintModel) {
		//Create a name for the feature model.
		String featureModelName = "";
		
		try {
			String rawName = URLDecoder.decode(deFeatureModel.eResource().getURI().lastSegment(), "UTF-8");
			featureModelName += "Export of " + rawName;
		} catch (Exception e) {
		}
		
		FeatureModel featureModel = createFeatureModel(deFeatureModel, featureModelName);
		setMetaData(featureModel, featureModelName);
		convertConstraints(deConstraintModel, featureModel);
		
		return new DESPLOTFeatureModelImportExportData(featureModel);
	}

	private FeatureModel createFeatureModel(DEFeatureModel deFeatureModel, String featureModelName) {
		//Questionable way of creating a feature model but this is by design by the SPLOT creators.
		FeatureModel featureModel = new FeatureModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected FeatureTreeNode createNodes() throws FeatureModelException {
				DEFeature deRootFeature = deFeatureModel.getRootFeature();
				return convertRootFeature(deRootFeature);
			}

			@Override
			protected void saveNodes() {
			}
		};
		
		try {
			//This triggers the feature model to invoke createNodes().
			featureModel.loadModel();
			featureModel.setName(featureModelName);
		} catch (FeatureModelException e) {
			e.printStackTrace();
			return null;
		}
		
		return featureModel;
	}

	private void setMetaData(FeatureModel featureModel, String featureModelName) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		String formattedNow = dateFormat.format(now);
		
		featureModel.addMetaData("description", "Export of feature model \"" + featureModelName + "\" from DeltaEcore.");
		featureModel.addMetaData("creator", "DeltaEcore SPLOT Feature Model Exporter");
		featureModel.addMetaData("date", formattedNow);
		featureModel.addMetaData("organization", "DeltaEcore");
		featureModel.addMetaData("website", "http://deltaecore.org");
		featureModel.addMetaData("reference", "http://deltaecore.org");
	}
	
	private void convertConstraints(DEConstraintModel deConstraintModel, FeatureModel featureModel) {
		DEExpressionToPropositionalFormulaConverter converter = new DEExpressionToPropositionalFormulaConverter();
		
		if (deConstraintModel != null) {
			List<DEConstraint> deConstraints = deConstraintModel.getConstraints();
			
			for (DEConstraint deConstraint : deConstraints) {
				DEExpression deRootExpression = deConstraint.getRootExpression();
				
				//Convert expression to constraint in CNF
				DEExpression deRootExpressionInCNF = DEExpressionToCNFConverter.convertToCNF(deRootExpression);
				PropositionalFormula constraint = converter.convertToPropositionalFormula(deRootExpressionInCNF, "constraint_", deFeatureToIdMapping);
				featureModel.addConstraint(constraint);
			}
		}
	}
	
	protected RootNode convertRootFeature(DEFeature deRootFeature) {
		String name = deRootFeature.getName();
		String id = createFeatureId(deRootFeature);
		deFeatureToIdMapping.put(deRootFeature, id);
		
		RootNode rootNode = new RootNode(id, name, nodeRenderer);

		doConvertGroupsOfFeature(deRootFeature, rootNode);
		
		return rootNode;
	}
	
	protected void convertSolitaireFeature(DEFeature deFeature, FeatureTreeNode parentNode) {
		String name = deFeature.getName();
		String id = createFeatureId(deFeature);
		deFeatureToIdMapping.put(deFeature, id);
		
		boolean isOptional = DEFeatureUtil.isOptional(deFeature);
		
		SolitaireFeature solitaireFeature = new SolitaireFeature(isOptional, id, name, nodeRenderer);
		parentNode.add(solitaireFeature);
		
		doConvertGroupsOfFeature(deFeature, solitaireFeature);
	}
	
	protected void convertGroupedFeature(DEFeature deFeature, FeatureTreeNode parentNode) {
		String name = deFeature.getName();
		String id = createFeatureId(deFeature);
		deFeatureToIdMapping.put(deFeature, id);
		
		FeatureTreeNode groupedFeature = new FeatureTreeNode(id, name, nodeRenderer);
		groupedFeature.setName(name);
		
		parentNode.add(groupedFeature);
		
		doConvertGroupsOfFeature(deFeature, groupedFeature);
	}
	
	protected void doConvertGroupsOfFeature(DEFeature deFeature, FeatureTreeNode parentNode) {
		List<DEGroup> deGroups = deFeature.getGroups();
		
		for (DEGroup deGroup : deGroups) {
			convertGroup(deGroup, parentNode);
		}
	}
	
	protected void convertGroup(DEGroup deGroup, FeatureTreeNode parentNode) {
		//Recursive call
		List<DEFeature> deFeatures = deGroup.getFeatures();
		
		boolean isSolitaire = deFeatures.size() == 1;
	
		if (isSolitaire) {
			//Convert to solitaire feature
			DEFeature deFeature = deFeatures.get(0);
			convertSolitaireFeature(deFeature, parentNode);
		} else {
			//Create group
			int minCardinality = deGroup.getMinCardinality();
			int maxCardinality = deGroup.getMaxCardinality();
			FeatureGroup featureGroup = new FeatureGroup("", "", minCardinality, maxCardinality, nodeRenderer);
			
			parentNode.add(featureGroup);
			
			//Convert all children to grouped features
			for (DEFeature deFeature : deFeatures) {
				convertGroupedFeature(deFeature, featureGroup);
			}
		}
	}
	
	protected String createFeatureId(DEFeature deFeature) {
		//For possible future extensions or overrides.
		return deFeature.getName();
	}
}
