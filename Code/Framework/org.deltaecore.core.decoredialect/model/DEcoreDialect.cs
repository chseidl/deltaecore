SYNTAXDEF decoredialect
FOR <http://deltaecore.org/decoredialect/1.0>
START DEDeltaDialect

IMPORTS {
	@SuppressWarnings(unreachableRule)
	decorebase : <http://deltaecore.org/decorebase/1.0> WITH SYNTAX decorebase <../../org.deltaecore.core.decorebase/model/DEcoreBase.cs>
}

OPTIONS {
	reloadGeneratorModel = "false";
	generateCodeFromGeneratorModel = "true";
	usePredefinedTokens = "false";

	disableNewProjectWizard = "true";
	overrideLaunchConfigurationDelegate = "false";
	additionalDependencies = "org.deltaecore.core.generation;visibility:=reexport, de.christophseidl.util.swt;visibility:=reexport, de.christophseidl.util.eclipse.ui;visibility:=reexport";
	
	overrideNewFileWizard = "false";
	overrideNewFileWizardPage = "false";
	overrideBuilder = "false";
	
	editorName = "Delta Dialect Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Delta Dialect (*.decoredialect)";
}

RULES {
	DEDeltaDialect ::= "deltaDialect" !0 
	                    "{"
	                       !1 "configuration" ":"
	                          !2 "metaModel" ":" #1 domainPackage['<','>'] ";"
	                          (!2 "identifierResolver" ":" #1 domainModelElementIdentifierResolverClassReference ";")?
	                       !0
	                       !1 "deltaOperations" ":" 
	                           deltaOperationDefinitions* !0
	                    "}";
	
	
	DESetDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "setOperation" #1 name[IDENTIFIER_TOKEN] "(" value "," #1 element ")" ";";
	DEUnsetDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "unsetOperation" #1 name[IDENTIFIER_TOKEN] "(" element ")" ";";
	
	DEAddDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "addOperation" #1 name[IDENTIFIER_TOKEN] "(" value "," #1 element ")" ";";
	DEInsertDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "insertOperation" #1 name[IDENTIFIER_TOKEN] "(" value "," #1 element "," #1 index ")" ";";
	DERemoveDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "removeOperation" #1 name[IDENTIFIER_TOKEN] "(" value "," #1 element ")" ";";
	
	DEModifyDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "modifyOperation" #1 name[IDENTIFIER_TOKEN] "(" value "," #1 element ")" ";";
	
	DEDetachDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "detachOperation" #1 name[IDENTIFIER_TOKEN] "(" element ")" ";";
	
	DECustomDeltaOperationDefinition ::= !2 (modificationType[CONFIGURATION : "", EVOLUTION : "evolution"])? #1 "customOperation" #1 name[IDENTIFIER_TOKEN] "(" (declaredParameters ("," #1 declaredParameters)*)? ")" ";";
	
	
	DENamedParameter ::= type #1 name[IDENTIFIER_TOKEN];
	DEModelElementParameter ::= type #1 name[IDENTIFIER_TOKEN];
	DEModelElementWithReferenceParameter ::= type #1 "[" reference[IDENTIFIER_TOKEN] "]" #1 name[IDENTIFIER_TOKEN];
	DEModelElementWithAttributeParameter ::= type #1 "[" attribute[IDENTIFIER_TOKEN] "]" #1 name[IDENTIFIER_TOKEN];
}