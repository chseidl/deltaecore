package org.deltaecore.core.generation;

import java.util.HashSet;
import java.util.Set;

import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;

public class DEIdentifierNameBuilder {
	private Set<String> registeredIdentifierNames;
	
	public DEIdentifierNameBuilder() {
		registeredIdentifierNames = new HashSet<String>();
	}
	
	protected void registerIdentifierName(String name) {
		registeredIdentifierNames.add(name);
	}
	
	protected void unregisterIdentifierName(String name) {
		registeredIdentifierNames.remove(name);
	}
	
//	public boolean isIdentifierNameValidAndNotTaken(String identifierName) {
//		if (!DEDecoreBaseValidationUtil.isValidIdentifier(identifierName)) {
//			return false;
//		}
//		
//		if (registeredIdentifierNames.contains(identifierName)) {
//			return false;
//		}
//		
//		return true;
//	}
	
	protected String makeIdentifierNameValidAndUniqueIfNecessary(String identifierName) {
		String validIdentifierName = DEDEcoreBaseUtil.makeValidIdentifier(identifierName);
		
		if (registeredIdentifierNames != null) {
			//Start numbering with two as in "human" counting you start with one
			//and the original parameter is already declared which makes this (at least) number two.
			int i = 2;
			
			//If the suggested parameter name is already in use or a reserved keyword, make it unique by appending a number.
			while (registeredIdentifierNames.contains(validIdentifierName) || DEDEcoreBaseUtil.isReservedKeyword(validIdentifierName)) {
				validIdentifierName = identifierName + i;
				i++;
			}
		}
		
		return validIdentifierName;
	}
}
