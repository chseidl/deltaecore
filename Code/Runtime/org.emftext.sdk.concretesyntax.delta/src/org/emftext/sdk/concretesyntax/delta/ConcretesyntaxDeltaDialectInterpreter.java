package org.emftext.sdk.concretesyntax.delta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.ConcretesyntaxPackage;
import org.emftext.sdk.concretesyntax.Rule;
import org.emftext.sdk.concretesyntax.resource.cs.ICsOptions;
import org.emftext.sdk.concretesyntax.resource.cs.util.CsResourceUtil;


//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class ConcretesyntaxDeltaDialectInterpreter extends ConcretesyntaxAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretAddRule(DEModelWriter modelWriter, String ruleText, ConcreteSyntax concreteSyntax) {
		if (concreteSyntax != null) {
			Rule rule = (Rule) getResourceContent(ruleText, ConcretesyntaxPackage.eINSTANCE.getRule());
			
			if (rule != null) {
				List<Rule> rules = concreteSyntax.getRules();
				rules.add(rule);
				return true;
			}
		}
		
		return false;
	}
	
	//Workaround for buggy implementation in CsResourceUtil.getResourceContent()
	private static EObject getResourceContent(String text, EClass startEClass) {
		Map<Object, Object> loadOptions = new LinkedHashMap<Object, Object>();
		
		if (startEClass != null) {
			loadOptions.put(ICsOptions.RESOURCE_CONTENT_TYPE, startEClass);
		}
		
		Resource resource = CsResourceUtil.getResource(text.getBytes(), new ResourceSetImpl(), loadOptions);
		
		if (resource == null) {
			return null;
		}
		List<EObject> contents = resource.getContents();
		if (contents == null || contents.isEmpty()) {
			return null;
		}
		EObject root = contents.get(0);
		return (Rule) root;
	}
}