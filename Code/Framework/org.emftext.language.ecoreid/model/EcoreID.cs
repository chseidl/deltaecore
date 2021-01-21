SYNTAXDEF ecoreid
FOR <http://www.emftext.org/ecoreid/1.0>
START EcoreIDList

OPTIONS {
	reloadGeneratorModel = "false";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	
	additionalExports = "org.emftext.language.ecoreid.resource.ecoreid.util,org.emftext.language.ecoreid.resource.ecoreid.exception";
}

RULES {
	@SuppressWarnings(explicitSyntaxChoice)
	EcoreID ::= (names[] ".")* names[] ("(" ((explicitlyNoParametersSpecified["" : "..."]) | (parameterTypeNames[]  ("," #1 parameterTypeNames[])*)) ")")?;
	
	EcoreIDList ::= ecoreIDs ("," !0 ecoreIDs)*;
}