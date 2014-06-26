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
	
	public LeagueRosterListener(LolChat api){
		this.api=api;
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
					if (typeUsers.containsKey(from)) {
						Presence.Type previous = typeUsers.get(from);
						if (p.getType() == Presence.Type.available && previous != Presence.Type.available) {
							l.onFriendJoin(friend);
						} else if (p.getType() == Presence.Type.unavailable
								&& previous != Presence.Type.unavailable) {
							l.onFriendLeave(friend);
						}
					} else if (p.getType() == Presence.Type.available) {
						l.onFriendJoin(friend);
					}

					if (modeUsers.containsKey(from)) {
						Presence.Mode previous = modeUsers.get(from);
						if (p.getMode() == Presence.Mode.chat && previous != Presence.Mode.chat) {
							l.onFriendAvailable(friend);
						} else if (p.getMode() == Presence.Mode.away && previous != Presence.Mode.away) {
							l.onFriendAway(friend);
						} else if (p.getMode() == Presence.Mode.dnd && previous != Presence.Mode.dnd) {
							l.onFriendBusy(friend);
						}
					}

					if (statusUsers.containsKey(from)) {
						String previous = statusUsers.get(from);
						if (!p.getStatus().equals(previous)) {
							l.onFriendStatusChange(friend);
						}
					}

					typeUsers.put(from, p.getType());
					modeUsers.put(from, p.getMode());
					statusUsers.put(from, p.getStatus());
				}
			}
		}
	}
	
	public boolean isLoaded(){
		return added;
	}

}
