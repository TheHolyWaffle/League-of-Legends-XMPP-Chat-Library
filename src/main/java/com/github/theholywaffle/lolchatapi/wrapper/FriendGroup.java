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
package com.github.theholywaffle.lolchatapi.wrapper;

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
	 * Moves a friend to this group and removes the friend from his previous group. This is an asynchronous call.
	 * 
	 * @param friend
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
	 * @param friend The friend
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
