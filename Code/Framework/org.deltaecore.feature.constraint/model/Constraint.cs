SYNTAXDEF deconstraints
FOR <http://deltaecore.org/feature/constraint/1.0>
START DEConstraintModel

IMPORTS {
	expression : <http://deltaecore.org/feature/expression/1.0> WITH SYNTAX expression <../../org.deltaecore.feature.expression/model/Expression.cs>
}

OPTIONS {
	//Backward compatibility (March 2016)
	additionalFileExtensions = "constraints";
	
	reloadGeneratorModel = "false";
	usePredefinedTokens = "false";
	
	editorName = "Constraints Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Feature Model Constraints (*.deconstraints)";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
}

RULES {
	DEConstraintModel ::= (constraints !0)*;

	DEConstraint ::= rootExpression;
}