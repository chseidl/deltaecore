package org.deltaecore.core.generation.interpreter;

import java.util.List;
import org.deltaecore.core.generation.general.DENameGenerator;
import org.deltaecore.core.decoredialect.*;

public class DEDeltaDialectInterpreterCreator
{
  protected static String nl;
  public static synchronized DEDeltaDialectInterpreterCreator create(String lineSeparator)
  {
    nl = lineSeparator;
    DEDeltaDialectInterpreterCreator result = new DEDeltaDialectInterpreterCreator();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + NL + "//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually." + NL + "public class ";
  protected final String TEXT_5 = " extends ";
  protected final String TEXT_6 = " {";
  protected final String TEXT_7 = NL + NL + "\t@Override" + NL + "\t";
  protected final String TEXT_8 = NL + "\t\t//TODO: Implement ";
  protected final String TEXT_9 = "()" + NL + "\t\treturn false;";
  protected final String TEXT_10 = NL + "\t}";
  protected final String TEXT_11 = NL + "}";

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
    
	List<String> importStatements = DEDeltaDialectInterpreterCreatorUtil.createImportStatements(deltaOperationDefinitions, true);
		
	for (String importStatement : importStatements) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( importStatement );
    
	}

    stringBuffer.append(TEXT_4);
    stringBuffer.append( nameGenerator.getDeltaDialectInterpreterSimpleClassName() );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( nameGenerator.getAbstractDeltaDialectInterpreterSimpleClassName() );
    stringBuffer.append(TEXT_6);
    
	for (DEDeltaOperationDefinition deltaOperationDefinition : deltaOperationDefinitions) {
	String methodName = DEDeltaDialectInterpreterCreatorUtil.createDeltaOperationMethodName(deltaOperationDefinition);

	//Methods that had no implementation generated now need to be overwritten or manual implementation.
	if (DEDeltaDialectInterpreterCreatorUtil.needsManualImplementation(deltaOperationDefinition)) {

    stringBuffer.append(TEXT_7);
    stringBuffer.append( DEDeltaDialectInterpreterCreatorUtil.createInterpreterMethodSignature(deltaOperationDefinition) );
    stringBuffer.append(TEXT_6);
    
			
			if (deltaOperationDefinition instanceof DECustomDeltaOperationDefinition) {

    stringBuffer.append(TEXT_8);
    stringBuffer.append( methodName );
    stringBuffer.append(TEXT_9);
    
			}

    stringBuffer.append(TEXT_10);
    
		}
	}

    stringBuffer.append(TEXT_11);
    return stringBuffer.toString();
  }
}
