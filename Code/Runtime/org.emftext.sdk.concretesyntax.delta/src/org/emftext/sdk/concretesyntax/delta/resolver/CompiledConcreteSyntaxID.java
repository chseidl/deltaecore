package org.emftext.sdk.concretesyntax.delta.resolver;

public class CompiledConcreteSyntaxID {
	private String elementType;
	private String elementIdentifier;
	
	public CompiledConcreteSyntaxID(String elementType, String elementIdentifier) {
		this.elementType = elementType;
		this.elementIdentifier = elementIdentifier;
	}
	
	public String getElementType() {
		return elementType;
	}
	
	public String getElementIdentifier() {
		return elementIdentifier;
	}
}
