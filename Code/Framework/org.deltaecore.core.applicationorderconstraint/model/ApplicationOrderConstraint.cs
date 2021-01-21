SYNTAXDEF deapplicationorderconstraints
FOR <http://deltaecore.org/applicationorderconstraint/1.0>
START DEApplicationOrderConstraintModel

IMPORTS {
	@SuppressWarnings(unreachableRule)
	decorebase : <http://deltaecore.org/decorebase/1.0> WITH SYNTAX decorebase <../../org.deltaecore.core.decorebase/model/DEcoreBase.cs>
}

OPTIONS {
	//Backward compatibility (March 2016)
	additionalFileExtensions = "aoc";
	
	reloadGeneratorModel = "false";
	usePredefinedTokens = "false";
	
	editorName = "Application-Order Constraints Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Application-Order Constraints (*.deapplicationorderconstraints)";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
}

RULES {
	DEApplicationOrderConstraintModel ::= (constrainedGroups !0) (constrainedGroups !0)+;
	
	DEConstrainedGroup ::= "[" !1 (constrainedDeltaPaths !0) ("," constrainedDeltaPaths !0)* "]";
}
