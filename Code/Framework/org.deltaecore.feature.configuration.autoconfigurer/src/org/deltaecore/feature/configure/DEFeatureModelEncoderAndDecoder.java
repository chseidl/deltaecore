package org.deltaecore.feature.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.configure.preprocessor.DEConstraintModelPreprocessor;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintUtil;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEBooleanValueExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.DEVersionRestriction;
import org.eclipse.emf.ecore.util.EcoreUtil;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.LogicalConstraintFactory;
import solver.constraints.set.SetConstraintsFactory;
import solver.search.strategy.IntStrategyFactory;
import solver.variables.IntVar;
import solver.variables.SetVar;
import solver.variables.Variable;
import solver.variables.VariableFactory;

//TODO: This should use the official Configuration model - Migrate!

//Converts an HFM to a CSP problem for Choco and the resulting configurations back again.
public class DEFeatureModelEncoderAndDecoder {
	private Solver solver;
	
	private Map<IntVar<?>, DEFeature> variableToFeatureMapping;
	private DEConfiguration initialConfiguration;
	private List<DEConfigurationArtifact> fixedConfigurationArtifacts;
	
	public DEFeatureModelEncoderAndDecoder(Solver solver) {
		this.solver = solver;
		
		variableToFeatureMapping = new HashMap<IntVar<?>, DEFeature>();
		fixedConfigurationArtifacts = new ArrayList<DEConfigurationArtifact>();
	}
	
	
	//Encoding
	
	public void encodeConfiguration(DEConfiguration initialConfiguration, DEConstraintModel constraintModel) {
		this.initialConfiguration = initialConfiguration;
		
		//Preprocess constraints of HFM to make encoding easier
		DEConstraintModel preprocessedConstraintModel = DEConstraintModelPreprocessor.preprocessConstraintModel(constraintModel, initialConfiguration);
		
		//DEBUG
//		DEDebug.println();
//		DEDebug.println("Preprocessed constraint model");
//		DEDebug.println(DEConstraintFormatter.formatConstraintModel(preprocessedConstraintModel));
		
		//Convert HFM to CSP for Choco
		encodeHFM(initialConfiguration);
		encodeConstraintModel(preprocessedConstraintModel);
		
		//Define the search strategy
		//TODO: Don't understand that
		Set<IntVar<?>> variables = variableToFeatureMapping.keySet();
		
		//If there is nothing left to configure, there are no variables either
		//That poses a problem for the solver. Hence, avoid that situation.
		if (!variables.isEmpty()) {
			IntVar<?>[] variablesArray = variables.toArray(new IntVar[0]);
			solver.set(IntStrategyFactory.inputOrder_InDomainMin(variablesArray));
		}
	}
	
	
	public void encodeHFM(DEConfiguration initialConfiguration) {
		List<DEConfigurationArtifact> configurationArtifacts = initialConfiguration.getConfigurationArtifacts();
		
		//Encode features that do not yet have a version in the configuration.
		for (DEConfigurationArtifact artifact : configurationArtifacts) {
			if (artifact instanceof DEFeatureSelection) {
				DEFeatureSelection featureSelection = (DEFeatureSelection) artifact;
				DEFeature feature = featureSelection.getFeature();
				
				//Only try to find versions for features that have no version in the configuration.
				DEVersion version = DEConfigurationUtil.getSelectedVersionForFeature(initialConfiguration, feature);
				
				if (version == null) {
					encodeFeatureToVariable(feature);
				}
			}
		}
	}

	protected void encodeFeatureToVariable(DEFeature feature) {
		if (variableToFeatureMapping.containsValue(feature)) {
			System.err.println("Feature already encoded as variable.");
			return;
		}
		
		String name = feature.getName();
		List<DEVersion> versions = feature.getVersions();
		
		if (versions.size() == 1) {
			//NOTE: Choco optimizes CSP by dropping variables that have only one possible value.
			//Remember those for later decoding of configurations.
			DEVersionSelection versionSelection = DEConfigurationFactory.eINSTANCE.createDEVersionSelection();
			versionSelection.setVersion(versions.get(0));
			fixedConfigurationArtifacts.add(versionSelection);
		} else {
			IntVar<?> variable =  VariableFactory.bounded(name, 0, versions.size() - 1, solver);
			
			variableToFeatureMapping.put(variable, feature);
		}
	}
	
	
	protected void encodeConstraintModel(DEConstraintModel constraintModel) {
		if (constraintModel == null) {
			return;
		}
		
		List<DEConstraint> constraints = constraintModel.getConstraints();
		
		for (DEConstraint constraint : constraints) {
			encodeConstraint(constraint);
		}
	}
	
	protected void encodeConstraint(DEConstraint constraint) {
		DEExpression rootExpression = constraint.getRootExpression();
		Constraint<?, ?> encodedConstraint = encodeExpression(rootExpression);
		
		if (encodedConstraint != null) {
			solver.post(encodedConstraint);
		}
	}
	
