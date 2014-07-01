package com.github.theholywaffle.lolchatapi.example;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class ListenerExample {

	public static void main(String[] args) {
		new ListenerExample();
	}

	public ListenerExample() {
		LolChat api = new LolChat(ChatServer.EUW);
		/** First add or set all listeners BEFORE logging in!*/
		
		// Example 1: Adding FriendListeners - listens to changes in your
		// friendlist
		api.addFriendListener(new FriendListener() {
			int i = 0;

			public void onFriendAvailable(Friend friend) {
				System.out.println("[" + i + "]" + friend.getName()
						+ " is available!");
				i++;
			}

			public void onFriendAway(Friend friend) {
				System.out.println("[" + i + "]" + friend.getName()
						+ " is away!");
				i++;
			}

			public void onFriendBusy(Friend friend) {
				System.out.println("[" + i + "]" + friend.getName()
						+ " is busy!");
				i++;
			}

			public void onFriendJoin(Friend friend) {
				System.out.println("[" + i + "]" + friend.getName()
						+ " joined!");
				i++;
			}

			public void onFriendLeave(Friend friend) {
				System.out.println("[" + i + "]" + friend.getName() + " left!");
				i++;
			}

			public void onFriendStatusChange(Friend friend) {
				System.out.println("[" + i + "]" + friend.getName()
						+ " status changed! New GameStatus: "
						+ friend.getStatus().getGameStatus());
				i++;
			}
		});

		// Example 2: Adding ChatListener - listens to chat messages from
		// any of your friends.
		api.addChatListener(new ChatListener() {

			public void onMessage(Friend friend, String message) {
				System.out.println("[All]>" + friend.getName() + ": " + message);

			}
		});

		// Example 3: Adding ChatListener to 1 Friend only - this only
		// listens to messages from this friend only
		api.getFriendByName("Dyrus").setChatListener(new ChatListener() {

			public void onMessage(Friend friend, String message) {
				// Dyrus sent us a message
				System.out.println("Dyrus: " + message);
			}
		});
		
		if (api.login("myusername", "mypassword")) {
			//...
		}
	}

}
