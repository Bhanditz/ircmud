package com.cb2.ircmud.event;

import com.cb2.ircmud.domain.Item;

public class LookEvent extends Event {
	public LookEvent(Item sender, Item target) {
		super(Event.Type.Look, sender, target);
	}

}
