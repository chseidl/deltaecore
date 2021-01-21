package eu.vicci.ecosystem.goalstructuringnotation.gsn.delta.resolver;

import org.deltaecore.core.extension.resolution.DEAbstractIteratingDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;

public class GsnDomainModelElementIdentifierResolver extends DEAbstractIteratingDomainModelElementIdentifierResolver {

	@Override
	protected boolean matches(String identifier, EObject domainModelElement) {
		String id = getIdFromDomainModelElement(domainModelElement);
		
		if (id != null && id.equals(identifier)) {
			return true;
		}
		
		return false;
	}

	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		return getIdFromDomainModelElement(domainModelElement);
	}
	
	private String getIdFromDomainModelElement(EObject domainModelElement) {
		if (domainModelElement instanceof GSNModel) {
			return "";
		}
		
		if (domainModelElement instanceof GSNConcreteElement) {
			GSNConcreteElement element = (GSNConcreteElement) domainModelElement;
			return element.getId();
		}
		
		return null;
	}
}
