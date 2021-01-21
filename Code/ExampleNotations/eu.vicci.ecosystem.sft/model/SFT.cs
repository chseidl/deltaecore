SYNTAXDEF sft_text
FOR <http://vicci.eu/sft/1.0>
START SFTSoftwareFaultTree

OPTIONS {
	reloadGeneratorModel = "false";
	
	disableNewProjectWizard = "true";
	disableBuilder = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
}

TOKENS {
	DEFINE PROBABILITY $('0''.'('0'..'9')+)$;
}

RULES {
	SFTSoftwareFaultTree ::= "softwareFaultTree" #1 name['"','"'] #1 "{" !1 rootFault !0 "}";
	SFTBasicFault ::= "basicFault" #1 "{" !1 "id" #0 ":" #1 id[TEXT] !0 "name" #0 ":" #1 name['"','"'] !0 ("description" #0 ":" #1 description['"','"'] !0)? "probability" #0 ":" #1 probability[PROBABILITY] !0 "}";
	SFTIntermediateFault ::= "intermediateFault" #1 "{" !1 "id" #0 ":" #1 id[TEXT] !0 "name" #0 ":" #1 name['"','"'] !0 ("description" #0 ":" #1 description['"','"'] !0)? gate !0 "}";
	SFTGate ::= "gate" #1 "{" !0 "id" #1 ":" #1 id[TEXT] !0 "gateType" #0 ":" #1 gateType[AND : "AND", OR : "OR"] !0 faults !0 (faults !0)+ "}";
}