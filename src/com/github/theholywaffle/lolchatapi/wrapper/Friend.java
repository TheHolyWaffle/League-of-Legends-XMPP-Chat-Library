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

import java.io.IOException;

import org.jdom2.JDOMException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;

public class Friend extends Wrapper<RosterEntry> {

	private Friend instance = null;
	private Chat chat = null;
	private ChatListener listener = null;

	public Friend(LolChat api, XMPPConnection connection, RosterEntry entry) {
		super(api, connection, entry);
		this.instance = this;
	}

	/**
	 * Deletes this friend.
	 * 
	 * @return true if succesful, otherwise false
	 */
	public boolean delete() {
		try {
			con.getRoster().removeEntry(get());
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Chat getChat() {
		if (chat == null) {
			chat = con.getChatManager().createChat(getUserId(),
					new MessageListener() {

						@Override
						public void processMessage(Chat c, Message m) {
							if (chat != null && listener != null) {
								listener.onMessage(instance, m.getBody());
							}

						}
					});
		}
		return chat;
	}

	/**
	 * Returns the current ChatMode of this friend (e.g. away, busy,
	 * available)
	 * 
	 * @see ChatMode
	 * @return ChatMode of this friend
	 */
	public ChatMode getChatMode() {
		Presence.Mode mode = con.getRoster().getPresence(getUserId()).getMode();
		for (ChatMode c : ChatMode.values()) {
			if (c.mode == mode) {
				return c;
			}
		}
		return null;
	}

	/**
	 * @return the FriendGroup that currently contains this Friend
	 */
	public FriendGroup getGroup() {
		return new FriendGroup(api, con, get().getGroups().iterator().next());
	}

	/**
	 * @return name of your Friend (e.g. Dyrus)
	 */
	public String getName() {
		return get().getName();
	}

	/**
	 * Returns Status object that contains all data when hovering over a friend
	 * inside League of Legends client (e.g. amount of normal wins, current
	 * division and league, queue name, gamestatus,...)
	 * 
	 * @return Status
	 */
	public LolStatus getStatus() {
		String status = con.getRoster().getPresence(getUserId()).getStatus();
		if (status != null && !status.isEmpty()) {
			try {
				return new LolStatus(status);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
		return new LolStatus();
	}

	/**
	 * @return the XMPPAddress of your Friend (e.g. sum123456@pvp.net)
	 */
	public String getUserId() {
		return get().getUser();
	}

	/**
	 * Returns true if this friend is online.
	 * 
	 * @return true if online, false if offline
	 */
	public boolean isOnline() {
		return con.getRoster().getPresence(getUserId()).getType() == Type.available;
	}

	/**
	 * Sends a message to this friend
	 * 
	 */
	public void sendMessage(String message) {
		try {
			getChat().sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to this friend and sets the ChatListener. Only 1
	 * ChatListener can be active for each Friend. Any previously set
	 * ChatListener will be replaced by this one.
	 * 
	 * This ChatListener gets called when this Friend only sends you a message.
	 * 
	 * @param message
	 * @param listener
	 *            Your new active ChatListener
	 */
	public void sendMessage(String message, ChatListener listener) {
		this.listener = listener;
		try {
			getChat().sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the ChatListener for this friend only. Only 1 ChatListener can be
	 * active for each Friend. Any previously set ChatListener for this friend
	 * will be replaced by this one.
	 * 
	 * This ChatListener gets called when this Friend only sends you a message.
	 * 
	 * @param listener
	 */
	public void setChatListener(ChatListener listener) {
		this.listener = listener;
		getChat();
	}
}
