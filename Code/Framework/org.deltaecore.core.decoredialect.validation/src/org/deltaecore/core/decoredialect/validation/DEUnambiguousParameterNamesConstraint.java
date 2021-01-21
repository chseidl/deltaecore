package org.deltaecore.core.decoredialect.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEParameter;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEUnambiguousParameterNamesConstraint extends AbstractConstraint<DEDeltaOperationDefinition> {
	
	@Override
	protected IStatus doValidate(DEDeltaOperationDefinition deltaOperationDefinition) {
		Set<String> alreadyDeclaredParameterNames = new HashSet<String>();
		
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
		List<DEParameter> ambiguousParameters = new ArrayList<DEParameter>();
		
		for (DEParameter parameter : parameters) {
			String parameterName = parameter.getName();
			
			if (alreadyDeclaredParameterNames.contains(parameterName)) {
				ambiguousParameters.add(parameter);
			} else {
				alreadyDeclaredParameterNames.add(parameterName);
			}
		}
		
		if (!ambiguousParameters.isEmpty()) {
			//Only a single ambiguous parameter -> only one status
			if (ambiguousParameters.size() == 1) {
				return createAmbiguousParameterStatus(ambiguousParameters.get(0));
			}
			
			//Multiple ambiguous parameters -> multi status
			String pluginId = getDescriptor().getPluginId();
			MultiStatus multiStatus = new MultiStatus(pluginId, 1, "Ambiguous parameter names in declaration of \"" + deltaOperationDefinition.getName() + "\".", null);
			
			for (DEParameter ambiguousParameter : ambiguousParameters) {
				IStatus ambiguousParameterStatus = createAmbiguousParameterStatus(ambiguousParameter);
				multiStatus.add(ambiguousParameterStatus);
			}
			
			return multiStatus;
		}
		
		return createSuccessStatus();
	}
	
	private IStatus createAmbiguousParameterStatus(DEParameter ambiguousParameter) {
		return createErrorStatus("The parameter name \"" + ambiguousParameter.getName() + "\" is not unique.", ambiguousParameter);
	}
}
