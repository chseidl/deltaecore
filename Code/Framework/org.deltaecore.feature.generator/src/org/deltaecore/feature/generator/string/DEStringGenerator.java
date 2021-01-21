package org.deltaecore.feature.generator.string;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.christophseidl.util.java.Random2;

public abstract class DEStringGenerator<T, U extends DEInternalStringGeneratorData> {
	private Random2 random;
	private Map<T, U> alreadyCreatedInternalRepresentationsMapping; 
	
	public DEStringGenerator() {
		random = new Random2();
		alreadyCreatedInternalRepresentationsMapping = new HashMap<T, U>();
		
		reset();
	}
	
	public void reset() {
		alreadyCreatedInternalRepresentationsMapping.clear();
	}

	protected Random2 getRandom() {
		return random;
	}
	
	public String generateString(T element) {
		U createdInternalRepresentation = null;
		int attemptNumber = 0;
		
		do {
			//Add some salt to make it unique
			createdInternalRepresentation = doGenerate(element, attemptNumber);
			attemptNumber++;
		} while(alreadyExists(createdInternalRepresentation));
		
		alreadyCreatedInternalRepresentationsMapping.put(element, createdInternalRepresentation);
		
		return createdInternalRepresentation.toString();
	}
	
	private boolean alreadyExists(U createdInternalRepresentation) {
		Collection<U> alreadyCreatedInternalRepresentations = alreadyCreatedInternalRepresentationsMapping.values();
		
		return alreadyCreatedInternalRepresentations.contains(createdInternalRepresentation);
	}
	
	protected abstract U doGenerate(T element, int attemptNumber);
	
	public U getPreviousInternalRepresentation(T element) {
		return alreadyCreatedInternalRepresentationsMapping.get(element);
	}
}
