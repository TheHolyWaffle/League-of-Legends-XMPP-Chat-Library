import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.FriendRequestListener;

public class FriendRequestExample {
	public static void main(String[] args) {
		new FriendRequestExample();
	}

	public FriendRequestExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.MANUAL, "RIOT-API-KEY");

		// Add FriendRequestListener
		api.setFriendRequestListener(new FriendRequestListener() {

			public boolean onFriendRequest(String userId, String name) {
				System.out.println(userId + " wants to add me. Yes or no?");
				return true; // Accept user
			}
		});

		if (api.login("myusername", "mypassword")) {
			System.out.println("Loaded!");
			// ...
		}
	}
}
