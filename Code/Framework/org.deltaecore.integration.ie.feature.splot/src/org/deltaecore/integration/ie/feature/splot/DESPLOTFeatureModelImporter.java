package org.deltaecore.integration.ie.feature.splot;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DECardinalityBasedElement;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintFactory;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEExpressionFactory;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.deltaecore.integration.ie.feature.DEFeatureModelImporter;
import org.deltaecore.integration.ie.feature.data.DEFeatureModelImportExportData;
import org.deltaecore.integration.ie.feature.splot.data.DESPLOTFeatureModelImportExportData;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;

import constraints.BooleanVariableInterface;
import constraints.CNFClause;
import constraints.CNFLiteral;
import constraints.PropositionalFormula;
import fm.FeatureGroup;
import fm.FeatureModel;
import fm.FeatureTreeNode;
import fm.RootNode;
import fm.SolitaireFeature;
import fm.XMLFeatureModel;

public class DESPLOTFeatureModelImporter extends DEFeatureModelImporter<DESPLOTFeatureModelImportExportData> {
	private Map<String, DEFeature> idToDEFeatureMapping;
	
	public DESPLOTFeatureModelImporter() {
		idToDEFeatureMapping = new HashMap<String, DEFeature>();
	}

	@Override
	protected void reset() {
		super.reset();
		
		idToDEFeatureMapping.clear();
	}
	
