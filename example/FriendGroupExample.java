import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;

public class FriendGroupExample {

	public static void main(String[] args) {
		new FriendGroupExample();
	}

	public FriendGroupExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",
						RateLimit.DEFAULT));
		if (api.login("myusername", "mypassword")) {

			// Example 1: Print out all groups and all friends in those groups
			for (final FriendGroup g : api.getFriendGroups()) {
				System.out.println("Group: " + g.getName()); // Print out name
																// of group
				for (final Friend f : g.getFriends()) {
					System.out.println("Friend: " + f.getName());
				}
			}

			// Example 2: Change name of FriendGroup
			api.getFriendGroupByName("duo queue partners").setName("feeders");

			// Example 3: Move friend to this group
			final Friend dyrus = api.getFriendByName("Dyrus");
			final FriendGroup proPlayers = api
					.getFriendGroupByName("pro players");
			proPlayers.addFriend(dyrus);
		}
	}

}
