package com.github.theholywaffle.lolchatapi.listeners;

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

import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public interface FriendListener {

	/**
	 * Gets called when a friends chat is available (green circle)
	 * 
	 * @param friend
	 *            The friend who becomes available
	 */
	public void onFriendAvailable(Friend friend);

	/**
	 * Gets called when a friend is away (yellow circle)
	 * 
	 * @param friend
	 *            The friend who becomes away
	 */
	public void onFriendAway(Friend friend);

	/**
	 * Gets called when a friend is busy (yellow circle). (e.g. waiting in
	 * queue, champion select, loading screen, ingame)
	 * 
	 * @param friend
	 *            The friend who becomes busy
	 */
	public void onFriendBusy(Friend friend);

	/**
	 * Gets called when a friend logs in.
	 * 
	 * @param friend
	 *            The friend who logs in
	 */
	public void onFriendJoin(Friend friend);

	/**
	 * Gets called when a friend logs out.
	 * 
	 * @param friend
	 *            The friend who logs out
	 */
	public void onFriendLeave(Friend friend);

	/**
	 * Gets called when the status of a friend changes. This can happen when he
	 * changes his profile icon, enters a game, joins a queue, changes division.
	 * Basically everything that can also be found when hovering over your
	 * friend in the ingame client.
	 * 
	 * @param friend
	 *            The friend who changes status
	 */
	public void onFriendStatusChange(Friend friend);

}
