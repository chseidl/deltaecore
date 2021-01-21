package org.deltaecore.feature.configure.example;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureFactory;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintFactory;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEExpressionFactory;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DERelativeVersionRestriction;
import org.deltaecore.feature.expression.DERelativeVersionRestrictionOperator;
import org.deltaecore.feature.expression.DEVersionRangeRestriction;

public class DEFeatureModelSampleCreator {
	private DEFeatureModel featureModel;
	
	private DEFeature turtleBotFeature;
	private DEVersion turtleBot_1_0;
	private DEVersion turtleBot_1_1;
	private DEVersion turtleBot_2_0;
	private DEVersion turtleBot_2_1;
	
	
	private DEFeature engineFeature;
	private DEVersion engine_1_0;
	private DEVersion engine_1_1;
	private DEVersion engine_Create_1_2;
	private DEVersion engine_Kobuki_1_0;
	
	
	private DEFeature movementFeature;
	private DEVersion movement_1_0;
	private DEVersion movement_1_1;
	private DEVersion movement_1_2;
	private DEVersion movement_2_0;
	
	private DEFeature keyboardFeature;
	private DEVersion keyboard_1_0;
	
	private DEFeature gamepadFeature;
	private DEVersion gamepad_1_0;
	private DEVersion gamepad_2_0;
	
	private DEFeature autonomousFeature;
	private DEVersion autonomous_1_0;
	private DEVersion autonomous_1_1;
	private DEVersion autonomous_2_0;
	
	
	private DEFeature webserviceFeature;
	private DEVersion webservice_1_0;
	private DEVersion webservice_1_1;

	
	private DEFeature detectionFeature;
	private DEVersion detection_1_0;
	private DEVersion detection_1_1;
	
	
	private DEFeature bumpFeature;
	private DEVersion bump_1_0;
	
	
	private DEFeature infraredFeature;
	private DEVersion infrared_1_0;
	private DEVersion infrared_2_0;
	private DEVersion infrared_2_2;
	
	
	private DEFeature ultrasoundFeature;
	private DEVersion ultrasound_0_8;
	private DEVersion ultrasound_0_9;
	private DEVersion ultrasound_1_0;
	
	
	private DEConstraintModel constraintModel;
	
	
	public DEFeatureModelSampleCreator() {
		createSampleFeatureModel();
		createSampleConstraintModel();
	}
	
	protected static DEFeature createFeature(String name, int minCardinality, int maxCardinality, DEFeature parentFeature) {
		DEFeature feature = DEFeatureFactory.eINSTANCE.createDEFeature();
		
		feature.setName(name);
		feature.setMinCardinality(minCardinality);
		feature.setMaxCardinality(maxCardinality);
		
		if (parentFeature != null) {
			List<DEGroup> groups = parentFeature.getGroups();
			DEGroup parentGroup = groups.get(0);
			
			if (parentGroup != null) {
				parentGroup.getFeatures().add(feature);
			}
		}
		
		return feature;
	}
	
	protected static DEFeature createFeature(String name, int minCardinality, int maxCardinality, int groupMinCardinality, int groupMaxCardinality, DEFeature parentFeature) {
		DEFeature feature = createFeature(name, minCardinality, maxCardinality, parentFeature);
		
		//Create group
		DEGroup group = DEFeatureFactory.eINSTANCE.createDEGroup();
		group.setMinCardinality(groupMinCardinality);
		group.setMaxCardinality(groupMaxCardinality);
		
		feature.getGroups().add(group);
		
		return feature;
	}
	
	protected static DEVersion createVersion(String number, DEFeature containingFeature, DEVersion predecessor) {
		DEVersion version = DEFeatureFactory.eINSTANCE.createDEVersion();
		
		version.setNumber(number);
		version.setFeature(containingFeature);
		version.setSupersededVersion(predecessor);
		
		return version;
	}
	
