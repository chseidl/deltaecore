package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.DEArgument;
import org.deltaecore.core.decore.DEBooleanLiteral;
import org.deltaecore.core.decore.DEDataTypeInitializer;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEDoubleLiteral;
import org.deltaecore.core.decore.DEEEnumLiteral;
import org.deltaecore.core.decore.DEIntegerLiteral;
import org.deltaecore.core.decore.DEModelElementIdentifier;
import org.deltaecore.core.decore.DENullLiteral;
import org.deltaecore.core.decore.DEStandaloneExpressionStatement;
import org.deltaecore.core.decore.DEStringLiteral;
import org.deltaecore.core.decore.DEStructuralFeatureReference;
import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.DEVariableReference;
import org.deltaecore.core.decore.DEVirtualConstructorCall;
import org.deltaecore.core.decore.impl.DEcoreFactoryImpl;

public class DEcoreFactoryImplCustom extends DEcoreFactoryImpl {

	@Override
	public DEVariableReference createDEVariableReference() {
		return new DEVariableReferenceImplCustom();
	}

	@Override
	public DENullLiteral createDENullLiteral() {
		return new DENullLiteralImplCustom();
	}
	
	@Override
	public DEIntegerLiteral createDEIntegerLiteral() {
		return new DEIntegerLiteralImplCustom();
	}

	@Override
	public DEBooleanLiteral createDEBooleanLiteral() {
		return new DEBooleanLiteralImplCustom();
	}

	@Override
	public DEDoubleLiteral createDEDoubleLiteral() {
		return new DEDoubleLiteralImplCustom();
	}

	@Override
	public DEStringLiteral createDEStringLiteral() {
		return new DEStringLiteralImplCustom();
	}

	@Override
	public DEVirtualConstructorCall createDEVirtualConstructorCall() {
		return new DEVirtualConstructorCallImplCustom();
	}

	@Override
	public DEEEnumLiteral createDEEEnumLiteral() {
		return new DEEEnumLiteralImplCustom();
	}

	@Override
	public DEVariableDeclaration createDEVariableDeclaration() {
		return new DEVariableDeclarationImplCustom();
	}

	@Override
	public DEStandaloneExpressionStatement createDEStandaloneExpressionStatement() {
		return new DEStandaloneExpressionStatementImplCustom();
	}

	@Override
	public DEArgument createDEArgument() {
		return new DEArgumentImplCustom();
	}

	@Override
	public DEStructuralFeatureReference createDEStructuralFeatureReference() {
		return new DEStructuralFeatureReferenceImplCustom();
	}

	@Override
	public DEDeltaBlock createDEDeltaBlock() {
		return new DEDeltaBlockImplCustom();
	}

	@Override
	public DEDataTypeInitializer createDEDataTypeInitializer() {
		return new DEDataTypeInitializerImplCustom();
	}

	@Override
	public DEModelElementIdentifier createDEModelElementIdentifier() {
		return new DEModelElementIdentifierImplCustom();
	}
}
