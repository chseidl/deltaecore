package org.deltaecore.core.decoredialect.impl.custom;

import org.deltaecore.core.decoredialect.DEParameter;
import org.deltaecore.core.decoredialect.impl.DEModifyDeltaOperationDefinitionImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class DEModifyDeltaOperationDefinitionImplCustom extends DEModifyDeltaOperationDefinitionImpl {

	@Override
	public EList<DEParameter> getParameters() {
		EList<DEParameter> parameters = new BasicEList<DEParameter>();
		
		parameters.add(getValue());
		parameters.add(getElement());
		
		return parameters;
	}
}
