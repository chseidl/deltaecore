package org.deltaecore.core.generation.interpreter;

import java.util.List;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EAttribute;
import org.deltaecore.core.generation.general.DENameGenerator;
import org.deltaecore.core.decoredialect.*;

public class DEAbstractDeltaDialectInterpreterCreator
{
  protected static String nl;
  public static synchronized DEAbstractDeltaDialectInterpreterCreator create(String lineSeparator)
  {
    nl = lineSeparator;
    DEAbstractDeltaDialectInterpreterCreator result = new DEAbstractDeltaDialectInterpreterCreator();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "import java.util.List;" + NL + "" + NL + "import org.deltaecore.core.decore.DEArgument;" + NL + "import org.deltaecore.core.decore.DEDeltaOperationCall;" + NL + "import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;" + NL + "import org.deltaecore.core.variant.interpretation.DEDeltaDialectInterpreter;" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + NL + "public abstract class ";
  protected final String TEXT_5 = " implements DEDeltaDialectInterpreter {" + NL + "\t" + NL + "\t@Override" + NL + "\tpublic boolean interpretDeltaOperationCall(DEDeltaOperationCall deltaOperationCall, DEModelWriter modelWriter) {" + NL + "\t\tDEDeltaOperationDefinition deltaOperationDefinition = deltaOperationCall.getOperationDefinition();" + NL + "\t\tString deltaOperationName = deltaOperationDefinition.getName();" + NL + "\t\t" + NL + "\t\tList<DEArgument> arguments = deltaOperationCall.getArguments();" + NL + "\t\t";
  protected final String TEXT_6 = "\t" + NL + "\t\tif (deltaOperationName.equals(\"";
  protected final String TEXT_7 = "\")) {";
  protected final String TEXT_8 = NL + "\t\t\t";
  protected final String TEXT_9 = "\t\t\t" + NL + "" + NL + "\t\t\treturn ";
  protected final String TEXT_10 = ";" + NL + "\t\t}" + NL + "\t\t";
  protected final String TEXT_11 = NL + "\t\treturn false;" + NL + "\t}" + NL;
  protected final String TEXT_12 = NL + NL + "\t";
  protected final String TEXT_13 = " {";
  protected final String TEXT_14 = NL + "\t\t//Set the value of ";
  protected final String TEXT_15 = ".";
  protected final String TEXT_16 = " to ";
  protected final String TEXT_17 = NL + "\t\tmodelWriter.setValue(";
  protected final String TEXT_18 = ", ";
  protected final String TEXT_19 = ");" + NL + "\t\treturn true;\t";
  protected final String TEXT_20 = NL + "\t\t//Unset the value of ";
  protected final String TEXT_21 = NL + "\t\tmodelWriter.unsetValue(";
  protected final String TEXT_22 = NL + "\t\t//Add ";
  protected final String TEXT_23 = NL + "\t\tmodelWriter.addValue(";
  protected final String TEXT_24 = ");" + NL + "\t\treturn true;";
  protected final String TEXT_25 = NL + "\t\t//Insert ";
  protected final String TEXT_26 = " into ";
  protected final String TEXT_27 = " at position ";
  protected final String TEXT_28 = NL + "\t\tmodelWriter.insertValue(";
  protected final String TEXT_29 = ");" + NL + "\t\t" + NL + "\t\treturn true;";
  protected final String TEXT_30 = NL + "\t\t//Remove ";
  protected final String TEXT_31 = " from ";
  protected final String TEXT_32 = NL + "\t\tmodelWriter.removeValue(";
  protected final String TEXT_33 = NL + "\t\t//Modify the value of ";
  protected final String TEXT_34 = " to have the value of ";
  protected final String TEXT_35 = NL + "\t\t//Detach ";
  protected final String TEXT_36 = " from its containing reference" + NL + "\t\tmodelWriter.detach(";
  protected final String TEXT_37 = NL + "\t}";
  protected final String TEXT_38 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	DEDeltaDialectInterpreterCreatorSetup setup = (DEDeltaDialectInterpreterCreatorSetup) argument;
	DENameGenerator nameGenerator = setup.getNameGenerator();
	DEDeltaDialect dialect = setup.getDialect();
	
	List<DEDeltaOperationDefinition> deltaOperationDefinitions = dialect.getDeltaOperationDefinitions();

    stringBuffer.append(TEXT_1);
    stringBuffer.append( nameGenerator.getDeltaDialectBasePackage() );
    stringBuffer.append(TEXT_2);
    
		List<String> importStatements = DEDeltaDialectInterpreterCreatorUtil.createImportStatements(deltaOperationDefinitions, false);
		
		for (String importStatement : importStatements) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( importStatement );
    
		}

