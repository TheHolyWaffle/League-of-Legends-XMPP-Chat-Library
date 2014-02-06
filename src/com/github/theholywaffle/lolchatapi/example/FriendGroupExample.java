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
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;

public class FriendGroupExample {

	public static void main(String[] args) {
		LogManager.getLogManager().reset(); // Disable logging of Smack
		new FriendGroupExample();
	}

	public FriendGroupExample() {
		LolChat api = new LolChat(ChatServer.EUW, false);
		if (api.login("myusername", "mypassword")) {

			try {
				Thread.sleep(1000); // Give server some time to send us all the
									// data
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Example 1: Print out all groups and all friends in those groups
			for (FriendGroup g : api.getFriendGroups()) {
				System.out.println("Group: " + g.getName()); // Print out name
																// of group
				for (Friend f : g.getFriends()) {
					System.out.println("Friend: " + f.getName());
				}
			}

			// Example 2: Change name of FriendGroup
			api.getFriendGroupByName("duo queue partners").setName("feeders");

			// Example 3: Move friend to this group
			final Friend dyrus = api.getFriendByName("Dyrus");
			final FriendGroup proPlayers = api.getFriendGroupByName("pro players");
			proPlayers.addFriend(dyrus);
		}
	}

}
