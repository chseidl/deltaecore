package org.deltaecore.installer;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapter;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.mwe.utils.GenModelHelper;
import org.eclipse.emf.mwe2.ecore.CvsIdFilteringGeneratorAdapterFactoryDescriptor;
import org.eclipse.emf.mwe2.runtime.Mandatory;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;

/**
 * Respects @generated NOT annotations and is less cluttered than the original.
 *  
 * @author Christoph Seidl
 */
public class EcoreGenerator2 implements IWorkflowComponent {

	private static Logger log = Logger.getLogger(EcoreGenerator2.class);
	
	static {
		EcorePackage.eINSTANCE.getEFactoryInstance();
		GenModelPackage.eINSTANCE.getEFactoryInstance();
	}

	private String genModel;
	
	private boolean generateEdit = false;
	private boolean generateEditor = false;

	
	@Mandatory
	public void setGenModel(String genModel) {
		this.genModel = genModel;
	}
	
	public void setGenerateEdit(boolean generateEdit) {
		this.generateEdit = generateEdit;
	}
	
	public void setGenerateEditor(boolean generateEditor) {
		this.generateEditor = generateEditor;
	}
	
	@Override
	public void preInvoke() {
	}
	
	@Override
	public void invoke(IWorkflowContext context) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(URI.createURI(genModel), true);
		List<EObject> contents = resource.getContents();
		
		if (contents.isEmpty()) {
			log.error("Could not load GenModel from " + genModel);
			return;
		}
		
		GenModel genModel = (GenModel) contents.get(0);
		genModel.setCanGenerate(true);
		genModel.reconcile();
		
		GenModelHelper helper = new GenModelHelper();
		helper.registerGenModel(genModel);

		Generator generator = new Generator();
		
		
		log.info("Generating EMF code for " + genModel);
		
		GeneratorAdapterFactory.Descriptor.Registry adapterFactoryDescriptorRegistry = generator.getAdapterFactoryDescriptorRegistry();
		adapterFactoryDescriptorRegistry.addDescriptor(GenModelPackage.eNS_URI, new CustomGeneratorAdapterDescriptor());
		generator.setInput(genModel);

		Diagnostic diagnostic = generator.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE, new BasicMonitor());

		if (diagnostic.getSeverity() != Diagnostic.OK) {
			log.info(diagnostic);
		}
		
		if (generateEdit) {
			Diagnostic editDiag = generator.generate(genModel, GenBaseGeneratorAdapter.EDIT_PROJECT_TYPE, new BasicMonitor());
			
			if (editDiag.getSeverity() != Diagnostic.OK) {
				log.info(editDiag);
			}
		}

		if (generateEditor) {
			Diagnostic editorDiag = generator.generate(genModel, GenBaseGeneratorAdapter.EDITOR_PROJECT_TYPE, new BasicMonitor());
			
			if (editorDiag.getSeverity() != Diagnostic.OK) {
				log.info(editorDiag);
			}
		}
	}

	@Override
	public void postInvoke() {
	}
	
	
	private static class CustomGeneratorAdapterDescriptor extends CvsIdFilteringGeneratorAdapterFactoryDescriptor {
		private final class CustomGeneratorAdapterFactory extends IdFilteringGenModelGeneratorAdapterFactory {
			@Override
			public Adapter createGenModelAdapter() {
				if (genModelGeneratorAdapter == null) {
					genModelGeneratorAdapter = new GenModelGeneratorAdapter(this);
				}
				
				return genModelGeneratorAdapter;
			}
		}

		@Override
		public GeneratorAdapterFactory createAdapterFactory() {
			return new CustomGeneratorAdapterFactory();
		}
	}
}
