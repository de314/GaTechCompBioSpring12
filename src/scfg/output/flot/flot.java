package scfg.output.flot;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import scfg.output.Display;

public class flot {
	
	public static final String TEMPLATE = "flotJS/template.php";
	public static final String HTML_DIR = "flotJS/output/";
	
	public static void outToHtml(HtmlTemplateObject tmpl, Display htmlOut) {
		try {
			Scanner scan = new Scanner(new File(tmpl.getFilename()));
			StringBuilder sb = new StringBuilder();
			while(scan.hasNextLine())
				sb.append(scan.nextLine()).append("\n");
			String html = sb.toString();
			html = html.replace("@@JS_FILENAME@@", tmpl.getJsFilename());
			html = html.replace("@@TABLE@@", tmpl.getTableHtml());
			html = html.replace("@@PLOT_TITLE@@", tmpl.getTitle());
			html = html.replace("@@DESCRIPTION@@", tmpl.getDescription());
			htmlOut.out(html).finalizeDisplay();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static String generateTableHtml(Map<String, Map<String, List<String>>> series) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\"><tr><th>Name</th><th>x-axis</th><th>y-axis</th></tr>");
		List<String> keys = new ArrayList<String>();
		for (String key : series.keySet())
			keys.add(key);
		Collections.sort(keys);
		for (String key : keys)
			if (!key.equals("keys"))
				sb.append("<tr><td>").append(key).append("</td><td class=\"d\"onclick=\"selTDX($(this), ").append(key).append(");\">&emsp;</td><td class=\"d\" onclick=\"selTDY($(this), ").append(key).append(");\">&emsp;</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}
	
	public static void outToJsFileEMH(flotEMH flotSet, Display out) {
		outToJsFileEMH(flotSet.getSeries(), out);
	}
	
	public static void outToJsFileEMH(Map<String, List<String>> series, Display out) {
		StringBuilder sb = new StringBuilder("// ");
		sb.append(out.getOutputFileName()).append("\n\n");
		for (Entry<String, List<String>> me : series.entrySet()) {
			if (me.getKey().equals("keys")) {
				Map<String, List<String>> map = new HashMap<String, List<String>>();
				map.put("keys", me.getValue());
				appendMap(me.getKey(), map, sb);
			} else {
				sb.append("var ").append(me.getKey()).append(" = [ ");
				for (String val : me.getValue())
					sb.append(val).append(", ");
				if (me.getValue().size() > 0)
					sb.delete(sb.length()-2, sb.length());
				sb.append(" ];\n");
			}
		}
		sb.append("\n$(document).ready(function(){\n\tselXdata = 0;\n\tselYdata = 0;\n});\n");
		out.out(sb.toString()).finalizeDisplay();
	}
	
	public static void outToJsFileSingle(flotSingle flotSet, Display out) {
		outToJsFileSingle(flotSet.getSeries(), flotSet.getLabel(), out);
	}
	
	public static void outToJsFileSingle(Map<String, List<String>> series, String label, Display out) {
		StringBuilder sb = new StringBuilder("// ");
		sb.append(out.getOutputFileName()).append("\n\n");
		String key = "";
		for (Entry<String, List<String>> me : series.entrySet()) {
			if (me.getKey().equals("keys")) {
				Map<String, List<String>> map = new HashMap<String, List<String>>();
				map.put("keys", me.getValue());
				appendMap(me.getKey(), map, sb);
			} else {
				key = me.getKey();
				sb.append("var ").append(me.getKey()).append(" = [ ");
				for (String val : me.getValue())
					sb.append(val).append(", ");
				if (me.getValue().size() > 0)
					sb.delete(sb.length()-2, sb.length());
				sb.append(" ];\n");
			}
		}
		sb.append("\n$(document).ready(function(){\n\tselXdata = 0;\n\tselYdata = 0;\n");
		sb.append("\tpredefinedData = [ { label: \"").append(label).append("\", color: \"rgb(255, 128, 0)\", data : ").append(key).append(" } ];\n");
		sb.append("});\n");
		out.out(sb.toString()).finalizeDisplay();
	}
	
	public static void outToJsFileInt(flotInt flotSet, Display out) {
		outToJsFileInt(flotSet.getSeries(), out);
	}
	
	public static void outToJsFileInt(Map<String, Map<String, List<String>>> series, Display out) {
		StringBuilder sb = new StringBuilder("// ");
		sb.append(out.getOutputFileName()).append("\n\n");
		int count = 0;
		String selXdata = "";
		String selYdata = "";
		for (Entry<String, Map<String, List<String>>> me : series.entrySet()) {
			if (count == 0 && !me.getKey().equals("keys"))
				selXdata = me.getKey();
			if (count == 1 && !me.getKey().equals("keys"))
				selYdata = me.getKey();
			if (!me.getKey().equals("keys"))
				count++;
			appendMap(me.getKey(), me.getValue(), sb);
		}
		sb.append("\n$(document).ready(function(){\n\tselXdata = ").append(selXdata).append(";\n\tselYdata = ").append(selYdata).append(";\n});\n");
		out.out(sb.toString()).finalizeDisplay();
	}
	
	private static void appendMap(String name, Map<String, List<String>> map, StringBuilder sb) {
		sb.append("var ").append(name).append(" = { ");
		for (Entry<String, List<String>> set : map.entrySet()) {
			sb.append(set.getKey()).append(" : [ ");
			for (String val : set.getValue())
				sb.append(val).append(", ");
			if (set.getValue().size() > 0)
				sb.delete(sb.length()-2, sb.length());
			sb.append(" ], ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(" };\n");
	}

	private static List<String> getRandomSeries(int size) {
		List<String> list = new LinkedList<String>();
		for (int i=0;i<size;i++)
			list.add("" + Math.random());
		return list;
	}

	private static List<String> getRandomPairSeries(int size) {
		List<String> list = new LinkedList<String>();
		for (int i=0;i<size;i++)
			list.add("[" + Math.random() + ", " + Math.random() + "]");
		return list;
	}
	
	private static Map<String, List<String>> getRandomSet(int size) {
		Map<String, List<String>> rtn = new HashMap<String, List<String>>();
		rtn.put("easy", getRandomSeries(size));
		rtn.put("medium", getRandomSeries(size));
		rtn.put("hard", getRandomSeries(size));
		return rtn;
	}

	private static List<String> getNames(int size) {
		List<String> list = new LinkedList<String>();
		for (int i=0;i<size;i++)
			list.add("\"item" + i + "\"");
		return list;
	}

	public static void main(String[] args) {
		Map<String, List<String>> series = new HashMap<String, List<String>>();
		series.put("easy", getRandomPairSeries(10));
		series.put("medium", getRandomPairSeries(10));
		series.put("hard", getRandomPairSeries(10));
		series.put("keys", getNames(10));
		outToJsFileEMH(series, new Display("flotJS/genTestEMH.js", null, false));
		
		series = new HashMap<String, List<String>>();
		series.put("predefinedSeries", getRandomPairSeries(50));
		series.put("keys", getNames(50));
		outToJsFileSingle(series, "Test Series", new Display("flotJS/test/genTestSingle.js", null, false));
		
		Map<String, Map<String, List<String>>> seriesInt = new HashMap<String, Map<String, List<String>>>();
		for (int i=0;i<10;i++)
			seriesInt.put("test" + i, getRandomSet(12));
		series = new HashMap<String, List<String>>();
		series.put("keys", getNames(12));
		seriesInt.put("keys", series);
		outToJsFileInt(seriesInt, new Display("flotJS/test/genTestInt.js", null, false));

		HtmlTemplateObject tmpl = new HtmlTemplateObject("Test Html File", "Nothing special.", "genTestInt.js", generateTableHtml(seriesInt), "flotJS/template.html");
		outToHtml(tmpl, new Display("flotJS/test/testInt.html", null, false));

		tmpl = new HtmlTemplateObject("Test Single Html File", "Nothing special.", "genTestSingle.js", "", "flotJS/template.html");
		outToHtml(tmpl, new Display("flotJS/test/testSingle.html", null, false));
	}
}
