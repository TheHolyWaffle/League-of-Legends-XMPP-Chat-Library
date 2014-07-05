package com.github.theholywaffle.lolchatapi.listeners;

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

import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public interface FriendListener {

	/**
	 * Gets called when a friends chat is available (green circle).
	 * 
	 * @param friend
	 *            The friend who becomes available
	 */
	public void onFriendAvailable(Friend friend);

	/**
	 * Gets called when a friend is away (yellow circle).
	 * 
	 * @param friend
	 *            The friend who becomes away
	 */
	public void onFriendAway(Friend friend);

	/**
	 * Gets called when a friend is busy (yellow circle). (e.g. waiting in
	 * queue, champion select, loading screen, ingame).
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
	
	
	/**
	 * Gets called when a new friend is added and you both accepted each other request.
	 * 
	 * @param friend The new Friend.
	 */
	public void onNewFriend(Friend friend);
	
	/**
	 * Gets called when a friend is deleted or the person declines your friend request.
	 * 
	 * @param userId The XMPP-address of the user who deleted you or declined your friend request (e.g. sum12345678@pvp.net).
	 */
	public void onRemoveFriend(String userId);

}
