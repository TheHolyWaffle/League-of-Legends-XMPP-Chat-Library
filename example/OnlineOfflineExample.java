import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;

public class OnlineOfflineExample {

	public static void main(String[] args) {
		new OnlineOfflineExample();
	}

	public OnlineOfflineExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.ACCEPT_ALL, "RIOT-API-KEY");
		if (api.login("myusername", "mypassword")) {

			// Example 1: appear offline
			api.setOffline();

			// Example 2: go online
			api.setOnline();

			// Example 3: put yourself away
			api.setChatMode(ChatMode.AWAY);

		}
	}

}
