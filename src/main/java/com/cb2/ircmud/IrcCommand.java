package com.cb2.ircmud;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public enum IrcCommand {
	NICK(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments = arguments;

			this.argumentMap.put("command",  "NICK");
			this.argumentMap.put("nick", arguments[0]);
			
			var_dump(con, prefix);
		}
	},
	USER(1, 4) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "USER");
			this.argumentMap.put("nick",     arguments[0]);
			this.argumentMap.put("mode",     arguments[1]);
			this.argumentMap.put("unused",   arguments[2]);
			this.argumentMap.put("realname", arguments[3]);

			var_dump(con, prefix);
		}
	},
	QUIT(1, 1) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "QUIT");
			this.argumentMap.put("quitMessage", arguments[0]);

			var_dump(con, prefix);
		}
	},
	MODE(0, 2) {
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "MODE");
			this.argumentMap.put("nick", arguments[0]);
			this.argumentMap.put("mode", arguments[1]);

			var_dump(con, prefix);
		}
	},
	JOIN(1, 2){
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "JOIN");
			this.argumentMap.put("channels", arguments[0]);
			this.argumentMap.put("passwords", arguments[1]);

			var_dump(con, prefix);
		}
	},
	PART(1, 2){
		@Override
		public void init(Connection con, String prefix, String[] arguments) throws Exception {
			this.arguments  = arguments;

			this.argumentMap.put("command",  "JOIN");
			this.argumentMap.put("channels", arguments[0]);
			this.argumentMap.put("leaveMessage", arguments[1]);

			var_dump(con, prefix);
		}
	};

	public void var_dump(Connection con, String prefix) {
		System.out.println("DEBUG: Command " + argument("command") + " from  " +con.hostname);
		System.out.println("DEBUG: prefix:" + prefix);
		
		Iterator it = this.argumentMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			System.out.println("DEBUG: "+pairs.getKey() + " = " + pairs.getValue());
			it.remove();
		}
	}
	
	public String[] arguments;
	public HashMap<String, String> argumentMap;
	
	private int minCmds;
	private int maxCmds;
	
	
	public int getMin() { return minCmds; }
	public int getMax() { return maxCmds; }
	
	private IrcCommand(int min, int max) {
		minCmds = min;
		maxCmds = max;
	}
	
	public String argument(String key) {
		if (key != null && this.argumentMap.containsKey(key))
			return this.argumentMap.get(key);
		else
			return null;
	}
	public String argument(int i) {
		if (0 < i && i < this.arguments.length)
			return this.arguments[i];
		else
			return null;
	}
	
	public abstract void init(Connection con, String prefix, String[] arguments) throws Exception;
}