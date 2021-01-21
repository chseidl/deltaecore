@SuppressWarnings(styleReferenceToNonExistingToken)
SYNTAXDEF constraints
FOR <http://www.tu-bs.de/snowflake/constraint/1.0> <Constraint.genmodel>
START SConstraintModel

OPTIONS {
	reloadGeneratorModel = "true";
	generateCodeFromGeneratorModel = "true";
	usePredefinedTokens = "false";
	
	defaultTokenName = "IDENTIFIER_TOKEN";
}

TOKENS {
	DEFINE INTEGER_TOKEN $('0'..'9')+$;
	DEFINE DOUBLE_TOKEN $('0'..'9')+('.')('0'..'9')+$;
	DEFINE IDENTIFIER_TOKEN $('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'0'..'9')*$;
	DEFINE QUALIFIED_IDENTIFIER_TOKEN IDENTIFIER_TOKEN + $('.')$ + IDENTIFIER_TOKEN;
	
	DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))*$;
	DEFINE ML_COMMENT $'/*'.*'*/'$;
	
	DEFINE LINEBREAK $('\r\n'|'\r'|'\n')$;
	DEFINE WHITESPACE $(' '|'\t'|'\f')$;
}

TOKENSTYLES  {
	"SL_COMMENT", "ML_COMMENT" COLOR #008000;
	
	"INTEGER_TOKEN" COLOR #FF0000;
	"DOUBLE_TOKEN" COLOR #FF00FF;
	
	"<->", "->", /*"excludes",*/ "||", /*"xor",*/ "&&", "!",
	 "equivalent", "implies", "requires", "excludes", "or", "xor", "and", "not" COLOR #800040, BOLD;
	
	"?" COLOR #800040, BOLD;
	
	"<", "<=", "=", "!=", ">", ">=", "~=" COLOR #800040, BOLD;
	"[", "]", "(", ")" COLOR #0000CC;
}

RULES {
	SConstraintModel ::= "constraints" #1 "for" #1 featureModel['<','>'] !0!0 (constraints !0)*;

	SConstraint ::= rootExpression;
	
	@Operator(type="binary_left_associative", weight="2", superclass="SExpression")
	SImpliesExpression ::= operand1 "->" operand2;
	
	@Operator(type="binary_left_associative", weight="4", superclass="SExpression")
	SOrExpression ::= operand1 "||" operand2;
	
	@Operator(type="binary_left_associative", weight="6", superclass="SExpression")
	SAndExpression ::= operand1 "&&" operand2;
	
	@SuppressWarnings(explicitSyntaxChoice)
	@Operator(type="unary_prefix", weight="7", superclass="SExpression")
	SNotExpression ::= ("!" | "not") operand;
	
	@Operator(type="primitive", weight="8", superclass="SExpression")
	SNestedExpression ::= "(" operand ")";
	
	
	@SuppressWarnings(explicitSyntaxChoice)
	@Operator(type="primitive", weight="8", superclass="SExpression")
	SFeatureReferenceExpression ::= (feature['"', '"'] | feature[]);
	
	
	@SuppressWarnings(explicitSyntaxChoice)
	@Operator(type="primitive", weight="8", superclass="SExpression")
	SRelativeVersionRestriction ::= conditional["?" : ""] (feature['"', '"'] | feature[]) "[" operator[lessThan : "<", lessThanOrEqual : "<=", equal : "=", implicitEqual : "", greaterThanOrEqual : ">=", greaterThan : ">"] version['"','"'] "]";
	
	@SuppressWarnings(explicitSyntaxChoice)
	@Operator(type="primitive", weight="8", superclass="SExpression")
	SVersionRangeRestriction ::= conditional["?" : ""] (feature['"', '"'] | feature[]) "[" lowerIncluded["" : "^"] lowerVersion['"','"'] "-" upperIncluded["" : "^"] upperVersion['"','"'] "]";
}