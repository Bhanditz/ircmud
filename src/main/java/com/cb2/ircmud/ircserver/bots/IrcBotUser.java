package com.cb2.ircmud.ircserver.bots;

import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cb2.ircmud.ircserver.Channel;
import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcUser;
import com.cb2.ircmud.ircserver.services.UserService;
import com.github.rlespinasse.slf4j.spring.AutowiredLogger;

public abstract class IrcBotUser extends IrcUser {
	protected boolean parsePrivateMessages = false;
	protected boolean parseChannelMessages = false;
	protected boolean parseJoinMessages = false;
	
	@Autowired
	UserService users;
	@AutowiredLogger
	Logger logger;
	
	
	public IrcBotUser() {

	}
	
	@PostConstruct
	protected void initialize(){
		init();
		boolean success = users.trySetNickname(this,nickname);
		logger.info("Initialized a bot: class: {} nick: {} success: "+success,this.getClass().getSimpleName(), nickname);
	}
	protected abstract void init();
	
	@Override
	public void sendReply(IrcReply reply) {
		if ((parsePrivateMessages || parseChannelMessages) && reply.getCommandName().equals("PRIVMSG")) {
			parsePrivateMessage(reply);
		}
		else if (parseJoinMessages && reply.getCommandName().equals("JOIN")) {
			
		}
	}
	
	public void parsePrivateMessage(IrcReply reply) {
		IrcUser sender = reply.getIrcUserSender();
		if (Channel.isValidPrefix(reply.argument(0).charAt(0))) { //Channel msg
			if (parseChannelMessages) {
				receiveChannelMessage(sender, reply.argument(0), reply.getPostfix());
			}
		}
		else if (parsePrivateMessages) {
			receivePrivateMessage(sender, reply.getPostfix());
		}
	}
	
	Vector<String> parseCommand(String commandString) {
		commandString = commandString.trim();
		Vector<String> ret = new Vector<String>();

		Pattern parameterPattern = Pattern.compile("(?:\"([^\"]*)(?:\"|$))|(\\S+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = parameterPattern.matcher(commandString);
		while (matcher.find()) {
			String paramWithSpaces = matcher.group(1);
			String paramWithoutSpaces = matcher.group(2);
			String param;
			if (paramWithoutSpaces == null) {
				param = paramWithSpaces;
			}
			else {
				param = paramWithoutSpaces;
			}
			ret.add(param);
		}
		return ret;
	}
	
	public void parseJoinMessage(IrcReply join) {
		IrcUser joinedUser = join.getIrcUserSender();
		String channel = join.argument(0);
		receiveJoinMessage(joinedUser, channel);
	}
	
	public void receivePrivateMessage(IrcUser user, String msg) {
		if (user == null) return;
		
		String[] parts = msg.split(" ");
		String command = parts[0].toLowerCase();
		String[] params = null;
		if (parts.length > 1) {
			params = new String[parts.length - 1];
			System.arraycopy(parts, 1, params, 0, parts.length - 1);
		}
		
		handleCommand(user, command, params);
	}
	
	public void receiveChannelMessage(IrcUser sender, String chan, String msg) {}
	public void receiveJoinMessage(IrcUser joinedUser, String channel) {}
	public void handleCommand(IrcUser user, String command, String[] params){}

	@Override
	protected boolean isConnection() {
		return false;
	}
}
