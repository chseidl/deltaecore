package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory;

import java.security.InvalidParameterException;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.util.GSNConstructor;

public class GSNElementCreationFactory implements CreationFactory {

	private GSNElementType type;
	
	public GSNElementCreationFactory(GSNElementType type) {
		this.type = type;
	}
	
	@Override
	public Object getNewObject() {
		
		switch (type.getValue()) {
			case GSNElementType.CONTEXT_VALUE:
				return GSNConstructor.createContext();
			case GSNElementType.GOAL_VALUE:
				return GSNConstructor.createGoal();
			case GSNElementType.SOLUTION_VALUE:
				return GSNConstructor.createSolution();
			case GSNElementType.STRATEGY_VALUE:
				return GSNConstructor.createStrategy();
			case GSNElementType.ASSUMPTION_VALUE:
				return GSNConstructor.createAssumption();
			case GSNElementType.JUSTIFICATION_VALUE:
				return GSNConstructor.createJustification();
		}

		throw new InvalidParameterException();
	}

	@Override
	public Object getObjectType() {
		return GSNConcreteElement.class;
	}
}
