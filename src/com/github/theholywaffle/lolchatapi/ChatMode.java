/*******************************************************************************
 * Copyright (c) 2014 Bert De Geyter (https://github.com/TheHolyWaffle).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Bert De Geyter (https://github.com/TheHolyWaffle)
 ******************************************************************************/
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
