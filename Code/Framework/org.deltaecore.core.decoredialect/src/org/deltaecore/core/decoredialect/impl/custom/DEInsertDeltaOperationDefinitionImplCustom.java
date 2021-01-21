package org.deltaecore.core.decoredialect.impl.custom;

import org.deltaecore.core.decoredialect.DEParameter;
import org.deltaecore.core.decoredialect.impl.DEInsertDeltaOperationDefinitionImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class DEInsertDeltaOperationDefinitionImplCustom extends DEInsertDeltaOperationDefinitionImpl {

	@Override
	public EList<DEParameter> getParameters() {
		EList<DEParameter> parameters = new BasicEList<DEParameter>();
		
		parameters.add(getValue());
		parameters.add(getElement());
		parameters.add(getIndex());
		
		return parameters;
	}
}
