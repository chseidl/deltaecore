package org.deltaecore.core.decoredialect.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DEType;
import org.deltaecore.core.decoredialect.DEAbstractModelElementParameter;
import org.deltaecore.core.decoredialect.DEAddDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DECustomDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEDetachDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEInsertDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEModifyDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DERemoveDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DESetDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEStandardDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEUnsetDeltaOperationDefinition;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

public class DEDEcoreDialectUtil {
	private static final List<Class<? extends DEDeltaOperationDefinition>> deltaOperationDefinitionTypeOrder = new ArrayList<Class<? extends DEDeltaOperationDefinition>>();
	
	static {
		//Sort order for automatically generated methods
		deltaOperationDefinitionTypeOrder.add(DESetDeltaOperationDefinition.class);
		deltaOperationDefinitionTypeOrder.add(DEUnsetDeltaOperationDefinition.class);
		
		deltaOperationDefinitionTypeOrder.add(DEAddDeltaOperationDefinition.class);
		deltaOperationDefinitionTypeOrder.add(DEInsertDeltaOperationDefinition.class);
		deltaOperationDefinitionTypeOrder.add(DERemoveDeltaOperationDefinition.class);
		
		deltaOperationDefinitionTypeOrder.add(DEModifyDeltaOperationDefinition.class);

		deltaOperationDefinitionTypeOrder.add(DEDetachDeltaOperationDefinition.class);
		
		deltaOperationDefinitionTypeOrder.add(DECustomDeltaOperationDefinition.class);
	}
	
	//Bring delta operation definitions in an order most suitable for printing.
	public static void sortDeltaOperationDefinitions(DEDeltaDialect deltaDialect) {
		//Cannot sort the list of delta operation definitions directly as it (temporarily) would contain
		//duplicate entries during sorting, which causes Ecore to instantly raise a "no duplicates" exception.
		List<DEDeltaOperationDefinition> originalDeltaOperationDefinitions = deltaDialect.getDeltaOperationDefinitions();
		List<DEDeltaOperationDefinition> deltaOperationDefinitions = new ArrayList<DEDeltaOperationDefinition>(originalDeltaOperationDefinitions);
		
		Collections.sort(deltaOperationDefinitions, new Comparator<DEDeltaOperationDefinition>() {
			@Override
			public int compare(DEDeltaOperationDefinition deltaOperationDefinition1, DEDeltaOperationDefinition deltaOperationDefinition2) {
				int intermediateResult = compareDeltaOperationDefinitionTargetEClassNames(deltaOperationDefinition1, deltaOperationDefinition2);
				
				//If EClass names are already different, take this result.
				if (intermediateResult != 0) {
					return intermediateResult;
				}
				
				//Otherwise, compare the type of method that is being generated
				int result = compareDeltaOperationDefinitionTypes(deltaOperationDefinition1, deltaOperationDefinition2);
				
				return result;
			}
		});
		
		originalDeltaOperationDefinitions.clear();
		originalDeltaOperationDefinitions.addAll(deltaOperationDefinitions);
	}
	
	private static int compareDeltaOperationDefinitionTargetEClassNames(DEDeltaOperationDefinition deltaOperationDefinition1, DEDeltaOperationDefinition deltaOperationDefinition2) {
		//Only standard operation definitions at this point
		if (deltaOperationDefinition1 instanceof DEStandardDeltaOperationDefinition && deltaOperationDefinition2 instanceof DEStandardDeltaOperationDefinition) {
			DEStandardDeltaOperationDefinition standardDeltaOperationDefinition1 = (DEStandardDeltaOperationDefinition) deltaOperationDefinition1;
			DEStandardDeltaOperationDefinition standardDeltaOperationDefinition2 = (DEStandardDeltaOperationDefinition) deltaOperationDefinition2;
			
			DEAbstractModelElementParameter modelElementParameter1 = standardDeltaOperationDefinition1.getElement();
			DEAbstractModelElementParameter modelElementParameter2 = standardDeltaOperationDefinition2.getElement();
			
			DEType type1 = modelElementParameter1.getType();
			DEType type2 = modelElementParameter2.getType();
			
			if (type1 instanceof DEMetaModelClassifierReference && type2 instanceof DEMetaModelClassifierReference) {
				DEMetaModelClassifierReference metaModelClassifierReference1 = (DEMetaModelClassifierReference) type1;
				DEMetaModelClassifierReference metaModelClassifierReference2 = (DEMetaModelClassifierReference) type2;
				
				EClassifier eClassifier1 = metaModelClassifierReference1.getClassifier();
				EClassifier eClassifier2 = metaModelClassifierReference2.getClassifier();
				
				if (eClassifier1 instanceof EClass && eClassifier2 instanceof EClass) {
					EClass eClass1 = (EClass) eClassifier1;
					EClass eClass2 = (EClass) eClassifier2;
					
					//Name of target class is main sort criterion.
					String name1 = eClass1.getName();
					String name2 = eClass2.getName();
					
					return name1.compareTo(name2);
				}
			}
		} else {
			System.err.println("Encountered non-standard delta operation definition for sorting - update method in " + DEDEcoreDialectUtil.class.getSimpleName());
		}
		
		return 0;
	}
	
	private static int compareDeltaOperationDefinitionTypes(DEDeltaOperationDefinition deltaOperationDefinition1, DEDeltaOperationDefinition deltaOperationDefinition2) {
		Class<? extends DEDeltaOperationDefinition> deltaOperationDefinitionType1 = deltaOperationDefinition1.getClass();
		Class<? extends DEDeltaOperationDefinition> deltaOperationDefinitionType2 = deltaOperationDefinition2.getClass();
		
		if (deltaOperationDefinitionType1 == deltaOperationDefinitionType2) {
			return 0;
		}
		
		int orderIndex1 = getDeltaOperationDefinitionTypeOrderIndex(deltaOperationDefinitionType1);
		int orderIndex2 = getDeltaOperationDefinitionTypeOrderIndex(deltaOperationDefinitionType2);
		
		return Integer.compare(orderIndex1, orderIndex2);
	}
	
	private static int getDeltaOperationDefinitionTypeOrderIndex(Class<? extends DEDeltaOperationDefinition> searchedDeltaOperationDefinitionType) {
		int i = 0;
		
		for (Class<? extends DEDeltaOperationDefinition> deltaOperationDefinitionType : deltaOperationDefinitionTypeOrder) {
			if (deltaOperationDefinitionType.isAssignableFrom(searchedDeltaOperationDefinitionType)) {
				return i;
			}
			
			i++;
		}
		
		return -1;
	}
}
