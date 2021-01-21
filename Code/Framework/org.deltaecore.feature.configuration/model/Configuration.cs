SYNTAXDEF deconfiguration
FOR <http://deltaecore.org/feature/configuration/1.0>
START DEConfiguration

OPTIONS {
	//Backward compatibility (March 2016)
	additionalFileExtensions = "configuration";
	
	reloadGeneratorModel = "true";
	
	editorName = "Configuration Editor (DeltaEcore)";
	newFileWizardCategory = "org.deltaecore.newwizards.Category";
	newFileWizardName = "DeltaEcore Configuration (*.deconfiguration)";
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
}

TOKENS {
	DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))*$;
	DEFINE ML_COMMENT $'/*'.*'*/'$;
}

TOKENSTYLES {
	"SL_COMMENT" COLOR #008000, ITALIC;
	"ML_COMMENT" COLOR #008000, ITALIC;
}


RULES {
	@SuppressWarnings(minOccurenceMismatch)
	DEConfiguration ::= "configuration" #1 featureModel['<','>'] "{" !1 configurationArtifacts ("," !0 configurationArtifacts)* !0 "}";
	
	@SuppressWarnings(explicitSyntaxChoice)
	DEFeatureSelection ::= (feature['"', '"'] | feature[]);
	
	@SuppressWarnings(explicitSyntaxChoice)
	@SuppressWarnings(minOccurenceMismatch)
	DEVersionSelection ::= (feature['"', '"'] | feature[]) "@" version['"','"'];
}