SYNTAXDEF cl_text
FOR <http://vicci.eu/cl/1.0>
START CLChecklist

OPTIONS {
	reloadGeneratorModel = "false";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
}

RULES {
	CLChecklist ::= "checklist" (#1 name['"','"'])? !0!0 groups+;
	CLChecklistItemGroup ::= "group" #1 title['"','"'] items+ !0!0;
	CLChecklistItem ::= !1 checked["x" : ""] #1 id[TEXT] #1 title['"','"'];
}