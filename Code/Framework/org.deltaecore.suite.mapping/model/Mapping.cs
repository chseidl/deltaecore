SYNTAXDEF demapping
FOR <http://deltaecore.org/feature/mapping/1.0>
START DEMappingModel

IMPORTS {
	expression : <http://deltaecore.org/feature/expression/1.0> WITH SYNTAX expression <../../org.deltaecore.feature.expression/model/Expression.cs>
}

OPTIONS {
	//Backward compatibility (March 2016)
	additionalFileExtensions = "mapping";
	
	reloadGeneratorModel = "false";
	usePredefinedTokens = "false";
	
	editorName = "Feature Mapping Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Feature Mapping (*.demapping)";
	
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
	disableBuilder = "true";
	disableNewProjectWizard = "true";
}

RULES {
	//Explicitly allow that there are no mappings, e.g., for generated empty mapping files.
	DEMappingModel ::= (mappings (!0!0 mappings)*)?;
	
	DEDeltaInvokation ::= delta['<','>'];
	DEMapping ::= expression #1 ":" !1 deltaInvokations ("," !0 deltaInvokations)*;
}