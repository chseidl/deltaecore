package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.impl.DEDataTypeInitializerImpl;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;

public class DEDataTypeInitializerImplCustom extends DEDataTypeInitializerImpl {

//	@Override
//	public Class<?> getValueJavaClass() {
//		return super.getValueJavaClass();
//	}

	//TODO: Install caching to increase performance
	@Override
	public Object getValue() {
		EDataType dataType = getDataType();
		
		if (dataType != null) {
			String initializingValue = getInitializingValue();
			
			DEDeltaBlock block = DEDEcoreResolverUtil.getContainingBlock(this);
//			DEDelta delta = (DEDelta) EcoreUtil.getRootContainer(this);
			DEDeltaDialect dialect = block.getDeltaDialect();
			
			EFactory factory = dialect.getDomainFactory();
			return factory.createFromString(dataType, initializingValue);
		}
		
		return super.getValue();
	}

}
