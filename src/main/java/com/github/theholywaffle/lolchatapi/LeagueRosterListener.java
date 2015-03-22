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
import com.github.theholywaffle.lolchatapi.wrapper.Friend.FriendStatus;

public class LeagueRosterListener implements RosterListener {

	private final HashMap<String, Presence.Type> typeUsers = new HashMap<>();
	private final HashMap<String, Presence.Mode> modeUsers = new HashMap<>();
	private final HashMap<String, LolStatus> statusUsers = new HashMap<>();
	private final HashMap<String, FriendStatus> friendStatusUsers = new HashMap<>();
	private final LolChat api;

	private boolean added;
	private final XMPPConnection connection;

	public LeagueRosterListener(LolChat api, XMPPConnection connection) {
		this.api = api;
		this.connection = connection;
	}

	public void entriesAdded(Collection<String> e) {
		for (final String s : e) {
			final Friend f = api.getFriendById(s);
			if (!added && !api.isLoaded()) {
				if (f.isOnline()) {
					typeUsers.put(s, Presence.Type.available);
					modeUsers.put(s, f.getChatMode().mode);
					statusUsers.put(s, f.getStatus());
				} else {
					typeUsers.put(s, Presence.Type.unavailable);
				}
			}
			if (f.getGroup() == null) {
				api.getDefaultFriendGroup().addFriend(f);
			}

			if (f.getFriendStatus() != FriendStatus.MUTUAL_FRIENDS) {
				friendStatusUsers.put(s, f.getFriendStatus());
			}
		}
		added = true;
	}

	public void entriesDeleted(Collection<String> entries) {
		for (final String s : entries) {
			friendStatusUsers.put(s, null);
			for (final FriendListener l : api.getFriendListeners()) {
				String name = null;
				if (api.getRiotApi() != null) {
					try {
						name = api.getRiotApi().getName(s);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
				l.onRemoveFriend(s, name);
			}
		}
	}

	public void entriesUpdated(Collection<String> e) {
		for (final String s : e) {
			final Friend f = api.getFriendById(s);
			final FriendStatus previous = friendStatusUsers.get(s);
			if (((previous != null && previous != FriendStatus.MUTUAL_FRIENDS)
					|| previous == null || !api.isLoaded())
					&& f.getFriendStatus() == FriendStatus.MUTUAL_FRIENDS) {
				onNewFriend(f);
			}
			friendStatusUsers.put(s, f.getFriendStatus());
		}
	}

	public boolean isLoaded() {
		return added;
	}

	private void onNewFriend(Friend f) {
		for (final FriendListener l : api.getFriendListeners()) {
			l.onNewFriend(f);
		}
	}

	public void presenceChanged(Presence p) {
		String from = p.getFrom();
		if (from != null) {
			p = connection.getRoster().getPresence(from);
			from = StringUtils.parseBareAddress(from);
			final Friend friend = api.getFriendById(from);
			if (friend != null) {
				for (final FriendListener l : api.getFriendListeners()) {
					final Presence.Type previousType = typeUsers.get(from);
					if (p.getType() == Presence.Type.available
							&& (previousType == null || previousType != Presence.Type.available)) {
						l.onFriendJoin(friend);
					} else if (p.getType() == Presence.Type.unavailable
							&& (previousType == null || previousType != Presence.Type.unavailable)) {
					    statusUsers.remove(friend);
					    typeUsers.remove(friend);
					    modeUsers.remove(friend);
						l.onFriendLeave(friend);
						return;
					}

					final Presence.Mode previousMode = modeUsers.get(from);
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
							final LolStatus previousStatus = statusUsers
									.get(from);
							final LolStatus newStatus = new LolStatus(
									p.getStatus());
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
