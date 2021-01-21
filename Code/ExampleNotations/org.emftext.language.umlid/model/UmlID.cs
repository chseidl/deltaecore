SYNTAXDEF umlid
FOR <http://www.emftext.org/umlid/1.0>
START UmlIDList

OPTIONS {
	reloadGeneratorModel = "false";
	
	additionalDependencies = "org.eclipse.uml2.uml";
	additionalExports = "org.emftext.language.umlid.resource.umlid;org.emftext.language.umlid.resource.umlid.util";
		
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
}

RULES {
	@SuppressWarnings(explicitSyntaxChoice)
	UmlIDList ::= ids ("," !0 ids)*;
	
	UmlModelID ::= "model" "::";
	
	UmlPackageID ::= "package" "::" packageNames[] ("." packageNames[])*;
	
	UmlClassID ::= "class" "::" packageNames[] ("." packageNames[])* "." classifierName[];
	UmlInterfaceID ::= "interface" "::" packageNames[] ("." packageNames[])* "." classifierName[];
	
	UmlOperationID ::= "operation" "::" packageNames[] ("." packageNames[])* "." classifierName[] "#" memberName[];
	//TODO: Parameters
	
	UmlPropertyID ::= "property" "::" packageNames[] ("." packageNames[])* "." classifierName[] "#" memberName[];
}