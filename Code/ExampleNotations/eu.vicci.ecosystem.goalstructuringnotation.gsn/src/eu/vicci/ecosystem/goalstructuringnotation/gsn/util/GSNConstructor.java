package eu.vicci.ecosystem.goalstructuringnotation.gsn.util;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNFactory;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;

public class GSNConstructor {
	private static GSNModel defaultModel = null;
	private static String defaultId = "0";
	private static String defaultDescription = "";
	private static boolean defaultAway = false;
	
	public static GSNConcreteElement createContext() {
		return createConcreteElement(GSNElementType.CONTEXT);
	}

	public static GSNConcreteElement createGoal() {
		return createConcreteElement(GSNElementType.GOAL);
	}
	
	public static GSNConcreteElement createSolution() {
		return createConcreteElement(GSNElementType.SOLUTION);
	}
	
	public static GSNConcreteElement createStrategy() {
		return createConcreteElement(GSNElementType.STRATEGY);
	}
	
	public static GSNConcreteElement createAssumption() {
		return createConcreteElement(GSNElementType.ASSUMPTION);
	}
	
	public static GSNConcreteElement createJustification() {
		return createConcreteElement(GSNElementType.JUSTIFICATION);
	}
	
	public static GSNConcreteElement createConcreteElement(GSNElementType type) {
		return createElement(defaultModel, type, defaultId, defaultDescription, defaultAway);
	}
	
	public static GSNConcreteElement createElement(GSNModel model, GSNElementType type, String id, String description, boolean away) {
		GSNConcreteElement element = GSNFactory.eINSTANCE.createGSNConcreteElement();

		element.setModel(model);
		element.setType(type);
		element.setId(id);
		element.setDescription(description);
		element.setAway(away);
		
		return element;
	}
	
	
	public static GSNConnection createSolvedByConnection() {
		return createConnection(GSNConnectionType.SOLVED_BY);
	}
	
	public static GSNConnection createInContextOfConnection() {
		return createConnection(GSNConnectionType.IN_CONTEXT_OF);	
	}
	
	public static GSNConnection createConnection(GSNConnectionType type) {
		return createConnection(null, type, null, null);
	}
	
	public static GSNConnection createConnection(GSNModel model, GSNConnectionType type, GSNElement sourceElement, GSNElement targetElement) {
		GSNConnection connection = GSNFactory.eINSTANCE.createGSNConnection();
		
		connection.setModel(model);
		connection.setType(type);
		connection.setSourceElement(sourceElement);
		connection.setTargetElement(targetElement);
		
		return connection;
	}
}
