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

import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class LeagueRosterListener implements RosterListener {

	private HashMap<String, Presence.Type> typeUsers = new HashMap<>();
	private HashMap<String, Presence.Mode> modeUsers = new HashMap<>();
	private HashMap<String, String> statusUsers = new HashMap<>();
	private LolChat api;

	private boolean added;

	public LeagueRosterListener(LolChat api) {
		this.api = api;
	}

	public void entriesAdded(Collection<String> e) {
		added = true;
	}

	public void entriesDeleted(Collection<String> e) {
	}

	public void entriesUpdated(Collection<String> e) {
	}

	public void presenceChanged(Presence p) {
		String from = p.getFrom();
		if (from != null) {
			Friend friend = api.getFriendById(from);
			if (friend != null) {
				for (FriendListener l : api.getFriendListeners()) {
					Presence.Type previousType = typeUsers.get(from);
					if (p.getType() == Presence.Type.available
							&& (previousType == null || previousType != Presence.Type.available)) {
						l.onFriendJoin(friend);
					} else if (p.getType() == Presence.Type.unavailable
							&& (previousType == null || previousType != Presence.Type.unavailable)) {
						l.onFriendLeave(friend);
					}

					Presence.Mode previousMode = modeUsers.get(from);
					if (p.getMode() == Presence.Mode.chat
							&& (previousMode == null || previousMode != Presence.Mode.chat)) {
						l.onFriendAvailable(friend);
					} else if (p.getMode() == Presence.Mode.away
							&& (previousMode == null || previousMode != Presence.Mode.away)) {
						l.onFriendAway(friend);
					} else if (p.getMode() == Presence.Mode.dnd
							&& (previousMode == null || previousMode != Presence.Mode.dnd)) {
						l.onFriendBusy(friend);
					}

					String previousStatus = statusUsers.get(from);
					if (previousStatus == null
							|| !p.getStatus().equals(previousStatus)) {
						l.onFriendStatusChange(friend);
					}

					typeUsers.put(from, p.getType());
					modeUsers.put(from, p.getMode());
					statusUsers.put(from, p.getStatus());
				}
			}
		}
	}

	public boolean isLoaded() {
		return added;
	}

}
