package com.cb2.ircmud;

public abstract class PlayerState {
	public static final int STATE_GROUP_WORLD = 1;
	public static final int STATE_GROUP_CHARACTER_CREATION = 2;
	
	public abstract int getStateGroup();
	
	public abstract String getStateName();
}