	@Override
	protected DESPLOTFeatureModelImportExportData loadFeatureModel(IFile file) {
		try {
			IPath absolutePath = file.getLocation();
			FeatureModel featureModel = new XMLFeatureModel(absolutePath.toString(), XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);
			
			featureModel.loadModel();
			
			return new DESPLOTFeatureModelImportExportData(featureModel);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected DEFeatureModelImportExportData doImportFeatureModel(DESPLOTFeatureModelImportExportData featureModelImportData) {
		FeatureModel featureModel = featureModelImportData.getFeatureModel();
		
		DEFeatureModel deFeatureModel = convertFeatureModel(featureModel);
		DEConstraintModel deConstraintModel = convertConstraints(featureModel);
		
		return new DEFeatureModelImportExportData(deFeatureModel, deConstraintModel);
	}
	
	
	//Feature Model
	
	protected DEFeatureModel convertFeatureModel(FeatureModel featureModel) {
		DEFeatureModel deFeatureModel = DEFeatureUtil.createFeatureModel();
		convertFeatureTreeNode(featureModel.getRoot(), deFeatureModel);
		
		return deFeatureModel;
	}
	
	protected void convertFeatureTreeNode(FeatureTreeNode node, EObject deParentElement) {
		DECardinalityBasedElement deConvertedElement = doConvertFeatureTreeNode(node, deParentElement);
		
		if (deConvertedElement instanceof DEFeature) {
			DEFeature deConvertedFeature = (DEFeature) deConvertedElement;

			//Store feature id
			String id = node.getID();
			idToDEFeatureMapping.put(id, deConvertedFeature);
		}
		
		//All elements of the SPLOT feature model may have child nodes. Convert them.
		convertChildNodes(node, deConvertedElement);
	}
	
	protected DECardinalityBasedElement doConvertFeatureTreeNode(FeatureTreeNode node, EObject deParentElement) {
		if (node instanceof FeatureGroup) {
			FeatureGroup featureGroup = (FeatureGroup) node;
			DEFeature deParentFeature = (DEFeature) deParentElement;
			
			return convertFeatureGroup(featureGroup, deParentFeature);
		}
		
		if (node instanceof RootNode) {
			RootNode rootNode = (RootNode) node;
			DEFeatureModel deFeatureModel = (DEFeatureModel) deParentElement;

			return convertRootNode(rootNode, deFeatureModel);
		}
		
		if (node instanceof SolitaireFeature) {
			SolitaireFeature solitaireFeature = (SolitaireFeature) node;
			DEFeature deParentFeature = (DEFeature) deParentElement;
			
			return convertSolitaireFeature(solitaireFeature, deParentFeature);
		}
		
		//If nothing else matches, it's a grouped feature
		DEGroup deParentGroup = (DEGroup) deParentElement;

		return convertGroupedFeature(node, deParentGroup);
	}

	private DEGroup convertFeatureGroup(FeatureGroup featureGroup,	DEFeature deParentFeature) {
		int minCardinality = featureGroup.getMin();
		int maxCardinality = featureGroup.getMax();
		
		DEGroup deGroup = DEFeatureUtil.createGroup(minCardinality, maxCardinality);
		List<DEGroup> deParentGroups = deParentFeature.getGroups();
		deParentGroups.add(deGroup);
		
		return deGroup;
	}
	
	private DEFeature convertRootNode(RootNode rootNode, DEFeatureModel deFeatureModel) {
		String name = rootNode.getName();
		DEFeature deRootFeature = DEFeatureUtil.createFeature(name);
		DEFeatureUtil.makeMandatory(deRootFeature);
		
		deFeatureModel.setRootFeature(deRootFeature);
		
		return deRootFeature;
	}

	private DEFeature convertSolitaireFeature(SolitaireFeature solitaireFeature, DEFeature deParentFeature) {
		String name = solitaireFeature.getName();
		DEFeature deFeature = DEFeatureUtil.createFeature(name);

		if (solitaireFeature.isOptional()) {
			DEFeatureUtil.makeOptional(deFeature);
		} else {
			DEFeatureUtil.makeMandatory(deFeature);
		}
		
		///Have to add dummy group as the original was a feature to feature nesting
		DEGroup deIntermediateGroup = DEFeatureUtil.createGroup(0, 1);
		List<DEGroup> deParentGroups = deParentFeature.getGroups();
		deParentGroups.add(deIntermediateGroup);
		
		List<DEFeature> deFeatures = deIntermediateGroup.getFeatures();
		deFeatures.add(deFeature);
		
		return deFeature;
	}
	
	private DEFeature convertGroupedFeature(FeatureTreeNode node, DEGroup deParentGroup) {
		String name = node.getName();
		DEFeature deFeature = DEFeatureUtil.createFeature(name);
		DEFeatureUtil.makeOptional(deFeature);

		List<DEFeature> deFeatures = deParentGroup.getFeatures();
		deFeatures.add(deFeature);
		
		return deFeature;
	}
	
	
	private void convertChildNodes(FeatureTreeNode node, EObject deParentElement) {
		//Recursive call.
		int n = node.getChildCount();
		
		for (int i = 0; i < n; i++) {
			FeatureTreeNode childFeatureTreeNode = (FeatureTreeNode) node.getChildAt(i);
			convertFeatureTreeNode(childFeatureTreeNode, deParentElement);
		}
	}
	
	
	//Constraints
	
	protected DEConstraintModel convertConstraints(FeatureModel featureModel) {
		Collection<PropositionalFormula> constraints = featureModel.getConstraints();
		
		if (constraints.isEmpty()) {
			return null;
		}
		
		DEConstraintModel deConstraintModel = DEConstraintFactory.eINSTANCE.createDEConstraintModel();
		List<DEConstraint> deConstraints = deConstraintModel.getConstraints();
		
		for (PropositionalFormula formula : constraints) {
			DEConstraint deConstraint = convertFormula(formula);
			deConstraints.add(deConstraint);
		}
		
		return deConstraintModel;
	}
	
	protected DEConstraint convertFormula(PropositionalFormula formula) {
		DEConstraint deConstraint = DEConstraintFactory.eINSTANCE.createDEConstraint();
		
		//Maybe add as comment some time?
		//String name = formula.getName();
		
		DEExpression rootExpression = doConvertFormula(formula);
		deConstraint.setRootExpression(rootExpression);
		
		return deConstraint;
	}
	
	protected DEExpression doConvertFormula(PropositionalFormula formula) {
		//In CNF, no further parsing, contains feature IDs not names
		//Note: Need an (= any defined) order on clauses for iteration. 
		List<CNFClause> clauses = new LinkedList<CNFClause>(formula.toCNFClauses());
		
		if (clauses.size() == 1) {
			//Direct child
			CNFClause clause = clauses.get(0);
			return convertClause(clause);
		} else {
			//Cascade of and expressions
			DEAndExpression firstDEAndExpression = null;
			DEAndExpression previousDEAndExpression = null;
			
			//Iterate all but the last clause
			for (int i = 0; i < clauses.size() - 1; i++) {
				CNFClause clause = clauses.get(i);
				
				DEAndExpression deAndExpression = DEExpressionFactory.eINSTANCE.createDEAndExpression();
				
				//Nest and expressions.
				if (previousDEAndExpression != null) {
					previousDEAndExpression.setOperand2(deAndExpression);
				}
				
				DEExpression deConvertedClause = convertClause(clause);
				deAndExpression.setOperand1(deConvertedClause);
				
				
				if (previousDEAndExpression == null) {
					firstDEAndExpression = deAndExpression;
				}
				
				previousDEAndExpression = deAndExpression;
			}
			
			CNFClause lastClause = clauses.get(clauses.size() - 1);
			DEExpression deConvertedLastClause = convertClause(lastClause);
			
			if (previousDEAndExpression != null) {
				previousDEAndExpression.setOperand2(deConvertedLastClause);
				return firstDEAndExpression;
			} else {
				return deConvertedLastClause;
			}
		}
	}
	
	protected DEExpression convertClause(CNFClause clause) {
		List<CNFLiteral> literals = clause.getLiterals();
		
		if (literals.size() == 1) {
			//Direct child
			CNFLiteral literal = literals.get(0);
			return convertLiteral(literal);
		} else {
			//Cascade of or expressions
			DEOrExpression firstDEOrExpression = null;
			DEOrExpression previousDEOrExpression = null;
			
			//Iterate all but the last literal
			for (int i = 0; i < literals.size() - 1; i++) {
				CNFLiteral literal = literals.get(i);
				
				DEOrExpression deOrExpression = DEExpressionFactory.eINSTANCE.createDEOrExpression();
				
				//Nest or expressions.
				if (previousDEOrExpression != null) {
					previousDEOrExpression.setOperand2(deOrExpression);
				}
				
				DEExpression deConvertedLiteral = convertLiteral(literal);
				deOrExpression.setOperand1(deConvertedLiteral);
				
				
				if (previousDEOrExpression == null) {
					firstDEOrExpression = deOrExpression;
				}
				
				previousDEOrExpression = deOrExpression;
			}
			
			CNFLiteral lastLiteral = literals.get(literals.size() - 1);
			DEExpression deConvertedLastLiteral = convertLiteral(lastLiteral);
			
			if (previousDEOrExpression != null) {
				previousDEOrExpression.setOperand2(deConvertedLastLiteral);
				return firstDEOrExpression;
			} else {
				return deConvertedLastLiteral;
			}
		}
	}
	
	protected DEExpression convertLiteral(CNFLiteral literal) {
		BooleanVariableInterface variable = literal.getVariable();
		
		//Retrieve converted feature
		String id = variable.getID();
		DEFeature deFeature = idToDEFeatureMapping.get(id);
		
		DEFeatureReferenceExpression featureReferenceExpression = DEExpressionFactory.eINSTANCE.createDEFeatureReferenceExpression();
		featureReferenceExpression.setFeature(deFeature);
		
		boolean isPositive = literal.isPositive();
		
		if (isPositive) {
			return featureReferenceExpression;
		} else {
			DENotExpression deNotExpression = DEExpressionFactory.eINSTANCE.createDENotExpression();
			deNotExpression.setOperand(featureReferenceExpression);

			return deNotExpression;
		}
	}
}
