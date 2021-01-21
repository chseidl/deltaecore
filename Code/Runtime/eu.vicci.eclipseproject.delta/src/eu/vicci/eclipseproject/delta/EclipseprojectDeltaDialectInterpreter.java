package eu.vicci.eclipseproject.delta;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class EclipseprojectDeltaDialectInterpreter extends EclipseprojectAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretAddRequiredLibrary(DEModelWriter modelWriter, String pathToLibrary) {
		//TODO: Implement this some time
		System.out.println("Added Required Library: " + pathToLibrary);
		return true;
	}

	@Override
	protected boolean interpretAddRequiredLib(DEModelWriter modelWriter, String pathToLibrary) {
		//Alias method
		return interpretAddRequiredLibrary(modelWriter, pathToLibrary);
	}

	@Override
	protected boolean interpretRemoveRequiredLibrary(DEModelWriter modelWriter, String pathToLibrary) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean interpretAddRequiredProject(DEModelWriter modelWriter, String projectName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean interpretRemoveRequiredProject(DEModelWriter modelWriter, String projectName) {
		// TODO Auto-generated method stub
		return false;
	}
}