package com.github.theholywaffle.lolchatapi.wrapper;

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
 * Represents a group that can contain multiple friends.
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
	 * Gets a list of all Friends in this FriendGroup.
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
