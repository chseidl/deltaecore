package eu.vicci.ecosystem.sft.gef.util;

public class ToolTipBuilder {

	public static String getCorrectDescription(String d) {
		String res = "Description: \n";
		if (d == null || d.equals("")) {
			return res + "No description available";
		} else
			return res + d;
	}
}
