package org.deltaecore.core.variant.dependency;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;

public class DEDependencyGraphNode {
	private DEDelta delta;
	private List<DEDependency> dependencies;
	private boolean active;
	
	public DEDependencyGraphNode(DEDelta delta) {
		this.delta = delta;
		dependencies = new ArrayList<DEDependency>();
		active = true;
	}
	
	public void addDependencyOnNode(DEDependencyGraphNode requiredNode, DEDependencyReason reason) {
		dependencies.add(new DEDependency(requiredNode, reason));
	}
	
	public void deactiveDependenciesOnNodes(List<DEDependencyGraphNode> requiredNodes) {
		for (DEDependency dependency : dependencies) {
			DEDependencyGraphNode target = dependency.getTarget();
			
			if (requiredNodes.contains(target)) {
				dependency.deactivate();
			}
		}
	}
	
	public boolean hasActiveDependencies() {
		for (DEDependency dependency : dependencies) {
			if (dependency.isActive()) {
				return true;
			}
		}
		return false;
	}

	public void deactivate() {
		active = false;
	}
	
	public void reset() {
		for (DEDependency dependency : dependencies) {
			dependency.reset();
		}
		
		active = true;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public DEDelta getDelta() {
		return delta;
	}
	
	/**
		@Deprecated For Debug only!
	*/
	@Deprecated
	public List<DEDependencyGraphNode> getRequiredNodes() {
		List<DEDependencyGraphNode> requiredNodes = new ArrayList<DEDependencyGraphNode>();
		
		for (DEDependency dependency : dependencies) {
			DEDependencyGraphNode target = dependency.getTarget();
			requiredNodes.add(target);
		}
		
		return requiredNodes;
	}

	/**
	@Deprecated For Debug only!
	*/
	@Deprecated
	public List<DEDependency> getDependencies() {
		return dependencies;
	}
}
