package com.cb2.ircmud;

import java.util.ArrayList;

public class IrcReply {

	private ArrayList<String> arguments = new ArrayList<String>();
	private String postfix = null;
	private String sender = null;
	private String command = null;
	
	public IrcReply(Connection sender, String command, String string) {
		this(sender, string);
		this.command = command;
	}

	public IrcReply(String sender, String string) {
		this.sender = sender;
		this.init(string);
	}
	public IrcReply(String sender, String command, String string) {
		this.sender = sender;
		this.command = command;
		this.init(string);
	}

	public IrcReply(Connection sender, String string) {
		this(sender);
		this.init(string);
	}
	
	public IrcReply(Connection sender) {
		this.sender = sender.getRepresentation();
	}



	public void init(String string) {
		this.arguments.clear();
		this.postfix = new String();
		
		String[] tokens = string.split(":");
		String prefix = "";
		String postfix = "";
		
		switch(tokens.length) {
			case 1:
				prefix = tokens[0];
				break;
			case 2:
				prefix = tokens[0];
				postfix = tokens[1];
				break;
			case 3:
				prefix = tokens[1];
				postfix = tokens[2];
				break;
			default:
		}
		
		this.postfix = postfix;

		tokens = prefix.split(" ");
		int i = 0;
		if ( this.sender == null ) {
			this.sender = tokens[i];
			i++;
		}
		if ( this.command == null ) {
			this.command = tokens[i];
			i++;
		}
		
		for( ; i < tokens.length; i++  ) {
			this.arguments.add(tokens[i]);
		}
	}
	
	public String argument( int n ) {
		if ( n < 0 ) return null;

		if ( n > arguments.size() ) return null;

		return this.arguments.get(n);
	}

	public String postfix( int n ) {
		return this.postfix;
	}
	
	public String sender() {
		return this.sender;
	}

	public String command() {
		return this.command;
	}
	
	public void pushArgument(String string) {
		this.arguments.add(string);
	}

	public String toString() {
		String string = ":" + this.sender + " "  + this.command + " ";
		for( String str : this.arguments )
			string = string + str + " ";

		return string + ":" + this.postfix;
	}
	public void var_dump() {
		System.out.println("DEBUG: IrcReply::var_dump()");
		System.out.println("Sender = "+sender);
		System.out.println("Command = "+this.command);
		for( String str : this.arguments )
			System.out.println("Argument[] = "+str);
		System.out.println("Postfix = "+this.postfix);
	}
}
