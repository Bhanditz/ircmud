package com.cb2.ircmud;

import java.util.ArrayList;
import com.cb2.ircmud.Connection;

public class Channel {

	private ArrayList<Connection> channelMembers = new ArrayList<Connection>();
	private String topic;
	protected String name;
	
	public Channel(String name) {
		this.name = name;
	}
	
	public void sendRawStringAllExcept(Connection except, String msg) {
		for (Connection con : channelMembers) {
			if (con != except) con.sendRawString(msg);
		}
	}
	
	public void sendPrivateMessage(Connection sender, String msg) {
		for (Connection con : channelMembers) {
			if (con != sender) con.sendPrivateMessage(sender.getRepresentation(), this.name, msg);
		}
	}
	
	public void sendRawStringAll(String toSend) {
		sendRawStringAllExcept(null, toSend);
	}
	
	public void addConnection(Connection con) {
		channelMembers.add(con);
		sendRawStringAll(":" + con.getRepresentation() + " JOIN "+ this.name);
		con.sendRawString(":"+Server.globalServerName+" 332 "+con.nick+" "+this.name+" :"+this.topic);

		String userlist = "";
		for(Connection _con : channelMembers) userlist = userlist + " " + _con.nick; 
		con.sendRawString(":"+Server.globalServerName+" 353 "+con.nick+" @ "+this.name+" :"+userlist);

		con.sendRawString(":"+Server.globalServerName+" 366 "+con.nick+" "+this.name+" :End of /NAMES list.");
	}
	
	public void removeConnection(Connection con) {
		channelMembers.remove(con);
	}
	
}
