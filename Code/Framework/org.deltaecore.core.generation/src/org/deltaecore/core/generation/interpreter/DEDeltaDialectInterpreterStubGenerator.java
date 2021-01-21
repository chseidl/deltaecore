package org.deltaecore.core.generation.interpreter;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.generation.general.DENameGenerator;
import org.deltaecore.core.generation.general.DEProjectSetupGenerator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.eclipse.generation.EclipseGenerationUtil;

public class DEDeltaDialectInterpreterStubGenerator {
	private DENameGenerator nameGenerator;
	private DEProjectSetupGenerator projectSetupGenerator;
	
	private DEDeltaDialectInterpreterCreatorSetup interpreterCreatorSetup;
	
	public DEDeltaDialectInterpreterStubGenerator(DENameGenerator nameGenerator, DEProjectSetupGenerator projectSetupGenerator, DEDeltaDialect dialect) {
		this.nameGenerator = nameGenerator;
		this.projectSetupGenerator = projectSetupGenerator;
		
		interpreterCreatorSetup = new DEDeltaDialectInterpreterCreatorSetup(nameGenerator, dialect);
	}
	
	private void generateAbstractDeltaDialectInterpreterStub() {
		DEAbstractDeltaDialectInterpreterCreator creator = new DEAbstractDeltaDialectInterpreterCreator();
		String qualifiedClassName = nameGenerator.getAbstractDeltaDialectInterpreterQualifiedClassName();
		IFolder srcGenFolder = projectSetupGenerator.getSrcGenFolder();
		
		IFile classFile = EclipseGenerationUtil.createClassFileFromQualifiedClassName(qualifiedClassName, srcGenFolder);
		
		String content = creator.generate(interpreterCreatorSetup);
		//Always overrides
		ResourceUtil.writeToFile(content, classFile);
	}
	
	private void generateDeltaDialectInterpreterStub() {
		DEDeltaDialectInterpreterCreator creator = new DEDeltaDialectInterpreterCreator();
		String qualifiedClassName = nameGenerator.getDeltaDialectInterpreterQualifiedClassName();
		IFolder srcFolder = projectSetupGenerator.getSrcFolder();

		IFile classFile = EclipseGenerationUtil.createClassFileFromQualifiedClassName(qualifiedClassName, srcFolder);
		
		//Only generate once - do not override.
		if (!classFile.exists()) {
			String content = creator.generate(interpreterCreatorSetup);
			ResourceUtil.writeToFile(content, classFile);
		}
	}
	
	public void generate() {
		generateAbstractDeltaDialectInterpreterStub();
		generateDeltaDialectInterpreterStub();
	}
}
