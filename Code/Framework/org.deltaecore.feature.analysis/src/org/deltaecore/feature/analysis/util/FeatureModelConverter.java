package org.deltaecore.feature.analysis.util;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENestedExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.LCF;
import solver.constraints.LCF2;


//TODO: This does NOT support versions right now!

public class FeatureModelConverter {
	private Solver solver;
	
	private FeatureVariableEncoding featureVariableEncoding;
	
	public Solver convertFeatureModel(DEFeatureModel featureModel) {
		return convertFeatureModel(featureModel, (DEConstraintModel) null);
	}
	
	public Solver convertFeatureModel(DEFeatureModel featureModel, DEConstraintModel constraintModel) {
		//Create new solver
		Solver solver = createSolver();
		convertFeatureModel(featureModel, constraintModel, solver);
		return solver;
	}
	
	protected Solver createSolver() {
		return new Solver();
	}
	
	public void convertFeatureModel(DEFeatureModel featureModel, DEConstraintModel constraintModel, Solver solver) {
		this.solver = solver;
		
		//Create variables for all features
		featureVariableEncoding = new FeatureVariableEncoding(featureModel, solver);
		
		//Create CSP constraints for feature model
		encodeFeatureModel(featureModel);
		
		//Create CSP constraints for constraints model
		encodeConstraintModel(constraintModel);
	}

	protected void encodeFeatureModel(DEFeatureModel featureModel) {
		DEFeature rootFeature = featureModel.getRootFeature();
		
		//Root feature always selected
		solver.post(featureVariableEncoding.selected(rootFeature));
		
		encodeFeature(rootFeature);
	}

	protected void encodeFeature(DEFeature feature) {
		int minCardinality = feature.getMinCardinality();
		int maxCardinality = feature.getMaxCardinality();
		
		if (minCardinality < 0 || minCardinality > 1 || maxCardinality != 1) {
			throw new RuntimeException("Invalid cardinality");
		}
		
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup != null) {
			DEFeature parentFeature = parentGroup.getParentOfGroup();
		
			if (parentFeature != null) {		
				//Feature -> Parent
				solver.post(featureVariableEncoding.implies(feature, parentFeature));
				
				//Feature Mandatory: Parent -> Feature
				if (minCardinality == 1) {
					solver.post(featureVariableEncoding.implies(parentFeature, feature));
				}
			}
		}
		
		List<DEGroup> groups = feature.getGroups();
		
		for (DEGroup group : groups) {
			encodeGroup(group);
		}
	}
	
	protected void encodeGroup(DEGroup group) {
		int minCardinality = group.getMinCardinality();
		int maxCardinality = group.getMaxCardinality();
		
		List<DEFeature> features = group.getFeatures();
		int numberOfFeatures = features.size();
		
		if (minCardinality == 1 && maxCardinality == 1) {
			//Alternative group
			solver.post(featureVariableEncoding.alternativeGroup(group));
		} else if (minCardinality == 1 && maxCardinality == numberOfFeatures) {
			//Or group
			solver.post(featureVariableEncoding.orGroup(group));
		} else {
			//Arbitrary group
			solver.post(featureVariableEncoding.andGroup(group));
		}
		
		for (DEFeature feature : features) {
			encodeFeature(feature);
		}
	}
	
	protected void encodeConstraintModel(DEConstraintModel constraintModel) {
		if (constraintModel != null) {
			List<DEConstraint> constraints = constraintModel.getConstraints();
			
			for (DEConstraint constraint : constraints) {
				encodeConstraint(constraint);
			}
		}
	}
	
	protected void encodeConstraint(DEConstraint constraint) {
		DEExpression expression = constraint.getRootExpression();
		Constraint<?, ?> constraint2 = expression(expression);
		solver.post(constraint2);
	}
	
	protected Constraint<?, ?> expression(DEExpression expression) {
		//Binary
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			return binaryExpression(binaryExpression);
		}
		
		//Unary
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			return unaryExpression(unaryExpression);
		}
		
		//Atomic
		if (expression instanceof DEAtomicExpression) {
			DEAtomicExpression atomicExpression = (DEAtomicExpression) expression;
			
			return atomicExpression(atomicExpression);
		}
		
		throw new RuntimeException("Unknown expression type.");
	}

	protected Constraint<?, ?> binaryExpression(DEBinaryExpression expression) {
		DEExpression operand1 = expression.getOperand1();
		DEExpression operand2 = expression.getOperand2();
		
		Constraint<?, ?> constraint1 = expression(operand1);
		Constraint<?, ?> constraint2 = expression(operand2);
		
		if (expression instanceof DEOrExpression) {
			return LCF.or(constraint1, constraint2);
		}
		
		if (expression instanceof DEAndExpression) {
			return LCF.and(constraint1, constraint2);
		}
		
		if (expression instanceof DEImpliesExpression) {
			return LCF2.implies(constraint1, constraint2);
		}
		
		//This is a horrible encoding that won't scale even to small problems.
		if (expression instanceof DEEquivalenceExpression) {
			//A <-> B = ((A && B) || (!A && !B))
			//NOTE: Reformulate on model level as Choco has no easy way of duplicating constraints.
			Constraint<?, ?> constraint1B = expression(operand1);
			Constraint<?, ?> constraint2B = expression(operand2);
			
			return LCF.or(LCF.and(constraint1, constraint2), LCF.and(LCF.not(constraint1B), LCF.not(constraint2B)));
		}
		
		throw new RuntimeException("Unknown binary expression type.");
	}
	
	protected Constraint<?, ?> unaryExpression(DEUnaryExpression expression) {
		DEExpression operand = expression.getOperand();
		
		Constraint<?, ?> constraint = expression(operand);
		
		if (expression instanceof DENotExpression) {
			return LCF.not(constraint);
		}
		
		if (expression instanceof DENestedExpression) {
			return constraint;
		}
		
		throw new RuntimeException("Unknown unary expression type.");
	}
	
	protected Constraint<?, ?> atomicExpression(DEAtomicExpression expression) {
		
		//TODO: Boolean values
//		if (expression instanceof DEBooleanValueExpression) {
//			DEBooleanValueExpression booleanValueExpression = (DEBooleanValueExpression) expression;
//			boolean value = booleanValueExpression.isValue();
//			int intValue = value ? 1 : 0;
//			
//		}
		
		if (expression instanceof DEFeatureReferenceExpression) {
			DEFeatureReferenceExpression featureReferenceExpression = (DEFeatureReferenceExpression) expression;
			DEFeature feature = featureReferenceExpression.getFeature();
			return featureVariableEncoding.selected(feature);
		}
		
		//TODO: Conditional
		if (expression instanceof DEConditionalFeatureReferenceExpression) {
			DEConditionalFeatureReferenceExpression conditionalFeatureReferenceExpression = (DEConditionalFeatureReferenceExpression) expression;
			DEFeature feature = conditionalFeatureReferenceExpression.getFeature();
			return featureVariableEncoding.selected(feature);
		}
		
		throw new RuntimeException("Unknown atomic expression type.");
	}

	public FeatureVariableEncoding getFeatureVariableEncoding() {
		return featureVariableEncoding;
	}

	protected Solver getSolver() {
		return solver;
	}
}
