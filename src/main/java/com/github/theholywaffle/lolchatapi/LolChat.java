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
package com.github.theholywaffle.lolchatapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.spark.util.DummySSLSocketFactory;

import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;

public class LolChat {

	private final XMPPConnection connection;
	private final ArrayList<ChatListener> chatListeners = new ArrayList<>();
	private final ArrayList<FriendListener> friendListeners = new ArrayList<>();
	private boolean stop = false;

	private String status = "";
	private Presence.Type type = Presence.Type.available;
	private Presence.Mode mode = Presence.Mode.chat;
	private LeagueRosterListener leagueRosterListener;

	/**
	 * Represents a single connection to a League of Legends chatserver.
	 * 
	 * @param server
	 *            The chatserver of the region you want to connect to
	 * @param acceptFriendRequests
	 *            True will automatically accept all friend requests. False will ignore all friend requests. NOTE: automatic accepting of requests
	 *            causes the name of the new friend to be null.
	 */
	public LolChat(ChatServer server, boolean acceptFriendRequests) {
		Roster.setDefaultSubscriptionMode(acceptFriendRequests ? SubscriptionMode.accept_all
				: SubscriptionMode.manual);
		ConnectionConfiguration config = new ConnectionConfiguration(
				server.host, 5223, "pvp.net");
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		config.setSocketFactory(new DummySSLSocketFactory());
		config.setCompressionEnabled(true);
		connection = new XMPPTCPConnection(config);
		try {
			connection.connect();
		} catch (XMPPException | SmackException | IOException e) {
			System.err.println("Failed to connect to " + server.host);
		}
		addListeners();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!stop) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException ignored) {
					}
				}
			}
		}).start();
	}

	/**
	 * Adds a ChatListener that listens to messages from all your friends.
	 * 
	 * @param chatListener The ChatListener that you want to add
	 */
	public void addChatListener(ChatListener chatListener) {
		chatListeners.add(chatListener);
	}

	/**
	 * Adds a FriendListener that listens to changes from all your friends. Such as logging in, starting games, ...
	 * 
	 * @param friendListener The FriendListener that you want to add
	 */
	public void addFriendListener(FriendListener friendListener) {
		friendListeners.add(friendListener);
	}

	private void addListeners() {
		connection.getRoster().addRosterListener(
				leagueRosterListener = new LeagueRosterListener(this));
		ChatManager.getInstanceFor(connection).addChatListener(
				new ChatManagerListener() {

					@Override
					public void chatCreated(Chat c, boolean locally) {
						final Friend friend = getFriendById(c.getParticipant());
						if (friend != null) {
							c.addMessageListener(new MessageListener() {

								@Override
								public void processMessage(
										Chat chat, Message msg) {
									for (ChatListener c : chatListeners) {
										if (msg.getType() == Message.Type.chat) {
											c.onMessage(friend, msg.getBody());
										}
									}
								}
							});
						} else {
							System.err
									.println("Friend is null in chat creation");
						}

					}
				});
	}

	/**
	 * Disconnects from chatserver and releases all resources.
	 */
	public void disconnect() {
		connection.getRoster().removeRosterListener(leagueRosterListener);
		try {
			connection.disconnect();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		stop = true;
	}

	/**
	 * Gets all ChatListeners that have been added.
	 * 
	 * @return List of ChatListeners
	 */
	public List<ChatListener> getChatListeners() {
		return chatListeners;
	}

	/**
	 * Gets the default FriendGroup
	 * 
	 * @return default FriendGroup
	 */
	public FriendGroup getDefaultFriendGroup() {
		return getFriendGroupByName("**Default");
	}

	/**
	 * Gets a friend based on his XMPPAddress
	 * 
	 * @param xmppAddress
	 *            For example sum12345678@pvp.net
	 * @return The corresponding Friend or null if user is not found or he is not a friend of you
	 */
	public Friend getFriendById(String xmppAddress) {
		return new Friend(this, connection, connection.getRoster().getEntry(
				StringUtils.parseBareAddress(xmppAddress)));
	}

	/**
	 * Gets a friend based on his name. The name is case insensitive.
	 * 
	 * @param name
	 *            The name of your friend, for example "Dyrus"
	 * @return The corresponding Friend object or null if user is not found or he is not a friend of you
	 */
	public Friend getFriendByName(String name) {
		for (Friend f : getFriends()) {
			if (f.getName().equalsIgnoreCase(name)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * Get a FriendGroup by name, for example "Duo Partners". The name is case sensitive!
	 * 
	 * @param name
	 *            The name of your group
	 * @return The corresponding FriendGroup or null if not found
	 */
	public FriendGroup getFriendGroupByName(String name) {
		RosterGroup g = connection.getRoster().getGroup(name);
		if (g != null) {
			return new FriendGroup(this, connection, g);
		}
		return null;
	}

	/**
	 * Get a list of all your FriendGroups
	 * 
	 * @return A List of all your FriendGroups
	 */
	public List<FriendGroup> getFriendGroups() {
		ArrayList<FriendGroup> groups = new ArrayList<>();
		for (RosterGroup g : connection.getRoster().getGroups()) {
			groups.add(new FriendGroup(this, connection, g));
		}
		return groups;
	}

	/**
	 * Gets all FriendListeners that have been added.
	 * 
	 * @return List of FriendListeners
	 */
	public List<FriendListener> getFriendListeners() {
		return friendListeners;
	}

	/**
	 * Get all your friends, both online and offline
	 * 
	 * @return A List of all your Friends
	 */
	public List<Friend> getFriends() {
		ArrayList<Friend> friends = new ArrayList<>();
		for (RosterEntry e : connection.getRoster().getEntries()) {
			friends.add(new Friend(this, connection, e));
		}
		return friends;
	}

	/**
	 * Get all your friends who are offline.
	 * 
	 * @return A list of all your offline Friends
	 */
	public List<Friend> getOfflineFriends() {
		List<Friend> f = getFriends();
		Iterator<Friend> i = f.iterator();
		while (i.hasNext()) {
			Friend friend = i.next();
			if (friend.isOnline()) {
				i.remove();
			}
		}
		return f;
	}

	/**
	 * Get all your friends who are online.
	 * 
	 * @return A list of all your online Friends
	 */
	public List<Friend> getOnlineFriends() {
		List<Friend> f = getFriends();
		Iterator<Friend> i = f.iterator();
		while (i.hasNext()) {
			Friend friend = i.next();
			if (!friend.isOnline()) {
				i.remove();
			}
		}
		return f;
	}

	/**
	 * Logs in to the chat server without replacing the official connection of the League of Legends client. This call is asynchronous.
	 * @param username Username of your account
	 * @param password Password of your account
	 * @return true if login is successful, false otherwise
	 */
	public boolean login(String username, String password) {
		return login(username, password, false);
	}

	/**
	 * Logs in to the chat server. This call is asynchronous.
	 * 
	 * @param username Username of your account
	 * @param password Password of your account 
	 * @param replaceLeague
	 *            True will disconnect you account from the League of Legends client. False allows you to have another connection open next to the official
	 *            connection in the League of Legends client.
	 * @return true if login was succesful, false otherwise
	 */
	public
			boolean login(
					String username, String password, boolean replaceLeague) {
		try {
			if (replaceLeague) {
				connection.login(username, "AIR_" + password, "xiff");
			} else {
				connection.login(username, "AIR_" + password);
			}

		} catch (XMPPException | SmackException | IOException e) {
		}
		if (connection.isAuthenticated()) {
			long startTime = System.currentTimeMillis();
			while (!leagueRosterListener.isLoaded()
					&& (System.currentTimeMillis() - startTime) < 1000) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes the ChatListener from the list and will no longer be called.
	 * 
	 * @param chatListener The ChatListener that you want to remove
	 */
	public void removeChatListener(ChatListener chatListener) {
		chatListeners.remove(chatListener);
	}

	/**
	 * Removes the FriendListener from the list and will no longer be called.
	 * 
	 * @param friendListener The FriendListener that you want to remove
	 */
	public void removeFriendListener(FriendListener friendListener) {
		friendListeners.remove(friendListener);
	}

	/**
	 * Changes your ChatMode (e.g. ingame, away, available)
	 * 
	 * @param chatMode The new ChatMode
	 * @see ChatMode
	 */
	public void setChatMode(ChatMode chatMode) {
		this.mode = chatMode.mode;
		updateStatus();
	}

	/**
	 * Change your appearance to offline.
	 * 
	 */
	public void setOffline() {
		this.type = Presence.Type.unavailable;
		updateStatus();
	}

	/**
	 * Change your appearance to online.
	 * 
	 */
	public void setOnline() {
		this.type = Presence.Type.available;
		updateStatus();
	}

	/**
	 * Update your own status with current level, ranked wins...
	 * 
	 * Create an Status object (without constructor arguments) and call the several ".set" methods on it to customise it. Finally pass this Status
	 * object back to this method
	 * 
	 * @param status
	 *            Your custom Status object
	 * @see LolStatus
	 */
	public void setStatus(LolStatus status) {
		this.status = status.toString();
		updateStatus();
	}

	private void updateStatus() {
		Presence newPresence = new Presence(type, status, 1, mode);
		try {
			connection.sendPacket(newPresence);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

}
