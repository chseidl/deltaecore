package org.deltaecore.integration.ie.feature.featureide;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintFactory;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEExpressionFactory;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.deltaecore.integration.ie.feature.DEFeatureModelImporter;
import org.deltaecore.integration.ie.feature.data.DEFeatureModelImportExportData;
import org.deltaecore.integration.ie.feature.featureide.data.DEFeatureIDEFeatureModelImportExportData;
import org.eclipse.core.resources.IFile;
import org.prop4j.And;
import org.prop4j.Equals;
import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;
import org.prop4j.Or;

import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.io.IFeatureModelReader;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelReader;

//Import a FeatureIDE feature model to a DeltaEcore feature model
@SuppressWarnings("deprecation")
public class DEFeatureIDEFeatureModelImporter extends DEFeatureModelImporter<DEFeatureIDEFeatureModelImportExportData> {
	@Override
	protected DEFeatureIDEFeatureModelImportExportData loadFeatureModel(IFile file) {
		DefaultFeatureModelFactory factory = new DefaultFeatureModelFactory();
		IFeatureModel featureModel = factory.createFeatureModel();
		IFeatureModelReader reader = new XmlFeatureModelReader(featureModel);

		File javaFile = new File(file.getLocation().toString());

		try {
			reader.readFromFile(javaFile);
		} catch (Exception e) {
			return null;
		}

		return new DEFeatureIDEFeatureModelImportExportData(featureModel);
	}

	@Override
	protected DEFeatureModelImportExportData doImportFeatureModel(
			DEFeatureIDEFeatureModelImportExportData featureModelImportData) {
		Map<IFeature, DEFeature> featureMap = new HashMap<IFeature, DEFeature>();

		IFeatureModel featureModelToImport = featureModelImportData.getFeatureModel();
		IFeatureModelStructure featureModelStructure = featureModelToImport.getStructure();
		IFeatureStructure rootFeatureStructure = featureModelStructure.getRoot();
		IFeature feature = rootFeatureStructure.getFeature();
		DEFeature deFeature = doImportFeature(feature);
		featureMap.put(feature, deFeature);

		DEFeatureModel deFeatureModel = DEFeatureUtil.createFeatureModel();
		deFeatureModel.setRootFeature(deFeature);

		DEConstraintModel deConstraintModel = DEConstraintFactory.eINSTANCE.createDEConstraintModel();
		for (IConstraint constraintToImport : featureModelToImport.getConstraints()) {
			deConstraintModel.getConstraints().add(doImportConstraint(constraintToImport, featureMap));
		}

		return new DEFeatureModelImportExportData(deFeatureModel, deConstraintModel);
	}

	private DEFeature doImportFeature(IFeature feature) {
		String name = feature.getName();

		DEFeature deFeature = DEFeatureUtil.createFeature(name);

		IFeatureStructure featureStructure = feature.getStructure();
		IFeatureStructure parentFeatureStructure = featureStructure.getParent();
		IFeature parentFeature = parentFeatureStructure == null ? null : parentFeatureStructure.getFeature();

		// Variation type of feature
		if (parentFeature == null) {
			// Root feature is always mandatory
			deFeature.setMinCardinality(1);
			deFeature.setMaxCardinality(1);
		} else if (parentFeatureStructure.isOr() || parentFeatureStructure.isAlternative()) {
			// In dedicated groups, all features are perceived as being optional
			deFeature.setMinCardinality(0);
			deFeature.setMaxCardinality(1);
		} else if (featureStructure.isMandatory()) {
			deFeature.setMinCardinality(1);
			deFeature.setMaxCardinality(1);
		} else {
			// Can only be optional
			deFeature.setMinCardinality(0);
			deFeature.setMaxCardinality(1);
		}

		List<IFeatureStructure> childStructures = featureStructure.getChildren();

		if (!childStructures.isEmpty()) {
			List<DEGroup> deGroups = deFeature.getGroups();

			DEGroup deGroup = DEFeatureUtil.createGroup();
			deGroups.add(deGroup);

			List<DEFeature> deChildren = deGroup.getFeatures();

			// Variation type of group
			if (featureStructure.isAlternative()) {
				deGroup.setMinCardinality(1);
				deGroup.setMaxCardinality(1);
			} else if (featureStructure.isOr()) {
				deGroup.setMinCardinality(1);
				deGroup.setMaxCardinality(childStructures.size());
			} else if (featureStructure.isAnd()) {
				// Minimum is the number of mandatory child features
				int numberOfMandatoryChildFeatures = 0;

				for (IFeatureStructure childStructure : childStructures) {
					if (childStructure.isMandatory()) {
						numberOfMandatoryChildFeatures++;
					}
				}

				deGroup.setMinCardinality(numberOfMandatoryChildFeatures);
				deGroup.setMaxCardinality(childStructures.size());
			}

			for (IFeatureStructure childStructure : childStructures) {
				IFeature childFeature = childStructure.getFeature();
				DEFeature deChild = doImportFeature(childFeature);
				deChildren.add(deChild);
			}
		}

		return deFeature;
	}

