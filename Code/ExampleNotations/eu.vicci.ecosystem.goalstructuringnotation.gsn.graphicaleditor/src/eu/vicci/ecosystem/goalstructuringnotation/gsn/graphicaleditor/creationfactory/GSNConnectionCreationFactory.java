package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory;

import java.security.InvalidParameterException;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.util.GSNConstructor;

public class GSNConnectionCreationFactory implements CreationFactory {

	private GSNConnectionType type;
	
	public GSNConnectionCreationFactory(GSNConnectionType type) {
		this.type = type;
	}
	
	@Override
	public Object getNewObject() {
		switch (type.getValue()) {
			case GSNConnectionType.SOLVED_BY_VALUE:
				return GSNConstructor.createSolvedByConnection();
			case GSNConnectionType.IN_CONTEXT_OF_VALUE:
				return GSNConstructor.createInContextOfConnection();
		}

		throw new InvalidParameterException();
	}

	@Override
	public Object getObjectType() {
		return GSNConnection.class;
	}

}
