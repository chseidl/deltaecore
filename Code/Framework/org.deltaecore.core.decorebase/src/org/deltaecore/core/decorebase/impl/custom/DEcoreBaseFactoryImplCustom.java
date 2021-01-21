package org.deltaecore.core.decorebase.impl.custom;

import org.deltaecore.core.decorebase.DEBoolean;
import org.deltaecore.core.decorebase.DEDouble;
import org.deltaecore.core.decorebase.DEInteger;
import org.deltaecore.core.decorebase.DEJavaClassReference;
import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DEString;
import org.deltaecore.core.decorebase.impl.DEcoreBaseFactoryImpl;

public class DEcoreBaseFactoryImplCustom extends DEcoreBaseFactoryImpl {
	@Override
	public DEBoolean createDEBoolean() {
		return new DEBooleanImplCustom();
	}

	@Override
	public DEInteger createDEInteger() {
		return new DEIntegerImplCustom();
	}

	@Override
	public DEDouble createDEDouble() {
		return new DEDoubleImplCustom();
	}

	@Override
	public DEString createDEString() {
		return new DEStringImplCustom();
	}

	@Override
	public DEMetaModelClassifierReference createDEMetaModelClassifierReference() {
		return new DEMetaModelClassifierReferenceImplCustom();
	}

	@Override
	public DEJavaClassReference createDEJavaClassReference() {
		return new DEJavaClassReferenceImplCustom();
	}
}
