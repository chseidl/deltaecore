@SuppressWarnings(noRuleForMetaClass)
SYNTAXDEF cfd_text
FOR <http://vicci.eu/cfd/1.0>
START CFDDiagram

OPTIONS {
	reloadGeneratorModel = "false";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
}

RULES {
	CFDDiagram ::= rootComponent;
	@SuppressWarnings(minOccurenceMismatch)
	CFDComponent ::= "component"
					 !0 "{"
					 	!1 "id" ":" #1 id[TEXT] ";"
					 	!1 "name" ":" #1 name['"', '"'] ";"
					 	(!1 "constraints" ":" #1 constraints['"','"'] ";")?
					 	(!1 "inPorts" ":" inPorts ("," #1 inPorts)* ";")?
					 	!1 "outPorts" ":" outPorts ("," #1 outPorts)* ";"
					 	(!0 elements+)?
					 	(!0 connections+)?
					 !0 "}";
	CFDInPort ::= "[" "in" "]" #1 name['"','"'];
	CFDOutPort ::= "[" "out" "]" #1 name['"','"'];
	
	CFDConnection ::= "connection" ":" sourcePort['<', '>'] "->" targetPort['<', '>'] ";";
}