	protected Constraint<?,?> encodeExpression(DEExpression expression) {
		if (expression instanceof DEAtomicExpression) {
			//Boolean value expressions have been eliminated in the preprocessing...
			//... unless they are all that remains of a constraint's expression.
			if (expression instanceof DEBooleanValueExpression) {
				DEBooleanValueExpression booleanValueExpression = (DEBooleanValueExpression) expression;
				boolean value = booleanValueExpression.isValue();
				
				if (value) {
					//Constraints that were evaluated to true can just be skipped.
					return null;
				} else {
					//Constraints that evaluated to false can never be satisfied
					//and cause the auto configuration process to fail.
					throw new UnsupportedOperationException("Invalid constraint in input constraint model.");
				}
			}
			
			//Conditional feature references have been eliminated in the preprocessing.
			
			if (expression instanceof DEFeatureReferenceExpression) {
				DEFeatureReferenceExpression featureReferenceExpression = (DEFeatureReferenceExpression) expression;
				DEFeature feature = featureReferenceExpression.getFeature();
				DEVersionRestriction versionRestriction = featureReferenceExpression.getVersionRestriction();

				IntVar<?> featureVariable = findVariableForFeature(feature);
				List<DEVersion> versionsSatisfyingVersionRestriction = DEConstraintUtil.getVersionsSatisfyingVersionRestriction(versionRestriction);
				SetVar setOfVersionsSatisfyingVersionRestriction = encodeVersionList(versionsSatisfyingVersionRestriction);
				
				//feature \in {versions}
				return SetConstraintsFactory.member(featureVariable, setOfVersionsSatisfyingVersionRestriction);
			}
		}
		
		if (expression instanceof DEUnaryExpression) {
			//NOTE: Nested expressions have been eliminated in the preprocessing.
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			DEExpression operand = unaryExpression.getOperand();
			Constraint<?, ?> encodedOperand = encodeExpression(operand);
			
			if (expression instanceof DENotExpression) {
				return LogicalConstraintFactory.not(encodedOperand);
			}
		}
		
		if (expression instanceof DEBinaryExpression) {
			//NOTE: Implications and equivalences have been eliminated in the preprocessing.
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			DEExpression operand1 = binaryExpression.getOperand1();
			DEExpression operand2 = binaryExpression.getOperand2();
			
			Constraint<?, ?> encodedOperand1 = encodeExpression(operand1);
			Constraint<?, ?> encodedOperand2 = encodeExpression(operand2);
			
			if (expression instanceof DEAndExpression) {
				return LogicalConstraintFactory.and(encodedOperand1, encodedOperand2);
			}
			
			if (expression instanceof DEOrExpression) {
				return LogicalConstraintFactory.or(encodedOperand1, encodedOperand2);
			}
		}
		
		throw new UnsupportedOperationException("Don't know how to encode " + expression.getClass().getSimpleName() + ".");
	}
	
	
	
	protected IntVar<?> findVariableForFeature(DEFeature feature) {
		for (Entry<IntVar<?>, DEFeature> entry : variableToFeatureMapping.entrySet()) {
			DEFeature value = entry.getValue();
					
			if (value == feature) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	
	protected SetVar encodeVersionList(List<DEVersion> versions) {
		int[] versionIds = new int[versions.size()];
		int i = 0;
		
		for (DEVersion version : versions) {
			DEFeature feature = version.getFeature();
			int versionId = feature.getVersions().indexOf(version);
			versionIds[i] = versionId;
			i++;
		}
		
		return createFixedSetVar(versionIds, solver);
	}
	
	//Careful with set vars:
	//SetVar set = VF.set("set", new int[] {3, 4, 5}, solver);
	//creates a set variable whose domain is comprised between the empty set ({}) and {3,4,5}. This means its possible values are {}, {3}, {4}, {5}, {3,4}, {3,5}, {4,5}, {3,4,5}.
	//In case you want to have a fixed SetVar, whose unique value is {3,4,5}, then you currently need to use the method:
	//SetVar set = VF.set("set", new int[] {3, 4, 5}, new int[] {3, 4, 5}, solver);
	public static SetVar createFixedSetVar(int[] values, Solver solver) {
		return VariableFactory.set("", values, values, solver);
	}
	
	
	//Decoding
	
	public DEConfiguration decodeCurrentConfiguration() {
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		configurationArtifacts.addAll(EcoreUtil.copyAll(initialConfiguration.getConfigurationArtifacts()));
		configurationArtifacts.addAll(EcoreUtil.copyAll(fixedConfigurationArtifacts));
		
		Variable<?>[] variables = solver.getVars();
		
		for (Variable<?> variable : variables) {
			//See if it is a feature variable.
			if (variable instanceof IntVar<?> && variableToFeatureMapping.containsKey(variable)) {
				IntVar<?> featureVariable = (IntVar<?>) variable;
				int versionIndex = featureVariable.getValue();
				
				DEFeature feature = variableToFeatureMapping.get(variable);
				List<DEVersion> versions = feature.getVersions();
				DEVersion version = versions.get(versionIndex);

				DEConfigurationUtil.addVersionToConfiguration(version, configuration, false);
			}
		}
		
		//DEBUG:
//		DEFeatureUtil.checkConfigurationValidity(configuration);
		
		return configuration;
	}
}
