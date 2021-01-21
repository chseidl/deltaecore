package org.deltaecore.core.decorebase.impl.custom;

import java.util.List;

import org.deltaecore.core.decorebase.impl.DEJavaClassReferenceImpl;

public class DEJavaClassReferenceImplCustom extends DEJavaClassReferenceImpl {

	@Override
	public Class<?> getReferencedJavaClass() {
		//Build qualified Java class name and resolve it
		try {
			String qualifiedClassName = getQualifiedNameOfReferencedJavaClass();
			//TODO Problem here: class is not loaded as it is specified at compile time. How to resolve?
			//Think I need to search class path.. :(
			
			//Maybe attempt to load class and see if it works
			
			
			return Class.forName(qualifiedClassName);
//			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//			
//			return classLoader.loadClass(qualifiedClassName);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getQualifiedNameOfReferencedJavaClass() {
		String qualifiedName = "";
		
		List<String> packageNameFragments = getPackageNameFragments();
		
		for (String packageNameFragment : packageNameFragments) {
			qualifiedName += packageNameFragment + ".";
		}
		
		qualifiedName += getClassNameFragment();
		
		return qualifiedName;
	}
}