	protected DEFeatureModel createSampleFeatureModel() {
		featureModel = DEFeatureFactory.eINSTANCE.createDEFeatureModel();
		
		turtleBotFeature = createFeature("TurtleBot", 1, 1, 2, 4, null);
		
		turtleBot_1_0 = createVersion("1.0", turtleBotFeature, null);
		turtleBot_1_1 = createVersion("1.1", turtleBotFeature, turtleBot_1_0);
		turtleBot_2_0 = createVersion("2.0", turtleBotFeature, turtleBot_1_0);
		turtleBot_2_1 = createVersion("2.1", turtleBotFeature, turtleBot_2_0);
		
		featureModel.setRootFeature(turtleBotFeature);
		
		
		engineFeature = createFeature("Engine", 1, 1, turtleBotFeature);
		
		engine_1_0 = createVersion("1.0", engineFeature, null);
		engine_1_1 = createVersion("1.1", engineFeature, engine_1_0);
		engine_Create_1_2 = createVersion("Create 1.2", engineFeature, engine_1_1);
		engine_Kobuki_1_0 = createVersion("Kobuki 1.0", engineFeature, engine_1_1);
		
		
		movementFeature = createFeature("Movement", 1, 1, 1, 1, turtleBotFeature);
		
		movement_1_0 = createVersion("1.0", movementFeature, null);
		movement_1_1 = createVersion("1.1", movementFeature, movement_1_0);
		movement_1_2 = createVersion("1.2", movementFeature, movement_1_1);
		movement_2_0 = createVersion("2.0", movementFeature, movement_1_2);
		
		
		keyboardFeature = createFeature("Keyboard", 0, 1, movementFeature);
		
		keyboard_1_0 = createVersion("1.0", keyboardFeature, null);
		
		
		gamepadFeature = createFeature("Gamepad", 0, 1, movementFeature);
		
		gamepad_1_0 = createVersion("1.0", gamepadFeature, null);
		gamepad_2_0 = createVersion("2.0", gamepadFeature, gamepad_1_0);
		
		
		autonomousFeature = createFeature("Autonomous", 0, 1, movementFeature);
		
		autonomous_1_0 = createVersion("1.0", autonomousFeature, null);
		autonomous_1_1 = createVersion("1.1", autonomousFeature, autonomous_1_0);
		autonomous_2_0 = createVersion("2.0", autonomousFeature, autonomous_1_1);
		
		
		webserviceFeature = createFeature("Webservice", 0, 1, turtleBotFeature);
		
		webservice_1_0 = createVersion("1.0", webserviceFeature, null);
		webservice_1_1 = createVersion("1.1", webserviceFeature, webservice_1_0);
		
		
		detectionFeature = createFeature("Detection", 0, 1, 1, 1, turtleBotFeature);
		
		detection_1_0 = createVersion("1.0", detectionFeature, null);
		detection_1_1 = createVersion("1.1", detectionFeature, detection_1_0);
		
		
		bumpFeature = createFeature("Bump", 0, 1, detectionFeature);
		
		bump_1_0 = createVersion("1.0", bumpFeature, null);
		
		
		infraredFeature = createFeature("Infrared", 0, 1, detectionFeature);
		
		infrared_1_0 = createVersion("1.0", infraredFeature, null);
		infrared_2_0 = createVersion("2.0", infraredFeature, infrared_1_0);
		infrared_2_2 = createVersion("2.2", infraredFeature, infrared_2_0);
		
		
		ultrasoundFeature = createFeature("Ultrasound", 0, 1, detectionFeature);
		
		ultrasound_0_8 = createVersion("0.8", ultrasoundFeature, null);
		ultrasound_0_9 = createVersion("0.9", ultrasoundFeature, ultrasound_0_8);
		ultrasound_1_0 = createVersion("1.0", ultrasoundFeature, ultrasound_0_9);
		
		return featureModel;
	}

	
	protected void createSampleConstraintModel() {
		constraintModel = DEConstraintFactory.eINSTANCE.createDEConstraintModel();
		List<DEConstraint> constraints = constraintModel.getConstraints();

		//Autonomous -> Detection
		constraints.add(createConstraint(createImpliesExpression(createFeatureReference(autonomousFeature), createFeatureReference(detectionFeature))));
		
		//Keyboard || Gamepad -> Webservice
		constraints.add(createConstraint(createImpliesExpression(createOrExpression(createFeatureReference(keyboardFeature), createFeatureReference(gamepadFeature)), createFeatureReference(webserviceFeature))));
		
		
		//TurtleBot [>= 2.0] -> Engine [>= Kobuki 1.0]
		constraints.add(createConstraint(createImpliesExpression(createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, turtleBot_2_0), createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, engine_Kobuki_1_0))));
		
		//TODO: Really "LESS_THAN"?
		//TurtleBot [1.0 - 1.1] -> Engine [< Create 1.2]
		constraints.add(createConstraint(createImpliesExpression(createFeatureReference(turtleBot_1_0, turtleBot_1_1), createFeatureReference(DERelativeVersionRestrictionOperator.LESS_THAN, engine_Create_1_2))));
		
		
		//Infrared [>= 2.0] || Ultrasound -> Detection [>= 1.1]
		constraints.add(createConstraint(createImpliesExpression(createOrExpression(createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, infrared_2_0), createFeatureReference(ultrasoundFeature)), createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, detection_1_1))));
		
		
		//NOTE: Nonsense
		//?Ultrasound [>= 0.9] <-> TurtleBot [>= 2.0]
