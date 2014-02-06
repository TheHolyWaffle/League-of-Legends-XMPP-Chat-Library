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
package com.github.theholywaffle.lolchatapi.example;

import java.util.logging.LogManager;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class ListenerExample {

	public static void main(String[] args) {
		LogManager.getLogManager().reset(); // Disable logging of Smack
		new ListenerExample();
	}

	public ListenerExample() {
		LolChat api = new LolChat(ChatServer.EUW, false);
		if (api.login("myusername", "mypassword")) {
			
			//Example 1: Adding FriendListeners - listens to changes in your friendlist
			api.addFriendListener(new FriendListener() {
				int i = 0;

				public void onFriendLeave(Friend friend) {
					System.out.println("[" + i + "]" + friend.getName() + " left!");
					i++;
				}

				public void onFriendJoin(Friend friend) {
					System.out.println("[" + i + "]" + friend.getName() + " joined!");
					i++;
				}

				public void onFriendAvailable(Friend friend) {
					System.out.println("[" + i + "]" + friend.getName() + " is available!");
					i++;
				}

				public void onFriendAway(Friend friend) {
					System.out.println("[" + i + "]" + friend.getName() + " is away!");
					i++;
				}

				public void onFriendBusy(Friend friend) {
					System.out.println("[" + i + "]" + friend.getName() + " is busy!");
					i++;
				}

				public void onFriendStatusChange(Friend friend) {
					System.out.println("[" + i + "]" + friend.getName() + " status changed! New GameStatus: "
							+ friend.getStatus().getGameStatus());
					i++;
				}
			});

			//Example 2: Adding ChatListener - listens to chat messages from any of your friends.
			api.addChatListener(new ChatListener() {

				public void onMessage(Friend friend, String message) {
					System.out.println("[All]>" + friend.getName() + ": " + message);

				}
			});
			
			//Example 3: Adding ChatListener to 1 Friend only - this only listens to messages from this friend only
			api.getFriendByName("Dyrus").setChatListener(new ChatListener() {
				
				public void onMessage(Friend friend, String message) {
					//Dyrus sent us a message
					System.out.println("Dyrus: "+message);					
				}
			});

		}
	}

}
