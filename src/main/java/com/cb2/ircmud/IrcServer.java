package com.cb2.ircmud;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.IOException;
import com.cb2.ircmud.LoginBot;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.cb2.ircmud.LoginBot;

public class IrcServer {
	
	private static ServerSocket serverSocket;
	public static String globalServerName;
	public static int    globalServerPort;
	public static String globalServerInfo = "";
	public static final String VERSION = "0.02";
	
	private static LoginBot loginBot = new LoginBot("LoginBot", "LoginBot");
	private static Map<String, IrcUser> userNicknameMap = new HashMap<String, IrcUser>();
	private static Map<String, Channel>       channelMap = new HashMap<String, Channel>();
		
	public static void init(String _globalServerName, int _globalServerPort) throws IOException {
		System.out.println("Initializing IrcServer("+_globalServerName+":"+_globalServerPort+")");

		globalServerPort = _globalServerPort;
		globalServerName = _globalServerName;

		System.out.println("IrcServer: Initializing ServerSocket");
		serverSocket = new ServerSocket(globalServerPort);

		System.out.println("IrcServer: Initializing "+Config.WorldChannel);

		Channel worldChannel = new Channel(Config.WorldChannel);
		channelMap.put(worldChannel.name, worldChannel);
		
		trySetNickname(loginBot, loginBot.getUsername());
		
		PingService.init(Config.connectionPingTime, Config.connectionPingTimeout);
	}
	
	public static boolean trySetNickname(IrcUser user, String nick) {
		nick = nick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(nick)) return false;
			userNicknameMap.put(nick, user);
		}
		return true;
	}
	
	public static boolean trySetNickname(IrcUser user, String newNick, String oldNick) {
		newNick = newNick.toLowerCase();
		oldNick = oldNick.toLowerCase();
		synchronized (userNicknameMap) {
			if (userNicknameMap.containsKey(newNick)) return false;
			if (userNicknameMap.containsKey(oldNick)) {
				userNicknameMap.remove(oldNick);
			}
			userNicknameMap.put(newNick, user);
		}
		return true;
	}
	
	public static Channel findChannel(String channelName) {
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return null;
			return channelMap.get(channelName);
		}
	}
	public static IrcUser findUserByNickname(String nickName) {
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return null;
			return userNicknameMap.get(nickName);
		}
	}
	public static void dropUser(String nickName) {
		System.out.println("DEBUG: IrcServer::dropUser("+nickName+")");
		nickName = nickName.toLowerCase();
		synchronized (userNicknameMap) {
			if (!userNicknameMap.containsKey(nickName)) return;

			userNicknameMap.get(nickName).quit("Quitting");
			userNicknameMap.remove(nickName);
		}
	}
	public static void dropChannel(String channelName) {
		System.out.println("DEBUG: IrcServer::dropChannel("+channelName+")");
		channelName = channelName.toLowerCase();
		synchronized (channelMap) {
			if (!channelMap.containsKey(channelName)) return;

			channelMap.remove(channelName);
		}
	}
	
	public static void addChannel(Channel  chan) {
		System.out.println("DEBUG: IrcServer::addChannel("+chan.getName()+")");
		channelMap.put(chan.getName().toLowerCase(), chan);
	}
	
	public static void run() {
		System.out.println("IrcServer: Starting server loop");

		while (true) {
			try {
				Socket	   socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				Thread	   thread = new Thread(connection);
				thread.start();
			} catch(IOException e) {
				System.err.println("IrcServer: IOException at Server.run: " + e.getMessage());
			}
		}
	}
	
	public static boolean close() throws IOException {

		serverSocket.close();
	
		return false;
	}
}
