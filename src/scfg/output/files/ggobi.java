package scfg.output.files;

import java.util.*;

public class ggobi {
	
	public static void outToXML(List<ggobiVariable> variablesObjs, List<List<String>>[] recordValues) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ggobidata>\n");
		sb.append("\t<brush color=\"6\" glyph=\"fc 3\">\n");
		sb.append("\t\t<variables count=\"7\">\n");
		XMLdivNode ggobidata = new XMLdivNode("ggobidata", 0);
		XMLdivNode brush = new XMLdivNode("brush", 0);
		brush.addValue("color", "6");
		brush.addValue("glyph", "fc 3");
		ggobidata.addChild(brush);
		XMLdivNode variables = new XMLdivNode("variables", 1);
		ggobidata.addChild(variables);
		variables.addValue("count", "" + variablesObjs.size());
		for (ggobiVariable var : variablesObjs)
			variables.addChild(var.getXMLdiv()); // handle categories
		for (int i=0;i<recordValues.length;i++) {
			XMLdivNode records = new XMLdivNode("records", 1);
			ggobidata.addChild(records);
			records.addValue("count", recordValues[i].get(0).size() + "");
			records.addValue("color", i+"");
			records.addValue("missingValue", "NA");
			
		}
	}

}
