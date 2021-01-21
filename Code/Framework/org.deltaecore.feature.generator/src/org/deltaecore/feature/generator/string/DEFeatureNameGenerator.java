package org.deltaecore.feature.generator.string;

import org.deltaecore.feature.DEFeature;

public class DEFeatureNameGenerator extends DEStringGenerator<DEFeature, DEInternalFeatureNameRepresentation> {
	private static final String[] qualifiers = {
		"HighSpeed",
		"LowVoltage",
		"PowerSaving",
		"HighPerformance",
		"LowMemory",
		
		"Red",
		"Green",
		"Blue",
		
		"Autonomous",
		"Dependent",
		"Detecting",
		
		"Cloud",
		"Wireless",
		"CyberPhysical",
		
		"Safe",
		"Secure",
		
		"Moving",
		"Static",
		"Stationary",
		
		"Hyper",
		"Precise",
		"Exact",
		"Leniant",
		"Automatic"
	};
	
	private static final String[] baseNames = {
		"TurtleBot",
		"Keyboard",
		"Gamepad",
		"Webservice",
		
		"Infrared",
		"Laser",
		"Ultrasound",
		
		"Steering",
		"ThrottleControl",
	};

	private static final String[] appendices = {
		"Component",
		"Container",
		"Element",
		"Engine",
		"Footprint",
		"Context",
		"Sensor",
		"Actuator",
	};

	@Override
	protected DEInternalFeatureNameRepresentation doGenerate(DEFeature feature, int attemptNumber) {
		if (attemptNumber > 0) {
//			DEDebug.println("Feature: " + attemptNumber);
		}
		
		//Compound name of a qualifier, base name and appendix
		String qualifier = null;
		
		if (getRandom().percentualChance(0.35)) {
			qualifier = getRandomString(qualifiers);
		}
		
		String baseName = getRandomString(baseNames);
		
		String appendix = null;
		
		if (getRandom().percentualChance(0.20)) {
			appendix = getRandomString(appendices);
		}
		
		String salt = null;
		
		if (attemptNumber > 10) {
			salt = createSalt();
		}
		
		return new DEInternalFeatureNameRepresentation(qualifier, baseName, appendix, salt);
	}
	
	private String getRandomString(String[] strings) {
		int maximum = strings.length;
		int index = getRandom().nextInt(maximum);
		
		return strings[index];
	}
	
	private String createSalt() {
		return Integer.toString(getRandom().nextInt(100));
	}
}
