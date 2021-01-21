SYNTAXDEF deshellscript
FOR <http://deltaecore.org/shellscript/1.0>
START DEShellScript

OPTIONS {
	reloadGeneratorModel = "false";
	usePredefinedTokens = "false";
	defaultTokenName = "NAME";
	overrideLaunchConfigurationDelegate = "false";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableDebugSupport = "true";

	//TEMP:
	disableLaunchSupport = "true";
	
	editorName = "Shell Script Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Shell Script (*.deshellscript)";
}

TOKENS {
	DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))*$;
	DEFINE ML_COMMENT $'/*'.*'*/'$;

	DEFINE LINEBREAK $('\r\n'|'\r'|'\n')$;
	DEFINE WHITESPACE $(' '|'\t'|'\f')$;
	DEFINE NAME $('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9')*$;
}

TOKENSTYLES {
	"NAME" COLOR #7f0055, BOLD;
	
	"SL_COMMENT" COLOR #008000, ITALIC;
	"ML_COMMENT" COLOR #008000, ITALIC;
}

RULES {
	DEShellScript ::= commands (";" !0 commands)*;
	
	DECommand ::= name[] #1 arguments*;
	
	DESwitchArgument ::= "-" name[];
	
	@SuppressWarnings(explicitSyntaxChoice)
	DEParameterArgument ::= "-" name[] #1 (values['"','"'] | values['\'','\'']) ("," #1 (values['"','"'] | values['\'','\'']))*;
}
