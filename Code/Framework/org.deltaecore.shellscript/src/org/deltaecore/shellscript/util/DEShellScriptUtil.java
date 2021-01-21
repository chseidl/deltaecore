package org.deltaecore.shellscript.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.deltaecore.shellscript.DEArgument;
import org.deltaecore.shellscript.DECommand;
import org.deltaecore.shellscript.DEParameterArgument;

public class DEShellScriptUtil {
	public static String getCanonicalName(String name) {
		if (name == null) {
			return null;
		}
		
		return name.toLowerCase();
	}

	public static boolean isCommand(DECommand command, String shortName) {
		String name = getCanonicalName(command.getName());
		
		if (name.equals(getCanonicalName(shortName))) {
			return true;
		}
		
		return false;
	}
	
	public static boolean hasArgument(DECommand command, String searchedName) {
		DEArgument argument = getArgument(command, searchedName);
		
		return argument != null;
	}
	
	public static DEArgument getArgument(DECommand command, String searchedName) {
		List<DEArgument> arguments = command.getArguments();
		
		for (DEArgument argument : arguments) {
			String name = getCanonicalName(argument.getName());
			
			if (name.equals(searchedName)) {
				return argument;
			}
		}
		
		return null;
	}
	
	public static String getArgumentValue(DECommand command, String argumentName) {
		List<String> argumentValues = getArgumentValues(command, argumentName);
		
		if (!argumentValues.isEmpty()) {
			return argumentValues.get(0);
		}
		
		return null;
	}
	
	public static List<String> getArgumentValues(DECommand command, String argumentName) {
		DEArgument argument = getArgument(command, argumentName);
		
		if (argument instanceof DEParameterArgument) {
			DEParameterArgument parameterArgument = (DEParameterArgument) argument;
			
			return parameterArgument.getValues();
		}
		
		return new ArrayList<String>();
	}
	
	public static boolean ensureMinMax(DECommand command, String[] arguments, int min, int max) {
		if (arguments == null || min > max) {
			throw new InvalidParameterException();
		}
		
		int argumentCount = 0;
		
		for (String argument : arguments) {
			if (hasArgument(command, argument)) {
				argumentCount++;
			}
		}
		
		if (argumentCount < min || argumentCount > max) {
			return false;
		}
		
		return true;
	}
}
