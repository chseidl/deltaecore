@SuppressWarnings(noRuleForMetaClass)
SYNTAXDEF decore
FOR <http://deltaecore.org/decore/1.0>
START DEDelta

IMPORTS {
	@SuppressWarnings(unreachableRule)
	decorebase : <http://deltaecore.org/decorebase/1.0> WITH SYNTAX decorebase <../../org.deltaecore.core.decorebase/model/DEcoreBase.cs>
}

OPTIONS {
	reloadGeneratorModel = "false";
	usePredefinedTokens = "false";
	
	additionalDependencies = "org.deltaecore.core.decoredialect.registry";
	additionalUIDependencies = "org.deltaecore.core.variant";
	disableLaunchSupport = "true";
	disableNewProjectWizard = "true";
	
	editorName = "Delta Module Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Delta Module (*.decore)";
	
	//NOTE: DecoreEditorCustom is registered manually in the plugin.xml file. If the file is ever overwritten, re-register the editor.
	overrideUIPluginXML = "false";
	overrideHoverTextProvider = "false";
	
	resolveProxyElementsAfterParsing = "false";
}

TOKENS {
	DEFINE BOOLEAN_LITERAL_TOKEN $('true'|'false')$;
	DEFINE INTEGER_LITERAL_TOKEN $('0')|(('-')?('1'..'9')('0'..'9')*)$;
	DEFINE DOUBLE_LITERAL_TOKEN $('-')?('0'..'9')+'.'('0'..'9')*$;
	DEFINE STRING_LITERAL_TOKEN $'"'(~('"')|('\\"'))*'"'$;
}

TOKENSTYLES {
	"BOOLEAN_LITERAL_TOKEN" COLOR #7F0055, BOLD;
	"INTEGER_LITERAL_TOKEN" COLOR #FF0000;
	"DOUBLE_LITERAL_TOKEN" COLOR #FF0000;
	"STRING_LITERAL_TOKEN" COLOR #2A00FF;
}

RULES {
	DEDelta ::= abstract["abstract" : ""] #1 (modificationType[CONFIGURATION : "configuration", EVOLUTION : "evolution", CUSTOMIZATION : "customization"] #1)? "delta" #1 name[STRING_LITERAL_TOKEN] !1 (blocks (!0!0 blocks)*)?;
					
	DEDeltaBlock ::= "dialect" #1 deltaDialect['<','>'] !1
                   ("requires" #1 requiredElementRelativeFilePaths ("," #1 requiredElementRelativeFilePaths)*)?
                   ("creates" #1 createdElementRelativeFilePaths ("," #1 createdElementRelativeFilePaths)*)?
                   ("modifies" #1 modifiedElementRelativeFilePaths ("," #1 modifiedElementRelativeFilePaths)*)?
	            "{"
	               (!0 statements)* !0
	            "}";
	
	DEDeltaOperationCall ::= operationDefinition[IDENTIFIER_TOKEN] "(" (arguments ("," #1 arguments)*)? ")" ";";
	DEArgument ::= expression;
	
	DEEEnumLiteral ::= enum[IDENTIFIER_TOKEN] "." enumLiteral[IDENTIFIER_TOKEN];
	
	DENullLiteral ::= "null";
	DEBooleanLiteral ::= value[BOOLEAN_LITERAL_TOKEN];
	DEIntegerLiteral ::= value[INTEGER_LITERAL_TOKEN];
	DEDoubleLiteral ::= value[DOUBLE_LITERAL_TOKEN];
	DEStringLiteral ::= value[STRING_LITERAL_TOKEN];
	
	DEModelElementIdentifier ::= rawIdentifier['<','>'];
	
	//Actually, this is a constant right now as there is no way to set the value after initialization.
	//As long as that is the case, the initializer is mandatory (at least in the syntax)!
	@SuppressWarnings(minOccurenceMismatch)
	DEVariableDeclaration ::= type name[IDENTIFIER_TOKEN] (#1 "=" #1 expression ) ";";
	
	DEVirtualConstructorCall ::= "new" #1 type #0 "(" #0 (namedArguments ("," #1 namedArguments)*)? #0 ")";
	DEStructuralFeatureReference ::= structuralFeature[IDENTIFIER_TOKEN] #0 ":" #1 expression;
	
	DEDataTypeInitializer ::= "new" #1 dataType[IDENTIFIER_TOKEN] #0 "(" #0 initializingValue[STRING_LITERAL_TOKEN] #0 ")";
	
	DEStandaloneExpressionStatement ::= expression #0 ";";
	DEVariableReference ::= variable[IDENTIFIER_TOKEN];
}