	/**
	 * 
	 * @param constraintToImport
	 * @param featureMap
	 * @return can be null
	 */
	private DEConstraint doImportConstraint(IConstraint constraintToImport, Map<IFeature, DEFeature> featureMap) {

		Node node = constraintToImport.getNode();
		DEExpression expression = createExpression(node, featureMap);

		if (expression != null) {
			DEConstraint constraint = DEConstraintFactory.eINSTANCE.createDEConstraint();
			constraint.setRootExpression(expression);
			return constraint;
		}

		return null;
	}

	private DEExpression createExpression(Node node, Map<IFeature, DEFeature> featureMap) {
		DEExpressionFactory expressionFactory = DEExpressionFactory.eINSTANCE;

		if (node instanceof Not) {
			DENotExpression notExpression = expressionFactory.createDENotExpression();
			notExpression.setOperand(createExpression(node.getChildren()[0], featureMap));
			return notExpression;
		} else if (node instanceof Literal) {
			Literal literal = (Literal) node;

			if (literal.var instanceof String) {
				DEFeatureReferenceExpression featureReferenceExpression = expressionFactory
						.createDEFeatureReferenceExpression();
				DEFeature feature = getDEFeatureFromFeatureMap((String) literal.var, featureMap);
				featureReferenceExpression.setFeature(feature);

				return featureReferenceExpression;
			} else {
				System.err.println("Could not find referenced feature of literal: " + literal);
				// TODO proper error handling
			}
		} else {
			DEBinaryExpression binaryExpression = null;
			if (node instanceof And) {
				binaryExpression = expressionFactory.createDEAndExpression();
			} else if (node instanceof Or) {
				binaryExpression = expressionFactory.createDEOrExpression();
			} else if (node instanceof Implies) {
				binaryExpression = expressionFactory.createDEImpliesExpression();
			} else if (node instanceof Equals) {
				binaryExpression = expressionFactory.createDEEquivalenceExpression();
			}
			DEExpression operand1 = createExpression(node.getChildren()[0], featureMap);
			DEExpression operand2 = createExpression(node.getChildren()[1], featureMap);
			
			if(operand1 == null || operand2 == null) {
				return null;
			}
			
			binaryExpression.setOperand1(operand1);
			binaryExpression.setOperand2(operand2);
			
			return binaryExpression;
		}
		
		return null;

	}

	private DEFeature getDEFeatureFromFeatureMap(String featureName, Map<IFeature, DEFeature> featureMap) {
		for (Entry<IFeature, DEFeature> entry : featureMap.entrySet()) {
			if (entry.getKey().getName().equals(featureName)) {
				return entry.getValue();
			}
		}

		return null;
	}
}
