SYNTAXDEF xml
FOR <http://www.emftext.org/DocBook>
START BookNode

OPTIONS {
	reloadGeneratorModel = "false";
	caseInsensitiveKeywords = "true";
	usePredefinedTokens = "false";
	
	defaultTokenName = "TEXT_CONTENT_TOKEN";
	
	editorName = "DocBook Editor (EMFText)";
	newFileWizardName = "DocBook Documentation (*.xml)";
		
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
}

TOKENS {
	DEFINE COMMENT $'<!--'.*'-->'$;
	
	DEFINE FRAGMENT LETTER $('A'..'Z'|'a'..'z')$; //TODO: Complete this
	DEFINE FRAGMENT DIGIT $('0'..'9')$;
	DEFINE FRAGMENT SYMBOL $('_'|'\''|'.'|':'|','|'('|')')$; //TODO: Complete this
	DEFINE FRAGMENT CHARACTER $($ + LETTER + $|$ + DIGIT + $|$ + SYMBOL + $)$;
	
	
	//TODO: Parsing of content in simple nodes causes headaches. Fix dirty hacks!
	//TODO: Should be possible to also start with whitespace
	DEFINE TEXT_CONTENT_TOKEN $'>'~(' '|'\t'|'\f'|'\r\n'|'\r'|'\n'|'<'|'>')(~('<'|'>'))*'</'$;

	DEFINE LINEBREAK $('\r\n'|'\r'|'\n')$;
	
	DEFINE WHITESPACE $(' '|'\t'|'\f')$;
}

TOKENSTYLES  {
	"COMMENT" COLOR #008000;
}

RULES {
	//Complex Nodes
	BookNode ::= "<" #0 "book" (#1 attributes)* #0 ">" !1
				childNodes+
			 "</" #0 "book" #0 ">" !0;
	
	ChapterNode ::= "<" #0 "chapter" (#1 attributes)* #0 ">" !1
					childNodes+
				"</" #0 "chapter" #0 ">" !0;

	SectionNode ::= "<" #0 "section" (#1 attributes)* #0 ">" !1
					childNodes+
				"</"#0  "section" #0 ">" !0;
	
	//Simple Nodes
	@SuppressWarnings(explicitSyntaxChoice)
	TitleNode ::= "<" #0 "title" #0 (value[] | (">" "</")) #0 "title" #0 ">" !0;
	
	@SuppressWarnings(explicitSyntaxChoice)
	ParagraphNode ::= "<" #0 "para" #0 (value[] | (">" "</")) #0 "para" #0 ">" !0;
				
	//Attributes
	IDAttribute ::= "id" #1 "=" #1 value['"','"','\\'];
}
