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
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class ChatExample {

	public static void main(String[] args) {
		LogManager.getLogManager().reset(); // Disable logging of Smack
		new ChatExample();
	}

	public ChatExample() {
		LolChat api = new LolChat(ChatServer.EUW, false);
		if (api.login("myusername", "mypassword")) {

			try {
				Thread.sleep(1000); // Give server some time to send us all the
									// data
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Example 1: Send Chat Message to all your friends
			for (Friend f : api.getFriends()) {
				f.sendMessage("Hello " + f.getName());
			}

			// Example 2: Send Chat Message to all your friends and wait for an
			// response
			for (Friend f : api.getFriends()) {
				f.sendMessage("Hello " + f.getName(), new ChatListener() {

					@Override
					public void onMessage(Friend friend, String message) {
						System.out.println("Friend " + friend.getName() + " responded to my Hello World!");
					}
				});
			}

			// Example3: Send Chat Message to an specific friend
			Friend f = api.getFriendByName("Dyrus");
			if (f != null && f.isOnline()) {
				f.sendMessage("Hi, I'm your biggest fan!");
			}
		}
	}

}
