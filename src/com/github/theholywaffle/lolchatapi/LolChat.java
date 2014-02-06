package com.github.theholywaffle.lolchatapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
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

	/**
	 * Represents a single connection to a League of Legends chatserver.
	 * 
	 * @param server
	 *            The chatserver of the region you want to connect to
	 * @param acceptFriendRequests
	 *            True will automatically accept all friend requests. False will
	 *            ignore all friend requests. NOTE: automatic accepting of
	 *            requests causes the name of the new friend to be null.
	 */
	public LolChat(ChatServer server, boolean acceptFriendRequests) {
		Roster.setDefaultSubscriptionMode(acceptFriendRequests ? SubscriptionMode.accept_all : SubscriptionMode.manual);
		ConnectionConfiguration config = new ConnectionConfiguration(server.host, 5223, "pvp.net");
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		config.setSocketFactory(new DummySSLSocketFactory());
		config.setCompressionEnabled(true);
		connection = new XMPPConnection(config);
		try {
			connection.connect();
		} catch (XMPPException e) {
			System.err.println("Failed to connect to " + connection.getHost());
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
	 */
	public void addChatListener(ChatListener chatListener) {
		chatListeners.add(chatListener);
	}

	/**
	 * Adds a FriendListener that listens to changes from all your friends. Such
	 * as logging in, starting games, ...
	 * 
	 * @param friendListener
	 */
	public void addFriendListener(FriendListener friendListener) {
		friendListeners.add(friendListener);
	}

	private void addListeners() {
		connection.getRoster().addRosterListener(new RosterListener() {

			private HashMap<String, Presence.Type> typeUsers = new HashMap<>();
			private HashMap<String, Presence.Mode> modeUsers = new HashMap<>();
			private HashMap<String, String> statusUsers = new HashMap<>();

			public void entriesAdded(Collection<String> e) {
			}

			public void entriesDeleted(Collection<String> e) {
			}

			public void entriesUpdated(Collection<String> e) {
			}

			public void presenceChanged(Presence p) {
				String from = p.getFrom();
				if (from != null) {
					Friend friend = getFriendById(from);
					if (friend != null) {
						for (FriendListener l : friendListeners) {
							if (typeUsers.containsKey(from)) {
								Presence.Type previous = typeUsers.get(from);
								if (p.getType() == Presence.Type.available && previous != Presence.Type.available) {
									l.onFriendJoin(friend);
								} else if (p.getType() == Presence.Type.unavailable
										&& previous != Presence.Type.unavailable) {
									l.onFriendLeave(friend);
								}
							} else if (p.getType() == Presence.Type.available) {
								l.onFriendJoin(friend);
							}

							if (modeUsers.containsKey(from)) {
								Presence.Mode previous = modeUsers.get(from);
								if (p.getMode() == Presence.Mode.chat && previous != Presence.Mode.chat) {
									l.onFriendAvailable(friend);
								} else if (p.getMode() == Presence.Mode.away && previous != Presence.Mode.away) {
									l.onFriendAway(friend);
								} else if (p.getMode() == Presence.Mode.dnd && previous != Presence.Mode.dnd) {
									l.onFriendBusy(friend);
								}
							}

							if (statusUsers.containsKey(from)) {
								String previous = statusUsers.get(from);
								if (!p.getStatus().equals(previous)) {
									l.onFriendStatusChange(friend);
								}
							}

							typeUsers.put(from, p.getType());
							modeUsers.put(from, p.getMode());
							statusUsers.put(from, p.getStatus());
						}
					}
				}
			}
		});

		connection.getChatManager().addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat c, boolean locally) {
				final Friend friend = getFriendById(c.getParticipant());
				if (friend != null) {
					c.addMessageListener(new MessageListener() {

						@Override
						public void processMessage(Chat chat, Message msg) {
							for (ChatListener c : chatListeners) {
								if (msg.getType() == Message.Type.chat) {
									c.onMessage(friend, msg.getBody());
								}
							}
						}
					});
				} else {
					System.err.println("Friend is null in chat creation");
				}

			}
		});
	}

	/**
	 * Disconnects from chatserver and releases all resources.
	 */
	public void disconnect() {
		connection.disconnect();
		stop = true;
	}

	/**
	 * @return default FriendGroup
	 */
	public FriendGroup getDefaultFriendGroup() {
		return getFriendGroupByName("**Default");
	}

	/**
	 * Gets your friend based on his XMPPAddress
	 * 
	 * @param xmppAddress
	 *            For example sum12345678@pvp.net
	 * @return The corresponding Friend or null if user is not found or he is
	 *         not a friend of you
	 */
	public Friend getFriendById(String xmppAddress) {
		return new Friend(this, connection, connection.getRoster().getEntry(StringUtils.parseBareAddress(xmppAddress)));
	}

	/**
	 * Gets a friend based on his name. The name is case insensitive.
	 * 
	 * @param name
	 *            The name of your friend, for example "Dyrus"
	 * @return The corresponding Friend object or null if user is not found or
	 *         he is not a friend of you
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
	 * Get all your FriendGroups
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
	 * Logs in to the chat server without replacing the official connection of
	 * the League of Legends client. This call is asynchronous.
	 * 
	 * @return true if login is successful, otherwise false
	 */
	public boolean login(String username, String password) {
		return login(username, password, false);
	}

	/**
	 * Logs in to the chat server. This call is asynchronous.
	 * 
	 * @param replaceLeague
	 *            True will disconnect you account from the League of Legends
	 *            client. False allows you to have another connection next to
	 *            the official connection in the League of Legends client.
	 * @return True if login was succesful
	 */
	public boolean login(String username, String password, boolean replaceLeague) {
		try {
			if (replaceLeague) {
				connection.login(username, "AIR_" + password, "xiff");
			} else {
				connection.login(username, "AIR_" + password);
			}

		} catch (XMPPException e) {
		}
		return connection.isAuthenticated();
	}

	/**
	 * Removes the ChatListener from the list and will no longer be called.
	 * 
	 * @param chatListener
	 */
	public void removeChatListener(ChatListener chatListener) {
		chatListeners.remove(chatListener);
	}

	/**
	 * Removes the FriendListener from the list and will no longer be called.
	 * 
	 * @param friendListener
	 */
	public void removeFriendListener(FriendListener friendListener) {
		friendListeners.remove(friendListener);
	}

	/**
	 * Changes your ChatMode (e.g. ingame, away, available)
	 * 
	 * @param chatMode
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
	 * Create an Status object (without constructor arguments) and call the
	 * several ".set" methods on it to customise it. Finally pass this Status
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
		connection.sendPacket(newPresence);
	}

}
