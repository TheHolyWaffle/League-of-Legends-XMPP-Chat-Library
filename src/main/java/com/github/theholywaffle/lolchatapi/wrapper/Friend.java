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

import java.io.IOException;

import org.jdom2.JDOMException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;

import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;

/**
 * Represents a friend of your friendlist.
 *
 */
public class Friend extends Wrapper<RosterEntry> {

	public enum FriendStatus {

		/**
		 * You are both friends.
		 */
		MUTUAL_FRIENDS(null),

		/**
		 * A request to be added is pending.
		 */
		ADD_REQUEST_PENDING(ItemStatus.subscribe),
		/**
		 * A request to be removed is pending.
		 */
		REMOVE_REQUEST_PENDING(ItemStatus.unsubscribe);

		public ItemStatus status;

		FriendStatus(ItemStatus status) {
			this.status = status;
		}

	}

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
		} catch (XMPPException | NotLoggedInException | NoResponseException
				| NotConnectedException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Chat getChat() {
		if (chat == null) {
			chat = ChatManager.getInstanceFor(con).createChat(getUserId(),
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
	 * Returns the current ChatMode of this friend (e.g. away, busy, available).
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
	 * Gets the relation between you and this friend.
	 * 
	 * @return The FriendStatus
	 * @see FriendStatus
	 */
	public FriendStatus getFriendStatus() {
		for (FriendStatus status : FriendStatus.values()) {
			if (status.status == get().getStatus()) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gets the FriendGroup that contains this friend.
	 * 
	 * @return the FriendGroup that currently contains this Friend
	 */
	public FriendGroup getGroup() {
		return new FriendGroup(api, con, get().getGroups().iterator().next());
	}

	/**
	 * Gets the name of this friend.
	 * 
	 * @return name of this Friend or an empty String if no name is assigned.
	 */
	public String getName() {
		String name = get().getName();
		if (name != null) {
			return name;
		}
		return "";
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
	 * Gets the XMPPAddress of your Friend (e.g. sum123456@pvp.net)
	 * 
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
	 * @param message
	 *            The message you want to send
	 */
	public void sendMessage(String message) {
		try {
			getChat().sendMessage(message);
		} catch (XMPPException | NotConnectedException e) {
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
	 *            The message you want to send
	 * @param listener
	 *            Your new active ChatListener
	 */
	public void sendMessage(String message, ChatListener listener) {
		this.listener = listener;
		try {
			getChat().sendMessage(message);
		} catch (XMPPException | NotConnectedException e) {
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
	 *            The ChatListener you want to assign to this Friend
	 */
	public void setChatListener(ChatListener listener) {
		this.listener = listener;
		getChat();
	}

	/**
	 * Changes the name of this Friend.
	 * 
	 * @param name
	 *            The new name for this Friend
	 */
	public void setName(String name) {
		try {
			get().setName(name);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
}
