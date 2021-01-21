package org.deltaecore.core.decore.resource.decore.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.resource.decore.mopp.DecorePrinter2;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEDecorePrintUtil {
	/**
	 * @param pathAndFilename Path and filename to a file with "decore_plain" extension (relative to the workspace).
	 * @return
	 */
	public static String printDecorePlain(String pathAndFilename) {
		IFile file = ResourceUtil.getLocalFile(pathAndFilename);
		return printDecorePlain(file);
	}
	
	/**
	 * @param file A file with "decore_plain" extension.
	 */
	public static String printDecorePlain(IFile file) {
		DEDelta deltaModule = EcoreIOUtil.loadModel(file);
		return printDeltaModule(deltaModule);
	}
	
	public static String printDeltaModule(DEDelta deltaModule) {
		try {
			//Having proxies in input can cause strange behavior - resolve!
			EcoreUtil.resolveAll(deltaModule);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DecorePrinter2 printer = new DecorePrinter2(out, null);
			printer.print(deltaModule);
			return out.toString();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
