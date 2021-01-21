package org.deltaecore.core.decoredialect.impl.custom;

import org.deltaecore.core.decoredialect.DEAddDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DECustomDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDetachDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEInsertDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEModelElementParameter;
import org.deltaecore.core.decoredialect.DEModifyDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DERemoveDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DESetDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEUnsetDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.impl.DEcoreDialectFactoryImpl;

public class DEcoreDialectFactoryImplCustom extends DEcoreDialectFactoryImpl {
	@Override
	public DEDeltaDialect createDEDeltaDialect() {
		return new DEDeltaDialectImplCustom();
	}

	@Override
	public DEAddDeltaOperationDefinition createDEAddDeltaOperationDefinition() {
		return new DEAddDeltaOperationDefinitionImplCustom();
	}
	
	@Override
	public DEInsertDeltaOperationDefinition createDEInsertDeltaOperationDefinition() {
		return new DEInsertDeltaOperationDefinitionImplCustom();
	}

	@Override
	public DEModifyDeltaOperationDefinition createDEModifyDeltaOperationDefinition() {
		return new DEModifyDeltaOperationDefinitionImplCustom();
	}

	@Override
	public DERemoveDeltaOperationDefinition createDERemoveDeltaOperationDefinition() {
		return new DERemoveDeltaOperationDefinitionImplCustom();
	}

	@Override
	public DECustomDeltaOperationDefinition createDECustomDeltaOperationDefinition() {
		return new DECustomDeltaOperationDefinitionImplCustom();
	}

	@Override
	public DEModelElementParameter createDEModelElementParameter() {
		return new DEModelElementParameterImplCustom();
	}

	@Override
	public DESetDeltaOperationDefinition createDESetDeltaOperationDefinition() {
		return new DESetDeltaOperationDefinitionImplCustom();
	}

	@Override
	public DEUnsetDeltaOperationDefinition createDEUnsetDeltaOperationDefinition() {
		return new DEUnsetDeltaOperationDefinitionImplCustom();
	}

	@Override
	public DEDetachDeltaOperationDefinition createDEDetachDeltaOperationDefinition() {
		return new DEDetachDeltaOperationDefinitionImplCustom();
	}
}
