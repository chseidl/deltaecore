package org.deltaecore.shellscript.interpretation;

public interface DEShellScriptConstants {
	public static final String COMMAND_FORMAT = "Command format: '<Command> [-<Argument> [\"<Value1>\", ..., \"<ValueN>\"]]'";
	
	//Commands
	public static final String COMMAND_HELP = "h";
	public static final String COMMAND_HELP_LONG = "Help";
	public static final String COMMAND_HELP_DESCRIPTION = "Display a list of commands or details on a specific command.";
	
	public static final String COMMAND_DERIVE_VARIANT = "dv";
	public static final String COMMAND_DERIVE_VARIANT_LONG = "DeriveVariant";
	public static final String COMMAND_DERIVE_VARIANT_DESCRIPTION = "Derive a variant from selected delta modules/features/features with versions.";
	
	//Options
	public static final String PARAMETER_COMMAND = "c";
	
	public static final String PARAMETER_DELTA_MODULES = "dm";
	public static final String PARAMETER_FEATURES = "f";
	public static final String PARAMETER_FEATURES_WITH_VERSIONS = "fv";
	//TODO: Configuration
	
	public static final String PARAMETER_FEATURE_MODEL = "fm";
//	public static final String PARAMETER_CONSTRAINT_MODEL = "cm";
//	public static final String PARAMETER_MAPPING_MODEL = "mm";
//	public static final String PARAMETER_APPLICATION_ORDER_CONSTRAINT_MODEL = "aocm";
	
	public static final String PARAMETER_VARIANT_FOLDER = "vf";
	
	//Modifiers
	public static final String SWITCH_QUIET = "q";
	public static final String SWITCH_VERBOSE = "vb";
}
