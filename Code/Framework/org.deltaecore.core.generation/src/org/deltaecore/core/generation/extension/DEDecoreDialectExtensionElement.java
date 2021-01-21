package org.deltaecore.core.generation.extension;

import org.deltaecore.core.extension.DEStaticDeltaDialectExtension;
import org.deltaecore.core.generation.general.DENameGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.christophseidl.util.eclipse.resourcehandlers.pluginxml.ExtensionElement;

public class DEDecoreDialectExtensionElement extends ExtensionElement {
	private DENameGenerator nameGenerator;
	
	public DEDecoreDialectExtensionElement(DENameGenerator nameGenerator) {
		super(DEStaticDeltaDialectExtension.EXTENSION_POINT_ID, null, null, null);
		
		this.nameGenerator = nameGenerator;
	}
	
	//Override content creation of node.
	@Override
	protected void appendContent(Element element, Document xmlDocument) {
		//E.g.,
		//  <extension point="org.deltaecore.deltadialect">
		//	    <dialect
		//	          interpreter="eu.vicci.ecosystem.sft.delta.SFTDeltaDialectInterpreter"
		//	          model="model/SFT.decoredialect">
		//	    </dialect>
		//	 </extension>
		
		Element dialectElement = xmlDocument.createElement("dialect");
		element.appendChild(dialectElement);
		
		String deltaDialectInterpreterQualifiedClassName = nameGenerator.getDeltaDialectInterpreterQualifiedClassName();
		String dialectRelativeFilename = nameGenerator.getDialectRelativeFilename();
		String domainModelElementIdentifierResolverQualifiedClassName = nameGenerator.getDomainModelElementIdentifierResolverQualifiedClassName();
		
		addAttribute("interpreter", deltaDialectInterpreterQualifiedClassName, dialectElement, xmlDocument);
		addAttribute("model", dialectRelativeFilename, dialectElement, xmlDocument);
		
		if (domainModelElementIdentifierResolverQualifiedClassName != null) {
			addAttribute("domainModelElementIdentifierResolver", domainModelElementIdentifierResolverQualifiedClassName, dialectElement, xmlDocument);
		}
	}
}
