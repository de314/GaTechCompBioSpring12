package scfg.output.flot;

public class HtmlTemplateObject {
	
	private String title;
	private String description;
	private String jsFilename;
	private String tableHtml;
	private String filename;
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getJsFilename() {
		return jsFilename;
	}
	
	public String getTableHtml() {
		return tableHtml;
	}
	
	public String getFilename() {
		return filename;
	}

	public HtmlTemplateObject(String title, String description,
			String jsFilename, String tableHtml, String filename) {
		this.title = title;
		this.description = description;
		this.jsFilename = jsFilename;
		this.tableHtml = tableHtml;
		this.filename = filename;
	}

	
}
