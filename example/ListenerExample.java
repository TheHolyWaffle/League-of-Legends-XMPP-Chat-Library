import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class ListenerExample {

	public static void main(String[] args) {
		new ListenerExample();
	}

	public ListenerExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",
						RateLimit.DEFAULT));
		/** First add or set all listeners BEFORE logging in! */

		// Example 1: Adding FriendListeners - listens to changes in your
		// friendlist
		api.addFriendListener(new FriendListener() {

			public void onFriendAvailable(Friend friend) {
				System.out.println(friend.getName() + " is available!");
			}

			public void onFriendAway(Friend friend) {
				System.out.println(friend.getName() + " is away!");
			}

			public void onFriendBusy(Friend friend) {
				System.out.println(friend.getName() + " is busy!");
			}

			public void onFriendJoin(Friend friend) {
				System.out.println(friend.getName() + " joined!");
			}

			public void onFriendLeave(Friend friend) {
				System.out.println(friend.getName() + " left!");
			}

			public void onFriendStatusChange(Friend friend) {
				System.out.println(friend.getName()
						+ " status changed! New GameStatus: "
						+ friend.getStatus().getGameStatus());
			}

			public void onNewFriend(Friend friend) {
				System.out.println("New friend: " + friend.getUserId());
			}

			public void onRemoveFriend(String userId, String name) {
				System.out.println("Friend removed: " + userId + " " + name);
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
			System.out.println("Loaded!");
			// ...
		}
	}

}
