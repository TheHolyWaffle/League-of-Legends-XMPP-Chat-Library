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
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.LolStatus.Queue;
import com.github.theholywaffle.lolchatapi.LolStatus.Tier;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class LolStatusExample {

	public static void main(String[] args) {
		LogManager.getLogManager().reset(); // Disable logging of Smack
		new LolStatusExample();
	}

	public LolStatusExample() {
		LolChat api = new LolChat(ChatServer.EUW, false);
		if (api.login("myusername", "mypassword")) {

			// Example 1: Print out all groups and all friends in those groups
			Friend dyrus = api.getFriendByName("Dyrus");
			LolStatus status = dyrus.getStatus();
			System.out.println("Current divison: " + status.getRankedLeagueDivision());
			// Status be "in queue", "championselect", "ingame", "spectating",..
			System.out.println("Current GameStatus: " + status.getGameStatus());
			System.out.println("Spectating: " + status.getSpectatedGameId());
			System.out.println("Normal Leaves: " + status.getNormalLeaves());
			// ...
			
			
			//Example 2: Set a custom status
			LolStatus newStatus = new LolStatus();
			newStatus.setLevel(1337);
			newStatus.setRankedLeagueQueue(Queue.RANKED_SOLO_5x5);
			newStatus.setRankedLeagueTier(Tier.CHALLENGER);
			newStatus.setRankedLeagueName("Fiora's asscheecks");
			api.setStatus(newStatus);
			
			//Example 3: Copy status from friend
			LolStatus copyStatus = api.getFriendByName("Dyrus").getStatus();
			copyStatus.setLevel(1337); //Modify it if you like
			api.setStatus(copyStatus); //Put it as your own status
			
		}
	}

}
