package com.github.theholywaffle.lolchatapi.wrapper;

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

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.util.StringUtils;

import com.github.theholywaffle.lolchatapi.LolChat;

/**
 * Represents a group that can contain multiple friends
 *
 */
public class FriendGroup extends Wrapper<RosterGroup> {

	public FriendGroup(LolChat api, XMPPConnection con, RosterGroup group) {
		super(api, con, group);
	}

	/**
	 * Moves a friend to this group and removes the friend from his previous
	 * group. This is an asynchronous call.
	 * 
	 * @param friend
	 *            The friend you want to move
	 */
	public void addFriend(Friend friend) {
		try {
			get().addEntry(friend.get());
		} catch (NoResponseException | XMPPErrorException
				| NotConnectedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if a given Friend is part of this group.
	 * 
	 * @param friend
	 *            The friend
	 * @return True if this group contains the friend, false otherwise.
	 */
	public boolean contains(Friend friend) {
		for (Friend f : getFriends()) {
			if (StringUtils.parseName(f.getUserId()).equals(
					StringUtils.parseName(friend.getUserId()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets a list of all Friends in this FriendGroup
	 * 
	 * @return list of all Friends in this group
	 */
	public List<Friend> getFriends() {
		List<Friend> friends = new ArrayList<>();
		for (RosterEntry e : get().getEntries()) {
			friends.add(new Friend(api, con, e));
		}
		return friends;
	}

	/**
	 * Gets the name of this FriendGroup
	 * 
	 * @return The name of this FriendGroup
	 */
	public String getName() {
		return get().getName();
	}

	/**
	 * Changes the name of this group. Case sensitive.
	 * 
	 * @param name
	 *            the new name of this group
	 */
	public void setName(String name) {
		try {
			get().setName(name);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return getName();
	}

}
