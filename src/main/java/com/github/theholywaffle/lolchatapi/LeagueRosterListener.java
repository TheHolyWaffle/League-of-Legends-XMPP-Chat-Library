package com.github.theholywaffle.lolchatapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.JDOMException;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class LeagueRosterListener implements RosterListener {

	private HashMap<String, Presence.Type> typeUsers = new HashMap<>();
	private HashMap<String, Presence.Mode> modeUsers = new HashMap<>();
	private HashMap<String, LolStatus> statusUsers = new HashMap<>();
	private LolChat api;

	private boolean added;

	public LeagueRosterListener(LolChat api) {
		this.api = api;
	}

	public void entriesAdded(Collection<String> e) {
		if (!added) {
			for (String s : e) {
				Friend f = api.getFriendById(s);
				if (f.isOnline()) {
					typeUsers.put(s, Presence.Type.available);
					modeUsers.put(s, f.getChatMode().mode);
					statusUsers.put(s, f.getStatus());
				} else {
					typeUsers.put(s, Presence.Type.unavailable);
				}
			}
			added = true;
		}
	}

	public void entriesDeleted(Collection<String> e) {
	}

	public void entriesUpdated(Collection<String> e) {
	}

	public void presenceChanged(Presence p) {
		String from = p.getFrom();
		if (from != null) {
			from = StringUtils.parseBareAddress(from);
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

					if (p.getStatus() != null) {
						try {
							LolStatus previousStatus = statusUsers.get(from);
							LolStatus newStatus = new LolStatus(p.getStatus());
							if (previousStatus != null
									&& !newStatus.equals(previousStatus)) {
								l.onFriendStatusChange(friend);
							}
						} catch (JDOMException | IOException e) {
						}
					}

				}

				typeUsers.put(from, p.getType());
				modeUsers.put(from, p.getMode());
				if (p.getStatus() != null) {
					try {
						statusUsers.put(from, new LolStatus(p.getStatus()));
					} catch (JDOMException | IOException e) {
					}
				} else {
					statusUsers.put(from, null);
				}
			}
		}
	}

	public boolean isLoaded() {
		return added;
	}

}
