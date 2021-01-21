package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;

public class GSNElementDeleteCommand extends Command {
	private GSNElement element;
	
	//Internal state for undo.
	private GSNModel oldModel;
	
	private List<GSNConnection> oldOutgoingConnections;
	private List<GSNConnection> oldIncomingConnections;
	
//	private Map<GSNConnection, GSNConnectionType> oldConnectionTypes;
	private Map<GSNConnection, GSNElement> oldConnectionSourceElements;
	private Map<GSNConnection, GSNElement> oldConnectionTargetElements;
	
	public GSNElementDeleteCommand(GSNElement element) {
		this.element = element;
		
		oldOutgoingConnections = new ArrayList<GSNConnection>();
		oldIncomingConnections = new ArrayList<GSNConnection>();
		
//		oldConnectionTypes = new HashMap<GSNConnection, GSNConnectionType>();
		oldConnectionSourceElements = new HashMap<GSNConnection, GSNElement>();
		oldConnectionTargetElements = new HashMap<GSNConnection, GSNElement>();
	}

	@Override
	public void execute() {
		oldConnectionSourceElements.clear();
		oldConnectionTargetElements.clear();
		
		detachAndBackupOutgoingConnections();
		detachAndBackupIncomingSolvedByConnections();

		oldModel = element.getModel();
		element.setModel(null);
	}

	private void detachAndBackupIncomingSolvedByConnections() {
		oldIncomingConnections.clear();
		
		List<GSNConnection> incomingConnections = element.getIncomingConnections();
		oldIncomingConnections.addAll(incomingConnections);
		
		//Avoid concurrent modification by iterating the copied list.
		for (GSNConnection incomingConnection : oldIncomingConnections) {
			detachAndBackupConnection(incomingConnection);
		}
	}
	
	private void detachAndBackupOutgoingConnections() {
		oldOutgoingConnections.clear();
		
		List<GSNConnection> outgoingConnections = element.getOutgoingConnections();
		oldOutgoingConnections.addAll(outgoingConnections);
		
		//Avoid concurrent modification by iterating the copied list.
		for (GSNConnection outgoingConnection : oldOutgoingConnections) {
			detachAndBackupConnection(outgoingConnection);
		}
	}
	
	private void detachAndBackupConnection(GSNConnection connection) {
//		GSNConnectionType oldType = connection.getType();
		GSNElement oldSourceElement = connection.getSourceElement();
		GSNElement oldTargetElement = connection.getTargetElement();
		
//		oldConnectionTypes.put(connection, oldType);
		oldConnectionSourceElements.put(connection, oldSourceElement);
		oldConnectionTargetElements.put(connection, oldTargetElement);
		
		connection.setSourceElement(null);
		connection.setTargetElement(null);
		
		connection.setModel(null);
	}
	
	private void reattachOutgoingConnections() {
		for (GSNConnection oldOutgoingConnection : oldOutgoingConnections) {
			reattachConnection(oldOutgoingConnection);
		}
	}
	
	private void reattachIncomingConnections() {
		for (GSNConnection oldIncomingConnection : oldIncomingConnections) {
			reattachConnection(oldIncomingConnection);
		}
	}
	
	private void reattachConnection(GSNConnection connection) {
//		GSNConnectionType oldType = oldConnectionTypes.get(connection);
		GSNElement oldSourceElement = oldConnectionSourceElements.get(connection);
		GSNElement oldTargetElement = oldConnectionTargetElements.get(connection);

//		connection.setType(oldType);
		connection.setSourceElement(oldSourceElement);
		connection.setTargetElement(oldTargetElement);

		connection.setModel(oldModel);
	}
	
	@Override
	public void undo() {
		element.setModel(oldModel);
		
		reattachIncomingConnections();
		reattachOutgoingConnections();
	}
}