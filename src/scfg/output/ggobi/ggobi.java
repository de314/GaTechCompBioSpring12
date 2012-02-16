package scfg.output.ggobi;

import java.util.*;

import scfg.output.Display;

public class ggobi {
	
	public static void outToXML(List<ggobiVariable> variablesObjs, List<List<List<String>>> recordValues, Display out) {
		XMLdivNode ggobidata = new XMLdivNode("ggobidata", 0);
		XMLdivNode brush = new XMLdivNode("brush", 0);
		brush.addValue("color", "6");
		brush.addValue("glyph", "fc 3");
		ggobidata.addChild(brush);
		XMLdivNode data = new XMLdivNode("data", 1);
		ggobidata.addChild(data);
		data.addValue("name", out.getOutputFileName());
		XMLdivNode variables = new XMLdivNode("variables", 1);
		data.addChild(variables);
		variables.addValue("count", "" + variablesObjs.size());
		for (ggobiVariable var : variablesObjs)
			variables.addChild(var.getXMLdiv()); // handle categories
		XMLdivNode records = new XMLdivNode("records", 1);
		data.addChild(records);
		records.addValue("color", 0+"");
		records.addValue("missingValue", "NA");
		int count = 0;
		for (int i=0;i<recordValues.size();i++) {
			for (List<String> recs : recordValues.get(i)){
				StringBuilder sb = new StringBuilder();
				for (String rec : recs)
					sb.append(rec).append(" ");
				sb.deleteCharAt(sb.length()-1);
				XMLdivNode record = new XMLdivNode("record", 2);
				records.addChild(record);
				record.addValue("color", ""+(4-i));
				tabString(record.getLevel()+1, sb);
				record.appendContent(sb.toString());
				count++;
			}
		}
		records.addValue("count", count + "");
		out.out("<?xml version=\"1.0\"?>");
		out.out("<!DOCTYPE ggobidata SYSTEM \"ggobi.dtd\">");
		out.out(ggobidata.toString()).finalizeDisplay();
	}
	
	private static void tabString(int levels, StringBuilder sb) {
		for (int i=0;i<levels;i++)
			sb.insert(0, '\t');
	}
	
	private static List<String> getRandomList() {
		List<String> list1 = new LinkedList<String>();
		list1.add("" + Math.random());
		list1.add("" + Math.random());
		list1.add("" + Math.random());
		list1.add("" + Math.random());
		return list1;
	}
	
	private static List<List<String>> getSet(int size) {
		List<List<String>> set = new LinkedList<List<String>>();
		for (int i=0;i<size;i++)
			set.add(getRandomList());
		return set;
	}
	
	public static void main(String[] args) {
		List<ggobiVariable> vars = new LinkedList<ggobiVariable>();
		vars.add(new ggobiVariable(ggobiVariable.VariableType.realvariable, "test1"));
		vars.add(new ggobiVariable(ggobiVariable.VariableType.realvariable, "test2"));
		vars.add(new ggobiVariable(ggobiVariable.VariableType.realvariable, "test3"));
		vars.add(new ggobiVariable(ggobiVariable.VariableType.realvariable, "test4"));
		List<List<List<String>>> values = new LinkedList<List<List<String>>>();
		values.add(getSet(10));
		values.add(getSet(10));
		values.add(getSet(10));
		outToXML(vars, values, new Display("gentest.xml", null, false));
	}

}