//		constraints.add(createConstraint(createEquivalenceExpression(createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, ultrasound_0_9, true), createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, turtleBot_2_0))));
		
		//NOTE: Nonsense
		//?Ultrasound [>= 0.9] <-> ?TurtleBot [>= 2.0]
		//constraints.add(createConstraint(createEquivalenceExpression(createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, ultrasound_0_9, true), createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, turtleBot_2_0, true))));
		
		//NOTE: Still nonsense
		//TurtleBot && Ultrasound -> TurtleBot [>= 2.0] && Ultrasound [>= 0.9]
		//constraints.add(createConstraint(createImpliesExpression(createAndExpression(createFeatureReference(turtleBotFeature), createFeatureReference(ultrasoundFeature)), createAndExpression(createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, turtleBot_2_0), createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, ultrasound_0_9)))));
		
		//TurtleBot [>= 2.0] -> ?Webservice [>= 1.1]
		constraints.add(createConstraint(createImpliesExpression(createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, turtleBot_2_0), createFeatureReference(DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL, webservice_1_1, true))));
	}
	
	protected static DEConstraint createConstraint(DEExpression expression) {
		DEConstraint constraint = DEConstraintFactory.eINSTANCE.createDEConstraint();
		
		constraint.setRootExpression(expression);
		
		return constraint;
	}
	
	protected static DEOrExpression createOrExpression(DEExpression operand1, DEExpression operand2) {
		DEOrExpression orExpression = DEExpressionFactory.eINSTANCE.createDEOrExpression();
		orExpression.setOperand1(operand1);
		orExpression.setOperand2(operand2);
		
		return orExpression;
	}
	
	protected static DEAndExpression createAndExpression(DEExpression operand1, DEExpression operand2) {
		DEAndExpression andExpression = DEExpressionFactory.eINSTANCE.createDEAndExpression();
		andExpression.setOperand1(operand1);
		andExpression.setOperand2(operand2);
		
		return andExpression;
	}
	
	protected static DEImpliesExpression createImpliesExpression(DEExpression operand1, DEExpression operand2) {
		DEImpliesExpression impliesExpression = DEExpressionFactory.eINSTANCE.createDEImpliesExpression();
		impliesExpression.setOperand1(operand1);
		impliesExpression.setOperand2(operand2);
		
		return impliesExpression;
	}
	
	protected static DEEquivalenceExpression createEquivalenceExpression(DEExpression operand1, DEExpression operand2) {
		DEEquivalenceExpression equivalenceExpression = DEExpressionFactory.eINSTANCE.createDEEquivalenceExpression();
		equivalenceExpression.setOperand1(operand1);
		equivalenceExpression.setOperand2(operand2);
		
		return equivalenceExpression;
	}
	
	protected static DEAbstractFeatureReferenceExpression createFeatureReference(DEFeature feature) {
		return createFeatureReference(feature, false);
	}
	
	protected static DEAbstractFeatureReferenceExpression createFeatureReference(DEFeature feature, boolean conditional) {
		DEAbstractFeatureReferenceExpression featureReferenceExpression;
		
		if (conditional) {
			featureReferenceExpression = DEExpressionFactory.eINSTANCE.createDEConditionalFeatureReferenceExpression();
		} else {
			featureReferenceExpression = DEExpressionFactory.eINSTANCE.createDEFeatureReferenceExpression();
		}
		
		featureReferenceExpression.setFeature(feature);
		return featureReferenceExpression;
	}
	
	protected static DEAbstractFeatureReferenceExpression createFeatureReference(DERelativeVersionRestrictionOperator operator, DEVersion version) {
		return createFeatureReference(operator, version, false);
	}
	
	protected static DEAbstractFeatureReferenceExpression createFeatureReference(DERelativeVersionRestrictionOperator operator, DEVersion version, boolean conditional) {
		DEFeature feature = version.getFeature();
		DEAbstractFeatureReferenceExpression featureReferenceExpression = createFeatureReference(feature, conditional);
		
		featureReferenceExpression.setVersionRestriction(createRelativeVersionRestriction(operator, version));
		
		return featureReferenceExpression;
	}
	
	protected static DEAbstractFeatureReferenceExpression createFeatureReference(DEVersion lowerVersion, DEVersion upperVersion) {
		return createFeatureReference(lowerVersion, upperVersion, false);
	}
	
	protected static DEAbstractFeatureReferenceExpression createFeatureReference(DEVersion lowerVersion, DEVersion upperVersion, boolean conditional) {
		DEFeature feature = lowerVersion.getFeature();
		DEAbstractFeatureReferenceExpression featureReferenceExpression = createFeatureReference(feature, conditional);
		
		featureReferenceExpression.setVersionRestriction(createVersionRangeRestriction(lowerVersion, upperVersion));
		
		return featureReferenceExpression;
	}
	
	
	protected static DEVersionRangeRestriction createVersionRangeRestriction(DEVersion lowerVersion, DEVersion upperVersion) {
		return createVersionRangeRestriction(lowerVersion, upperVersion, true, true);
	}
	
	protected static DEVersionRangeRestriction createVersionRangeRestriction(DEVersion lowerVersion, DEVersion upperVersion, boolean lowerIncluded, boolean upperIncluded) {
		DEVersionRangeRestriction versionRangeRestriction = DEExpressionFactory.eINSTANCE.createDEVersionRangeRestriction();
		
		versionRangeRestriction.setLowerVersion(lowerVersion);
		versionRangeRestriction.setUpperVersion(upperVersion);
		
		versionRangeRestriction.setLowerIncluded(lowerIncluded);
		versionRangeRestriction.setUpperIncluded(upperIncluded);
		
		return versionRangeRestriction;
	}
	
	protected static DERelativeVersionRestriction createRelativeVersionRestriction(DERelativeVersionRestrictionOperator operator, DEVersion version) {
		DERelativeVersionRestriction relativeVersionRestriction = DEExpressionFactory.eINSTANCE.createDERelativeVersionRestriction();
		
		relativeVersionRestriction.setOperator(operator);
		relativeVersionRestriction.setVersion(version);
		
		return relativeVersionRestriction;
	}
	
	public DEFeatureModel getHfm() {
		return featureModel;
	}

	public DEFeature getTurtleBotFeature() {
		return turtleBotFeature;
	}

	public DEFeature getEngineFeature() {
		return engineFeature;
	}

	public DEFeature getMovementFeature() {
		return movementFeature;
	}

	public DEFeature getKeyboardFeature() {
		return keyboardFeature;
	}

	public DEFeature getGamepadFeature() {
		return gamepadFeature;
	}

	public DEFeature getAutonomousFeature() {
		return autonomousFeature;
	}

	public DEConstraintModel getConstraintModel() {
		return constraintModel;
	}

	public DEVersion getTurtleBot_1_0() {
		return turtleBot_1_0;
	}

	public DEVersion getTurtleBot_1_1() {
		return turtleBot_1_1;
	}

	public DEVersion getTurtleBot_2_0() {
		return turtleBot_2_0;
	}

	public DEVersion getTurtleBot_2_1() {
		return turtleBot_2_1;
	}

	public DEVersion getEngine_1_0() {
		return engine_1_0;
	}

	public DEVersion getEngine_1_1() {
		return engine_1_1;
	}

	public DEVersion getEngine_Create_1_2() {
		return engine_Create_1_2;
	}

	public DEVersion getEngine_Kobuki_1_0() {
		return engine_Kobuki_1_0;
	}

	public DEVersion getMovement_1_0() {
		return movement_1_0;
	}

	public DEVersion getMovement_1_1() {
		return movement_1_1;
	}

	public DEVersion getMovement_1_2() {
		return movement_1_2;
	}

	public DEVersion getMovement_2_0() {
		return movement_2_0;
	}

	public DEVersion getKeyboard_1_0() {
		return keyboard_1_0;
	}

	public DEVersion getGamepad_1_0() {
		return gamepad_1_0;
	}

	public DEVersion getGamepad_2_0() {
		return gamepad_2_0;
	}

	public DEVersion getAutonomous_1_0() {
		return autonomous_1_0;
	}

	public DEVersion getAutonomous_1_1() {
		return autonomous_1_1;
	}

	public DEVersion getAutonomous_2_0() {
		return autonomous_2_0;
	}

	public DEFeature getWebserviceFeature() {
		return webserviceFeature;
	}

	public DEVersion getWebservice_1_0() {
		return webservice_1_0;
	}

	public DEVersion getWebservice_1_1() {
		return webservice_1_1;
	}

	public DEFeature getDetectionFeature() {
		return detectionFeature;
	}

	public DEVersion getDetection_1_0() {
		return detection_1_0;
	}

	public DEVersion getDetection_1_1() {
		return detection_1_1;
	}

	public DEFeature getBumpFeature() {
		return bumpFeature;
	}

	public DEVersion getBump_1_0() {
		return bump_1_0;
	}

	public DEFeature getInfraredFeature() {
		return infraredFeature;
	}

	public DEVersion getInfrared_1_0() {
		return infrared_1_0;
	}

	public DEVersion getInfrared_2_0() {
		return infrared_2_0;
	}

	public DEVersion getInfrared_2_2() {
		return infrared_2_2;
	}

	public DEFeature getUltrasoundFeature() {
		return ultrasoundFeature;
	}

	public DEVersion getUltrasound_0_8() {
		return ultrasound_0_8;
	}

	public DEVersion getUltrasound_0_9() {
		return ultrasound_0_9;
	}

	public DEVersion getUltrasound_1_0() {
		return ultrasound_1_0;
	}

}
