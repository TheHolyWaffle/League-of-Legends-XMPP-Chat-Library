package com.github.theholywaffle.lolchatapi;

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
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.spark.util.DummySSLSocketFactory;

import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendRequestListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.Friend.FriendStatus;
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
	private LeaguePacketListener leaguePacketListener;
	private FriendRequestPolicy friendRequestPolicy;

	/**
	 * Represents a single connection to a League of Legends chatserver. Default
	 * FriendRequestPolicy is {@link FriendRequestPolicy#ACCEPT_ALL}.
	 * 
	 * @see FriendRequestPolicy
	 * 
	 * @param server
	 *            The chatserver of the region you want to connect to
	 */
	public LolChat(ChatServer server) {
		this(server, FriendRequestPolicy.ACCEPT_ALL);
	}

	/**
	 * Represents a single connection to a League of Legends chatserver.
	 * 
	 * @param server
	 *            The chatserver of the region you want to connect to
	 * @param friendRequestPolicy
	 *            Determines how new Friend requests are treated.
	 * 
	 * @see LolChat#setFriendRequestPolicy(FriendRequestPolicy)
	 * @see LolChat#setFriendRequestListener(FriendRequestListener)
	 */
	public LolChat(ChatServer server, FriendRequestPolicy friendRequestPolicy) {
		this.friendRequestPolicy = friendRequestPolicy;
		Roster.setDefaultSubscriptionMode(friendRequestPolicy.mode);
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
	 * @param chatListener
	 *            The ChatListener that you want to add
	 */
	public void addChatListener(ChatListener chatListener) {
		chatListeners.add(chatListener);
	}

	/**
	 * Adds a FriendListener that listens to changes from all your friends. Such
	 * as logging in, starting games, ...
	 * 
	 * @param friendListener
	 *            The FriendListener that you want to add
	 */
	public void addFriendListener(FriendListener friendListener) {
		friendListeners.add(friendListener);
	}

	private void addListeners() {
		connection.getRoster().addRosterListener(
				leagueRosterListener = new LeagueRosterListener(this,
						connection));
		ChatManager.getInstanceFor(connection).addChatListener(
				new ChatManagerListener() {

					@Override
					public void chatCreated(Chat c, boolean locally) {
						final Friend friend = getFriendById(c.getParticipant());
						if (friend != null) {
							c.addMessageListener(new MessageListener() {

								@Override
								public void processMessage(Chat chat,
										Message msg) {
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
	 * Sends an friend request to an other user.
	 * 
	 * @param userId
	 *            The userId of the user you want to add.
	 */
	public void addUser(String userId) {
		addUser(userId, null, getDefaultFriendGroup());
	}

	/**
	 * Sends an friend request to an other user.
	 * 
	 * @param userId
	 *            The userId of the user you want to add.
	 * @param friendGroup
	 *            The FriendGroup you want to put this user in.
	 */
	public void addUser(String userId, FriendGroup friendGroup) {
		addUser(userId, null, friendGroup);
	}

	/**
	 * Sends an friend request to an other user.
	 * 
	 * @param userId
	 *            The userId of the user you want to add.
	 * @param name
	 *            The name you want to assign to this user.
	 */
	public void addUser(String userId, String name) {
		addUser(userId, name, getDefaultFriendGroup());
	}

	/**
	 * Sends an friend request to an other user.
	 * 
	 * @param userId
	 *            The userId of the user you want to add.
	 * @param name
	 *            The name you want to assign to this user.
	 * @param friendGroup
	 *            The FriendGroup you want to put this user in.
	 */
	public void addUser(String userId, String name, FriendGroup friendGroup) {
		try {
			connection.getRoster().createEntry(
					StringUtils.parseBareAddress(userId), name,
					new String[] { friendGroup.getName() });
		} catch (NotLoggedInException | NoResponseException
				| XMPPErrorException | NotConnectedException e) {
			e.printStackTrace();
		}
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
	 * Gets the default FriendGroup.
	 * 
	 * @return default FriendGroup
	 */
	public FriendGroup getDefaultFriendGroup() {
		FriendGroup group = getFriendGroupByName("**Default");
		if (group != null) {
			return group;
		}
		return new FriendGroup(this, connection, connection.getRoster()
				.createGroup("**Default"));
	}

	/**
	 * Gets a friend based on a given filter.
	 * 
	 * @param filter
	 *            The filter defines conditions that your Friend must meet.
	 * @return The first Friend that meets the conditions or null if not found.
	 */
	public Friend getFriend(Filter<Friend> filter) {
		for (RosterEntry e : connection.getRoster().getEntries()) {
			Friend f = new Friend(this, connection, e);
			if (filter.accept(f)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * Gets a friend based on his XMPPAddress.
	 * 
	 * @param xmppAddress
	 *            For example sum12345678@pvp.net
	 * @return The corresponding Friend or null if user is not found or he is
	 *         not a friend of you
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
	 * @return The corresponding Friend object or null if user is not found or
	 *         he is not a friend of you
	 */
	public Friend getFriendByName(final String name) {
		return getFriend(new Filter<Friend>() {

			public boolean accept(Friend friend) {
				return friend.getName() != null
						&& friend.getName().equalsIgnoreCase(name);
			}
		});
	}

	/**
	 * Get a FriendGroup by name, for example "Duo Partners". The name is case
	 * sensitive!
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
	 * Get a list of all your FriendGroups.
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
	 * Gets the current FriendRequestPolicy.
	 * 
	 * @return The current FriendRequestPolicy
	 */
	public FriendRequestPolicy getFriendRequestPolicy() {
		return friendRequestPolicy;
	}

	/**
	 * Get all your friends, both online and offline.
	 * 
	 * @return A List of all your Friends
	 */
	public List<Friend> getFriends() {
		return getFriends(new Filter<Friend>() {

			public boolean accept(Friend e) {
				return true;
			}
		});
	}

	/**
	 * Gets a list of your friends based on a given filter.
	 * 
	 * @param filter
	 *            The filter defines conditions that your Friends must meet.
	 * @return A List of your Friends that meet the condition of your Filter
	 */
	public List<Friend> getFriends(Filter<Friend> filter) {
		ArrayList<Friend> friends = new ArrayList<>();
		for (RosterEntry e : connection.getRoster().getEntries()) {
			Friend f = new Friend(this, connection, e);
			if (filter.accept(f)) {
				friends.add(f);
			}
		}
		return friends;
	}

	/**
	 * Get all your friends who are offline.
	 * 
	 * @return A list of all your offline Friends
	 */
	public List<Friend> getOfflineFriends() {
		return getFriends(new Filter<Friend>() {

			public boolean accept(Friend friend) {
				return !friend.isOnline();
			}
		});
	}

	/**
	 * Get all your friends who are online.
	 * 
	 * @return A list of all your online Friends
	 */
	public List<Friend> getOnlineFriends() {
		return getFriends(new Filter<Friend>() {

			public boolean accept(Friend friend) {
				return friend.isOnline();
			}
		});
	}

	/**
	 * Gets a list of user that you've sent friend requests but haven't answered
	 * yet.
	 * 
	 * @return A list of Friends.
	 */
	public List<Friend> getPendingFriendRequests() {
		return getFriends(new Filter<Friend>() {

			public boolean accept(Friend friend) {
				return friend.getFriendStatus() == FriendStatus.ADD_REQUEST_PENDING;
			}
		});
	}

	/**
	 * Logs in to the chat server without replacing the official connection of
	 * the League of Legends client. This call is asynchronous.
	 * BEWARE: add/set all listeners before logging in, otherwise some offline
	 * messages can get lost.
	 * 
	 * @param username
	 *            Username of your account
	 * @param password
	 *            Password of your account
	 * @return true if login is successful, false otherwise
	 */
	public boolean login(String username, String password) {
		return login(username, password, false);
	}

	/**
	 * Logs in to the chat server. This call is asynchronous.
	 * 
	 * BEWARE: add/set all listeners before logging in, otherwise some offline
	 * messages can get lost.
	 * 
	 * @param username
	 *            Username of your account
	 * @param password
	 *            Password of your account
	 * @param replaceLeague
	 *            True will disconnect you account from the League of Legends
	 *            client. False allows you to have another connection open next
	 *            to the official connection in the League of Legends client.
	 * @return true if login was succesful, false otherwise
	 */
	public boolean login(String username, String password, boolean replaceLeague) {
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
	 * @param chatListener
	 *            The ChatListener that you want to remove
	 */
	public void removeChatListener(ChatListener chatListener) {
		chatListeners.remove(chatListener);
	}

	/**
	 * Removes the FriendListener from the list and will no longer be called.
	 * 
	 * @param friendListener
	 *            The FriendListener that you want to remove
	 */
	public void removeFriendListener(FriendListener friendListener) {
		friendListeners.remove(friendListener);
	}

	/**
	 * Changes your ChatMode (e.g. busy, away, available).
	 * 
	 * @param chatMode
	 *            The new ChatMode
	 * @see ChatMode
	 */
	public void setChatMode(ChatMode chatMode) {
		this.mode = chatMode.mode;
		updateStatus();
	}

	/**
	 * Changes the current FriendRequestListener. It is recommended to do this
	 * before logging in.
	 * 
	 * @param friendRequestListener
	 *            The new FriendRequestListener
	 */
	public void setFriendRequestListener(
			FriendRequestListener friendRequestListener) {
		if (leaguePacketListener == null) {
			leaguePacketListener = new LeaguePacketListener(this, connection);
			leaguePacketListener
					.setFriendRequestListener(friendRequestListener);
			connection.addPacketListener(leaguePacketListener,
					new PacketFilter() {
						public boolean accept(Packet packet) {
							if (packet instanceof Presence) {
								Presence presence = (Presence) packet;
								if (presence.getType().equals(
										Presence.Type.subscribed)
										|| presence.getType().equals(
												Presence.Type.subscribe)
										|| presence.getType().equals(
												Presence.Type.unsubscribed)
										|| presence.getType().equals(
												Presence.Type.unsubscribe)) {
									return true;
								}
							}
							return false;
						}
					});
		} else {
			leaguePacketListener
					.setFriendRequestListener(friendRequestListener);
		}
	}

	/**
	 * Changes the the current FriendRequestPolicy.
	 * 
	 * @param friendRequestPolicy
	 *            The new FriendRequestPolicy
	 * @see FriendRequestPolicy
	 */
	public void setFriendRequestPolicy(FriendRequestPolicy friendRequestPolicy) {
		connection.getRoster().setSubscriptionMode(friendRequestPolicy.mode);
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
	 * Create an Status object (without constructor arguments) and call the
	 * several ".set" methods to customise it. After that pass this Status
	 * object back to this method.
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
