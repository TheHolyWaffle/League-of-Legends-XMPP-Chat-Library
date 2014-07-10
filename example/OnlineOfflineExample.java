import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;

public class OnlineOfflineExample {

	public static void main(String[] args) {
		new OnlineOfflineExample();
	}

	public OnlineOfflineExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",RateLimit.DEFAULT));
		if (api.login("myusername", "mypassword")) {

			// Example 1: appear offline
			api.setOffline();

			// Example 2: go online - you will not receive changes from your friendlist anymore
			api.setOnline();

			// Example 3: put yourself away
			api.setChatMode(ChatMode.AWAY);

		}
	}

}
