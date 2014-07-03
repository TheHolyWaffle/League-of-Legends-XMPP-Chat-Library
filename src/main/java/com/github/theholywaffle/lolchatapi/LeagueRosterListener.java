package com.github.theholywaffle.lolchatapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.JDOMException;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
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
	private XMPPConnection connection;

	public LeagueRosterListener(LolChat api, XMPPConnection connection) {
		this.api = api;
		this.connection = connection;
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
		System.out.println("Entries added"); // TODO remove
	}

	public void entriesDeleted(Collection<String> e) {
		System.out.println("Entries deleted"); // TODO remove
	}

	public void entriesUpdated(Collection<String> e) {
	}

	public boolean isLoaded() {
		return added;
	}

	public void presenceChanged(Presence p) {
		String from = p.getFrom();
		if (from != null) {
			p = connection.getRoster().getPresence(p.getFrom());
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
				}
			}
		}
	}

}
