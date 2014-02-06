package com.github.theholywaffle.lolchatapi;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;

public enum ChatMode {

	AVAILABLE(Presence.Mode.chat),
	AWAY(Presence.Mode.away),
	BUSY(Presence.Mode.dnd);

	public Mode mode;

	ChatMode(Presence.Mode mode) {
		this.mode = mode;
	}

}
