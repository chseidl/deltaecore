SYNTAXDEF expression
FOR <http://deltaecore.org/feature/expression/1.0> <Expression.genmodel>
START DEExpression

OPTIONS {
	reloadGeneratorModel = "false";
	usePredefinedTokens = "false";
	
	defaultTokenName = "IDENTIFIER_TOKEN";
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
}

TOKENS {
	DEFINE IDENTIFIER_TOKEN $('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'0'..'9')*$;
	
	DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))*$;
	DEFINE ML_COMMENT $'/*'.*'*/'$;
	
	DEFINE LINEBREAK $('\r\n'|'\r'|'\n')$;
	DEFINE WHITESPACE $(' '|'\t'|'\f')$;
}

TOKENSTYLES  {
	"SL_COMMENT", "ML_COMMENT" COLOR #008000;
		
	"<->", "->", "||", "&&", "!" COLOR #800040, BOLD;
	
	//"^" COLOR #800040, BOLD;
	"?" COLOR #800040, BOLD;
	
	"<", "<=", "=", ">", ">=" COLOR #800040, BOLD;
	"[", "]", "(", ")" COLOR #0000CC;
}

RULES {
	@Operator(type="binary_left_associative",  weight="1", superclass="DEExpression")
	DEEquivalenceExpression ::= operand1 #1 "<->" #1 operand2;
	
	@Operator(type="binary_left_associative", weight="2", superclass="DEExpression")
	DEImpliesExpression ::= operand1 #1 "->" #1 operand2;
	
	@Operator(type="binary_left_associative", weight="3", superclass="DEExpression")
	DEOrExpression ::= operand1 #1 "||" #1 operand2;
	
	@Operator(type="binary_left_associative", weight="4", superclass="DEExpression")
	DEAndExpression ::= operand1 #1 "&&" #1 operand2;
	
	@Operator(type="unary_prefix", weight="5", superclass="DEExpression")
	DENotExpression ::= "!" operand;
	
	@Operator(type="primitive", weight="6", superclass="DEExpression")
	DENestedExpression ::= "(" operand ")";
	
	
	@SuppressWarnings(explicitSyntaxChoice)
	@Operator(type="primitive", weight="6", superclass="DEExpression")
	DEFeatureReferenceExpression ::= (feature['"', '"'] | feature[]) (versionRestriction)?;
	
	@SuppressWarnings(explicitSyntaxChoice)
	@SuppressWarnings(minOccurenceMismatch)
	@Operator(type="primitive", weight="6", superclass="DEExpression")
	DEConditionalFeatureReferenceExpression ::= "?" (feature['"', '"'] | feature[]) versionRestriction;
	
	@Operator(type="primitive", weight="6", superclass="DEExpression")
	DEBooleanValueExpression ::= value["true" : "false"];
	
	DERelativeVersionRestriction ::= "[" operator[lessThan : "<", lessThanOrEqual : "<=", equal : "=", implicitEqual : "", greaterThanOrEqual : ">=", greaterThan : ">"] #1 version['"','"'] "]";
	DEVersionRangeRestriction ::= "[" lowerIncluded["" : "^"] lowerVersion['"','"'] #1 "-" #1 upperIncluded["" : "^"] upperVersion['"','"'] "]";
}