    stringBuffer.append(TEXT_4);
    stringBuffer.append( nameGenerator.getAbstractDeltaDialectInterpreterSimpleClassName() );
    stringBuffer.append(TEXT_5);
    



	//Unwrap delta operation definitions
	for (DEDeltaOperationDefinition deltaOperationDefinition : deltaOperationDefinitions) {

    stringBuffer.append(TEXT_6);
    stringBuffer.append( deltaOperationDefinition.getName() );
    stringBuffer.append(TEXT_7);
    
		List<String> interpreterMethodArgumentDeclarations = DEDeltaDialectInterpreterCreatorUtil.createInterpreterMethodArgumentDeclarations(deltaOperationDefinition);
		
		for (String interpreterMethodArgumentDeclaration : interpreterMethodArgumentDeclarations) {

    stringBuffer.append(TEXT_8);
    stringBuffer.append( interpreterMethodArgumentDeclaration );
    
		}

    stringBuffer.append(TEXT_9);
    stringBuffer.append( DEDeltaDialectInterpreterCreatorUtil.createInterpreterMethodCall(deltaOperationDefinition) );
    stringBuffer.append(TEXT_10);
    
	}

    stringBuffer.append(TEXT_11);
    



	//Create implementations for interpretation methods as far as possible.
	for (DEDeltaOperationDefinition deltaOperationDefinition : deltaOperationDefinitions) {
		
		if (DEDeltaDialectInterpreterCreatorUtil.needsManualImplementation(deltaOperationDefinition)) {
			//Create abstract declaration and have stub in concrete interpreter.

    stringBuffer.append(TEXT_12);
    stringBuffer.append( DEDeltaDialectInterpreterCreatorUtil.createInterpreterMethodAbstractDeclaration(deltaOperationDefinition) );
    
		} else {

    stringBuffer.append(TEXT_12);
    stringBuffer.append( DEDeltaDialectInterpreterCreatorUtil.createInterpreterMethodSignature(deltaOperationDefinition) );
    stringBuffer.append(TEXT_13);
    
			if (deltaOperationDefinition instanceof DESingleValuedReferenceDeltaOperationDefinition || deltaOperationDefinition instanceof DEMultiValuedReferenceDeltaOperationDefinition) {
				DEStandardDeltaOperationDefinition standardDeltaOperationDefinition = (DEStandardDeltaOperationDefinition) deltaOperationDefinition;
				DEModelElementWithReferenceParameter elementParameter = (DEModelElementWithReferenceParameter) standardDeltaOperationDefinition.getElement();
				String elementParameterName = elementParameter.getName();
			
				EReference reference = elementParameter.getReference();
				String referencePackageConstant = DEDeltaDialectInterpreterCreatorUtil.createPackageConstantForStructuralFeature(reference);
				
				

				if (deltaOperationDefinition instanceof DESingleValuedReferenceDeltaOperationDefinition) {
				
				
					if (deltaOperationDefinition instanceof DESetDeltaOperationDefinition) {
						DESetDeltaOperationDefinition setDeltaOperationDefinition = (DESetDeltaOperationDefinition) deltaOperationDefinition;
					
						DENamedParameter valueParameter = setDeltaOperationDefinition.getValue();
						String valueParameterName = valueParameter.getName();

    stringBuffer.append(TEXT_14);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( reference.getName() );
    stringBuffer.append(TEXT_16);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_17);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( referencePackageConstant );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_19);
    					
					}
			
			
					if (deltaOperationDefinition instanceof DEUnsetDeltaOperationDefinition) {

    stringBuffer.append(TEXT_20);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( reference.getName() );
    stringBuffer.append(TEXT_21);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( referencePackageConstant );
    stringBuffer.append(TEXT_19);
    	
					}
				}
			
			
			
				if (deltaOperationDefinition instanceof DEMultiValuedReferenceDeltaOperationDefinition) {
					DEMultiValuedReferenceDeltaOperationDefinition multiValuedReferenceDeltaOperationDefinition = (DEMultiValuedReferenceDeltaOperationDefinition) deltaOperationDefinition;
					
					DENamedParameter valueParameter = multiValuedReferenceDeltaOperationDefinition.getValue();
					String valueParameterName = valueParameter.getName();
				
				
					if (deltaOperationDefinition instanceof DEAddDeltaOperationDefinition) {

    stringBuffer.append(TEXT_22);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_16);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( reference.getName() );
    stringBuffer.append(TEXT_23);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( referencePackageConstant );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_24);
    
					}
					
					
					if (deltaOperationDefinition instanceof DEInsertDeltaOperationDefinition) {
						DEInsertDeltaOperationDefinition insertDeltaOperationDefinition = (DEInsertDeltaOperationDefinition) deltaOperationDefinition;
						
						DENamedParameter indexParameter = insertDeltaOperationDefinition.getIndex();
						String indexParameterName = indexParameter.getName();

    stringBuffer.append(TEXT_25);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_26);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( reference.getName() );
    stringBuffer.append(TEXT_27);
    stringBuffer.append( indexParameterName );
    stringBuffer.append(TEXT_28);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( referencePackageConstant );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( indexParameterName );
    stringBuffer.append(TEXT_29);
    
					}
					
					
					if (deltaOperationDefinition instanceof DERemoveDeltaOperationDefinition) {

    stringBuffer.append(TEXT_30);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_31);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( reference.getName() );
    stringBuffer.append(TEXT_32);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( referencePackageConstant );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_24);
    
					}	
					
				}
			}
		
		
		
			if (deltaOperationDefinition instanceof DEAttributeDeltaOperationDefinition) {
				DEAttributeDeltaOperationDefinition attributeDeltaOperationDefinition = (DEAttributeDeltaOperationDefinition) deltaOperationDefinition;
				
				DENamedParameter valueParameter = attributeDeltaOperationDefinition.getValue();
				String valueParameterName = valueParameter.getName();

				DEModelElementWithAttributeParameter elementParameter = attributeDeltaOperationDefinition.getElement();
				String elementParameterName = elementParameter.getName();
		
				EAttribute attribute = elementParameter.getAttribute();
				String attributePackageConstant = DEDeltaDialectInterpreterCreatorUtil.createPackageConstantForStructuralFeature(attribute);
					
				if (deltaOperationDefinition instanceof DEModifyDeltaOperationDefinition) {

    stringBuffer.append(TEXT_33);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( attribute.getName() );
    stringBuffer.append(TEXT_34);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_17);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( attributePackageConstant );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( valueParameterName );
    stringBuffer.append(TEXT_24);
    
				}	
			}
		



			if (deltaOperationDefinition instanceof DEModelElementDeltaOperationDefinition) {
				DEModelElementDeltaOperationDefinition modelElementDeltaOperationDefinition = (DEModelElementDeltaOperationDefinition) deltaOperationDefinition;
		
				DEModelElementParameter elementParameter = modelElementDeltaOperationDefinition.getElement();
				String elementParameterName = elementParameter.getName();
			
				if (deltaOperationDefinition instanceof DEDetachDeltaOperationDefinition) {

    stringBuffer.append(TEXT_35);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_36);
    stringBuffer.append( elementParameterName );
    stringBuffer.append(TEXT_24);
    
				}	
			}

    stringBuffer.append(TEXT_37);
    
		}	
	
	}

    stringBuffer.append(TEXT_38);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
