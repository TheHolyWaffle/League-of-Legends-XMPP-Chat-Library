package com.github.theholywaffle.lolchatapi.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.github.theholywaffle.lolchatapi.LolChat;

public class FriendGroup extends Wrapper<RosterGroup> {

	public FriendGroup(LolChat api, XMPPConnection con, RosterGroup group) {
		super(api, con, group);
	}

	/**
	 * Moves a friend to this group and removes the friend from his previous
	 * group. This is an asynchronous call.
	 * 
	 * @param friend
	 */
	public void addFriend(Friend friend) {
		try {
			get().addEntry(friend.get());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return list of all Friend's in this group
	 */
	public List<Friend> getFriends() {
		List<Friend> friends = new ArrayList<>();
		for (RosterEntry e : get().getEntries()) {
			friends.add(new Friend(api, con, e));
		}
		return friends;
	}

	/**
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
		get().setName(name);
	}

}
