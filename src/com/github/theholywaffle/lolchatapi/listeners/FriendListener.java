package com.github.theholywaffle.lolchatapi.listeners;

import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public interface FriendListener {

	/**
	 * Gets called when a friend logs out.
	 */
	public void onFriendLeave(Friend friend);

	/**
	 * Gets called when a friend logs in.
	 */
	public void onFriendJoin(Friend friend);

	/**
	 * Gets called when a friends chat is available (green circle)
	 */
	public void onFriendAvailable(Friend friend);

	/**
	 * Gets called when a friend is away (yellow circle)
	 */
	public void onFriendAway(Friend friend);

	/**
	 * Gets called when a friend is busy (yellow circle). (e.g. waiting in
	 * queue, champion select, loading screen, ingame)
	 */
	public void onFriendBusy(Friend friend);

	/**
	 * Gets called when the status of a friend changes. This can happen when he
	 * changes his profile icon, enters a game, joins a queue, changes division.
	 * Basically everything that can also be found when hovering over your
	 * friend in the ingame client.
	 */
	public void onFriendStatusChange(Friend friend);

}
