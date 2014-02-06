package com.github.theholywaffle.lolchatapi.example;

import java.util.logging.LogManager;

import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.LolChat;

public class OnlineOfflineExample {

	public static void main(String[] args) {
		LogManager.getLogManager().reset(); // Disable logging of Smack
		new OnlineOfflineExample();
	}

	public OnlineOfflineExample() {
		LolChat api = new LolChat(ChatServer.EUW, false);
		if (api.login("myusername", "mypassword")) {
			try {
				Thread.sleep(1000); // Give server some time to send us all the
									// data
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Example 1: appear offline
			api.setOffline();
			
			//Example 2: go online
			api.setOnline();
			
			//Example 3: put yourself away
			api.setChatMode(ChatMode.AWAY);

		}
	}

}
