package eu.vicci.ecosystem.goalstructuringnotation.gsn.delta;

import java.util.List;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNFactory;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNPackage;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class GsnDeltaDialectInterpreter extends GsnAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretConnect(DEModelWriter modelWriter, GSNConnectionType type, GSNConcreteElement source, GSNConcreteElement target, GSNModel parentModel) {
		GSNConnection connection = GSNFactory.eINSTANCE.createGSNConnection();
		
		connection.setType(type);
		connection.setSourceElement(source);
		connection.setTargetElement(target);
		
		modelWriter.addValue(parentModel, GSNPackage.GSN_MODEL__CONNECTIONS, connection);
		
		return true;
	}

	@Override
	protected boolean interpretDisconnect(DEModelWriter modelWriter, GSNConcreteElement source, GSNConcreteElement target, GSNModel parentModel) {
		GSNConnection connection = findConnection(source, target, parentModel);
		
		if (connection == null) {
			return false;
		}
		
		//Reset connection ends (will trigger resetting opposite of connected elements).
		modelWriter.unsetValue(connection, GSNPackage.GSN_CONNECTION__SOURCE_ELEMENT);
		modelWriter.unsetValue(connection, GSNPackage.GSN_CONNECTION__TARGET_ELEMENT);
		
		//Remove from containment reference.
		modelWriter.removeValue(parentModel, GSNPackage.GSN_MODEL__CONNECTIONS, connection);
		
		return true;
	}
	
	private static GSNConnection findConnection(GSNConcreteElement source, GSNConcreteElement target, GSNModel parentModel) {
		List<GSNConnection> connections = parentModel.getConnections();
		
		for (GSNConnection connection : connections) {
			GSNConcreteElement sourceElement = (GSNConcreteElement) connection.getSourceElement();
			GSNConcreteElement targetElement = (GSNConcreteElement) connection.getTargetElement();
			
			if (matchesById(sourceElement, source) && matchesById(targetElement, target)) {
				return connection;
			}
		}
		
		return null;
	}
	
	private static boolean matchesById(GSNConcreteElement element1, GSNConcreteElement element2) {
		String id1 = element1.getId();
		String id2 = element2.getId();
		
		if (id1 == id2 || (id1 != null && id1.equals(id2))) {
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean interpretRelocateElement(DEModelWriter modelWriter, GSNConcreteElement concreteElement, Integer x, Integer y) {
		Rectangle constraints = ensureConstraints(modelWriter, concreteElement);
		
		constraints.x = x;
		constraints.y = y;
		
		return true;
	}
	
	@Override
	protected boolean interpretResizeElement(DEModelWriter modelWriter, GSNConcreteElement concreteElement, Integer width, Integer height) {
		Rectangle constraints = ensureConstraints(modelWriter, concreteElement);
		
		constraints.width = width;
		constraints.height = height;
		
		return true;
	}
	
	private Rectangle ensureConstraints(DEModelWriter modelWriter, GSNConcreteElement concreteElement) {
		Rectangle constraints = concreteElement.getConstraints();
		
		//This is an interesting case as it is a hole in the model writer structure:
		//When the constraint already exists, it can be set without the model writer noticing, can't it?
		//TODO: Investigate and fix possible hole in model writer structure!
		
		if (constraints == null) {
			constraints = new Rectangle();
			modelWriter.setValue(concreteElement, GSNPackage.GSN_CONCRETE_ELEMENT__CONSTRAINTS, constraints);
		}
		
		return constraints;
	}
}