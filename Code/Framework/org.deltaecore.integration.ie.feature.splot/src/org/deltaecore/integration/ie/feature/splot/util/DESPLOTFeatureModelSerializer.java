package org.deltaecore.integration.ie.feature.splot.util;

import java.util.Collection;
import java.util.Set;

import constraints.PropositionalFormula;
import fm.FeatureGroup;
import fm.FeatureModel;
import fm.FeatureTreeNode;
import fm.RootNode;
import fm.SolitaireFeature;

public class DESPLOTFeatureModelSerializer {
	private static final String endl = System.lineSeparator();
	
	public static String serializeFeatureModel(FeatureModel featureModel) {
		String output = "";
		
		output += "<feature_model name=\"" + featureModel.getName() + "\">" + endl;
		
		
		Set<String> metaDataKeys = featureModel.getMetaDataKeys();
		
		output += "\t<meta>" + endl;
		for (String metaDataKey : metaDataKeys) {
			String metaData = featureModel.getMetaData(metaDataKey);
			output += "\t\t<data name=\"" + metaDataKey + "\">" + metaData + "</data>" + endl;
		}
		output += "\t</meta>" + endl;

		
		output += "\t<feature_tree>" + endl;
		output += serializeFeatureTree(featureModel);
		output += "\t</feature_tree>" + endl;
		
		
		Collection<PropositionalFormula> constraints = featureModel.getConstraints();
		
		output += "\t<constraints>" + endl;
		for (PropositionalFormula constraint : constraints) {
			output += constraint.getName() + ": " + constraint.getFormula() + endl;
		}
		output += "\t</constraints>" + endl;
		
		
		output += "</feature_model>" + endl;
		
		return output;
	}
	
	protected static String serializeFeatureTree(FeatureModel featureModel) {
		FeatureTreeNode rootNode = featureModel.getRoot();
		return serializeFeatureTreeNode(rootNode, 0);
	}
	
	private static String serializeFeatureTreeNode(FeatureTreeNode node, int tab) {
		String output = "";
		
		//Indent
		for (int i = 0 ; i < tab ; i++) {
			output += "\t";
		}

		//Print
		if (node instanceof FeatureGroup) {
			FeatureGroup featureGroup = (FeatureGroup) node;
			
			output += ":g [" + featureGroup.getMin() + "," + featureGroup.getMax() + "]" + endl;
		} else {
			output += getFeatureType(node) + node.getName() + "(" + node.getID() + ")" + endl;
		}

		//Recursive call
		for (int i = 0 ; i < node.getChildCount() ; i++) {
			FeatureTreeNode childNode = (FeatureTreeNode) node.getChildAt(i);
			output += serializeFeatureTreeNode(childNode, tab + 1);
		}
		
		return output;
	}
	
	private static String getFeatureType(FeatureTreeNode node) {
		if (node instanceof RootNode) {
			return ":r ";
		}
		
		if (node instanceof SolitaireFeature) {
			SolitaireFeature solitaireFeature = (SolitaireFeature) node;
			
			if (solitaireFeature.isOptional()) {
				return ":o ";
			} else {
				return ":m ";
			}
		}
	
		//Grouped features have no type.
		return "";
	}
}
