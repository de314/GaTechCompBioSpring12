package scfg.output;

import java.util.*;

public class CommandLineMenu {

	public static final int ARG_SPACE = 2;

	private List<CommandLineMenuArg> args;
	private String program;
	private String description;
	private String comments;

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public CommandLineMenu(String program) {
		this(program, null, null);
	}

	public CommandLineMenu(String program, String description) {
		this(program, description, null);
	}

	public CommandLineMenu(String program, String description, String comments) {
		this.program = program;
		this.description = description;
		this.comments = comments;
		args = new LinkedList<CommandLineMenuArg>();
	}

	public boolean add(String arg, String description) {
		CommandLineMenuArg cmarg = new CommandLineMenuArg(arg, description);
		if (!args.contains(cmarg)) {
			args.add(cmarg);
			return true;
		}
		return false;
	}

	public String remove(String arg) {
		CommandLineMenuArg cmarg = new CommandLineMenuArg(arg);
		int index = args.indexOf(cmarg);
		String rtn = null;
		if (index >= 0) {
			rtn = args.get(index).description;
			args.remove(index);
		}
		return rtn;
	}

	public boolean contains(String arg) {
		return args.contains(new CommandLineMenuArg(arg, null));
	}

	public String get(String arg) {
		int index = args.indexOf(new CommandLineMenuArg(arg, null));
		if (index >= 0)
			return args.get(index).description;
		return null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("\n");
		sb.append(box(program, "   ")).append("\n\n");
		sb.append(underline("Descrption", ""));
		sb.append("\n").append(wordWrap(description, 79, 0, "")).append("\n\n");
		int maxLength = 0;
		for (CommandLineMenuArg cmarg : args)
			maxLength = Math.max(maxLength, cmarg.arg.length());
		for (CommandLineMenuArg cmarg : args) {
			sb.append("   ").append(postBuffer(cmarg.arg, maxLength));
			sb.append(wordWrap(cmarg.description, 79,
					maxLength + 3 + ARG_SPACE, "   "));
			sb.append("\n");
		}
		sb.append("\n").append(underline("Comments", "")).append("\n");
		sb.append(wordWrap(comments, 79, 0, ""));
		return sb.toString();
	}

	public String box(String contents, String prefix) {
		StringBuilder sb = new StringBuilder(prefix);
		for (int i = 0; i < contents.length() + 4; i++)
			sb.append("-");
		sb.append("\n").append(prefix);
		sb.append("| ").append(contents).append(" |");
		sb.append("\n").append(prefix);
		for (int i = 0; i < contents.length() + 4; i++)
			sb.append("-");
		return sb.toString();
	}

	public String underline(String contents, String prefix) {
		StringBuilder sb = new StringBuilder(prefix);
		sb.append(contents);
		sb.append("\n").append(prefix);
		for (int i = 0; i < contents.length(); i++)
			sb.append("-");
		return sb.toString();
	}

	public String postBuffer(String contents, int maxLength) {
		StringBuilder sb = new StringBuilder(contents);
		while (sb.length() < maxLength + ARG_SPACE) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public String wordWrap(String contents, int length, int currentIndex,
			String wrapPrefix) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() < currentIndex)
			sb.append(" ");
		String front = sb.toString();
		sb = new StringBuilder();
		int left = length - currentIndex;
		if (contents.length() > length) {
			sb.append(contents.substring(0, left)).append("\n");
			contents = contents.substring(left);
			while (contents.length() + currentIndex > length) {
				sb.append(front).append(wrapPrefix).append(
						contents.substring(0, left - wrapPrefix.length()))
						.append("\n");
				contents = contents.substring(left - wrapPrefix.length());
			}
			sb.append(front).append(wrapPrefix).append(contents);
		} else
			sb.append(contents);
		return sb.toString();
	}

	private static class CommandLineMenuArg implements
			Comparable<CommandLineMenuArg> {
		public String arg;
		public String description;

		public CommandLineMenuArg(String arg) {
			this(arg, "(endefined functionality)");
		}

		public CommandLineMenuArg(String arg, String description) {
			this.arg = arg;
			this.description = description;
		}

		@Override
		public int compareTo(CommandLineMenuArg that) {
			return this.arg.compareTo(that.arg);
		}
	}

	public static void main(String[] args) {
		String program = "Command Line Menu";
		String description = "This class creates a formatted command line menu.";
		String comments = "Just create the object and print  the toString() method to console." +
				" Then you can do it again, and again... and again...";
		
		CommandLineMenu cm = new CommandLineMenu(program, description, comments);
		cm.add("-h", "Display the help menu");
		cm.add("-v", "Display verbose output");
		cm
				.add(
						"-wordWrap [length]",
						"This is a test of a really long word wrap. A really really long word wrap. And when I say really really long, I mean really really long.");
		cm.add("--debug", "Display debug output.");
		System.out.println(cm.toString());
	}
}
