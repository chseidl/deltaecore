package org.deltaecore.core.variant.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deltaecore.core.applicationorderconstraint.DEApplicationOrderConstraintModel;
import org.deltaecore.core.applicationorderconstraint.DEConstrainedGroup;
import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decorebase.DEModificationType;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class DEDependencyGraph {
	private Map<DEDelta, DEDependencyGraphNode> deltaToNodeMap;
	
	public DEDependencyGraph(List<DEDelta> deltas) {
		deltaToNodeMap = new HashMap<DEDelta, DEDependencyGraphNode>();
		initializeNodes(deltas);
	}
	
	private void initializeNodes(List<DEDelta> deltas) {
		//Create nodes for all deltas
		for (DEDelta delta : deltas) {
			DEDependencyGraphNode node = new DEDependencyGraphNode(delta);
			deltaToNodeMap.put(delta, node);
		}
		
		//Initialize each node with the requirements of the delta
		//after the respective nodes have been created.
		for (DEDelta delta : deltas) {
			List<DEDelta> requiredDeltas = DEDEcoreResolverUtil.getRequiredDeltas(delta);
			
			for (DEDelta requiredDelta : requiredDeltas) {
				addDependency(delta, requiredDelta, DEDependencyReason.REQUIREMENT);
			}
			
			//Customization delta modules implicitly have a requires edge to all other non-customization delta modules
			if (delta.getModificationType() == DEModificationType.CUSTOMIZATION) {
				
				Set<DEDelta> allDeltas = deltaToNodeMap.keySet();
				
				for (DEDelta otherDelta : allDeltas) {
					if (otherDelta.getModificationType() != DEModificationType.CUSTOMIZATION) {
						addDependency(delta, otherDelta, DEDependencyReason.CUSTOMIZATION_DELTA_MODULE);
					}
				}
			}
		}
	}
	
	public void processApplicationOrderConstraints(DEApplicationOrderConstraintModel aocModel) {
		if (aocModel == null) {
			return;
		}
		
		List<DEConstrainedGroup> constrainedGroups = aocModel.getConstrainedGroups();
		List<DEDelta> deltasOfAllPreviousConstrainedGroup = new ArrayList<DEDelta>();

		for (DEConstrainedGroup constrainedGroup : constrainedGroups) {
			List<DERelativeFilePath> constrainedDeltaPaths = constrainedGroup.getConstrainedDeltaPaths();
			
			List<DEDelta> deltasOfThisConstrainedGroup = new ArrayList<DEDelta>();
			
			for (DERelativeFilePath constrainedDeltaPath : constrainedDeltaPaths) {
				DEDelta delta = getDeltaFromRelativePath(constrainedDeltaPath);
				
				for (DEDelta deltaOfPreviousConstrainedGroup : deltasOfAllPreviousConstrainedGroup) {
					addOrderConstraint(deltaOfPreviousConstrainedGroup, delta);
				}
				
				deltasOfThisConstrainedGroup.add(delta);
			}
			
			deltasOfAllPreviousConstrainedGroup.addAll(deltasOfThisConstrainedGroup);
		}
	}
	
	private void addOrderConstraint(DEDelta beforeDelta, DEDelta afterDelta) {
		DEDependencyGraphNode beforeNode = getNodeForDelta(beforeDelta);
		DEDependencyGraphNode afterNode = getNodeForDelta(afterDelta);
		
		//Only do this if both nodes are present.
		if (beforeNode != null && afterNode != null) {
			afterNode.addDependencyOnNode(beforeNode, DEDependencyReason.APPLICATION_ORDER_CONSTRAINT);
		}
	}
	
	private void addDependency(DEDelta requiringDelta, DEDelta requiredDelta, DEDependencyReason reason) {
		DEDependencyGraphNode requiringNode = getNodeForDelta(requiringDelta);
		DEDependencyGraphNode requiredNode = getNodeForDelta(requiredDelta);
		
		if (requiredNode == null) {
			//Not all required deltas are in list.
			throw new UnsupportedOperationException("List of deltas cannot be sorted as not all required deltas are in the list. Delta \"" + requiringDelta.getName() + "\" is missing required delta \"" + requiredDelta.getName() + "\".");
		}
			
		requiringNode.addDependencyOnNode(requiredNode, reason);
	}
	
	private DEDependencyGraphNode getNodeForDelta(DEDelta delta) {
		Set<DEDelta> keys = deltaToNodeMap.keySet();
		DEDelta similarDelta = DEDEcoreBaseUtil.findSimilar(delta, keys);
		
		if (similarDelta != null) {
			return deltaToNodeMap.get(similarDelta);
		}
		
		return null;
	}
	
	private DEDelta getDeltaFromRelativePath(DERelativeFilePath relativeDeltaPath) {
		//Make path absolute
		Resource pathResource = relativeDeltaPath.eResource();
		URI pathResourceURI = pathResource.getURI();
		String rawRelativeDeltaPath = relativeDeltaPath.getRawRelativeFilePath();
		URI rawRelativeDeltaPathURI = URI.createURI(rawRelativeDeltaPath);
		URI rawAbsoluteDeltaPathURI = rawRelativeDeltaPathURI.resolve(pathResourceURI);
		
		Set<DEDelta> deltas = deltaToNodeMap.keySet();
		
		for (DEDelta delta : deltas) {
			Resource deltaResource = delta.eResource();
			URI deltaURI = deltaResource.getURI();
			
			if (deltaURI.equals(rawAbsoluteDeltaPathURI)) {
				return delta;
			}
		}
		
		return null;
	}
	
	public void reset() {
		for (DEDependencyGraphNode node : deltaToNodeMap.values()) {
			node.reset();
		}
	}
	
	public boolean hasUnprocessedNodes() {
		for (DEDependencyGraphNode node : deltaToNodeMap.values()) {
			if (node.isActive()) {
				return true;
			}
		}
		
		return false;
	}
	
	private List<DEDependencyGraphNode> findNodesWithoutRequirements() {
		List<DEDependencyGraphNode> nodesWithoutRequirements = new ArrayList<DEDependencyGraphNode>();
		
		for (DEDependencyGraphNode node : deltaToNodeMap.values()) {
			if (node.isActive() && !node.hasActiveDependencies()) {
				nodesWithoutRequirements.add(node);
			}
		}
		
		return nodesWithoutRequirements;
	}
	
	/**
	 * This removes the nodes of the current level. and dependencies to them.
	 */
	public List<DEDelta> placeDeltasOfCurrentLevel() {
		//Determine all nodes that do not have any (more) requirements.
		List<DEDependencyGraphNode> nodesWithoutRequirements = findNodesWithoutRequirements();
		List<DEDelta> deltasOfCurrentLevel = new ArrayList<DEDelta>();
		
		//Remove all these nodes from the nodes to be processed and collect the delta modules.
		for (DEDependencyGraphNode nodeWithoutRequirements : nodesWithoutRequirements) {
			DEDelta delta = nodeWithoutRequirements.getDelta();
			nodeWithoutRequirements.deactivate();
			deltasOfCurrentLevel.add(delta);
		}
		
		//Inform all other nodes that the nodes of this cycle are no longer required (as they are placed).
		for (DEDependencyGraphNode otherNode : deltaToNodeMap.values()) {
			otherNode.deactiveDependenciesOnNodes(nodesWithoutRequirements);
		}
		
		return deltasOfCurrentLevel;
	}
	
	/**
	 * @Deprecated For debug purposes only.
	 */
	@Deprecated
	public String createInternalStatusMessage() {
		//Debug output
		String message = "";
		message += "\n";
		message += "Remaining nodes and requirements:" + "\n";
		
		for (DEDependencyGraphNode remainingNode : deltaToNodeMap.values()) {
			DEDelta remainingDelta = remainingNode.getDelta();
			message += remainingDelta.getName()  + " (" + remainingDelta.eResource().getURI() + "): ";
			
			List<DEDependencyGraphNode> requiredNodes = remainingNode.getRequiredNodes();
			boolean isFirst = true;
			
			for (DEDependencyGraphNode requiredNode : requiredNodes) {
				if (!isFirst) {
					message += ", ";
				}
				
				Object data = requiredNode.getDelta();
				
				if (data instanceof DEDelta) {
					DEDelta requiredDelta = (DEDelta) data;
					
					message += requiredDelta.getName() + " (" + requiredDelta.eResource().getURI() + ")";
				} else if (data instanceof EObject) {
					EObject requiredEObject = (EObject) data;
					message += requiredEObject.eResource().getURI();
				} else {
					message += data;
				}
				
				isFirst = false;
			}
			
			message += "\n";
		}
		message += "\n";
		
		return message;
	}
}
