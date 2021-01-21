SYNTAXDEF decorebase
FOR <http://deltaecore.org/decorebase/1.0>
//Irrelevant. The language is never instantiated as it merely serves as basis for others.
START DEMetaModelClassifierReference, DEBoolean, DEInteger, DEDouble, DEString, DEJavaClassReference, DERelativeFilePath

OPTIONS {
	reloadGeneratorModel = "false";
	generateCodeFromGeneratorModel = "true";
	usePredefinedTokens = "false";

	disableLaunchSupport = "true";
	disableNewProjectWizard = "true";
}

TOKENS {
	DEFINE WHITESPACE $(' '|'\t'|'\f')+$;
	DEFINE LINEBREAK $('\r\n'|'\r'|'\n')$;

	DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))*$;
	DEFINE ML_COMMENT $'/*'.*'*/'$;
	
	@SuppressWarnings(tokenOverlapping)
	DEFINE IDENTIFIER_TOKEN $('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'0'..'9'|'_'|'-')*$;
}

TOKENSTYLES {
	"SL_COMMENT" COLOR #008000, ITALIC;
	"ML_COMMENT" COLOR #008000, ITALIC;
}

RULES {
	DEMetaModelClassifierReference ::= classifier[IDENTIFIER_TOKEN];
	
	DEBoolean ::= "Boolean";
	DEInteger ::= "Integer";
	DEDouble ::= "Double";
	DEString ::= "String";
	
	DEJavaClassReference ::= (packageNameFragments[IDENTIFIER_TOKEN] ".")* classNameFragment[IDENTIFIER_TOKEN];
	
	DERelativeFilePath ::= rawRelativeFilePath['<','>'];
}