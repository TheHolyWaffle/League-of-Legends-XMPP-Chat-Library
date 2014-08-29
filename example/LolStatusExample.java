import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.LolStatus.Queue;
import com.github.theholywaffle.lolchatapi.LolStatus.Tier;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

/**
 * This example shows how you can modify your own LolStatus.
 *
 */
public class LolStatusExample {

	public static void main(String[] args) {
		new LolStatusExample();
	}

	public LolStatusExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",
						RateLimit.DEFAULT));
		if (api.login("myusername", "mypassword")) {

			// Example 1: Print out all groups and all friends in those groups
			final Friend dyrus = api.getFriendByName("Dyrus");
			final LolStatus status = dyrus.getStatus();
			System.out.println("Current divison: "
					+ status.getRankedLeagueDivision());
			// Status be "in queue", "championselect", "ingame", "spectating",..
			System.out.println("Current GameStatus: " + status.getGameStatus());
			System.out.println("Spectating: " + status.getSpectatedGameId());
			System.out.println("Normal Leaves: " + status.getNormalLeaves());
			// ...

			// Example 2: Set a custom status
			final LolStatus newStatus = new LolStatus();
			newStatus.setLevel(1337);
			newStatus.setRankedLeagueQueue(Queue.RANKED_SOLO_5x5);
			newStatus.setRankedLeagueTier(Tier.CHALLENGER);
			newStatus.setRankedLeagueName("Fiora's asscheecks");
			api.setStatus(newStatus);

			// Example 3: Copy status from friend
			final LolStatus copyStatus = api.getFriendByName("Dyrus")
					.getStatus();
			copyStatus.setLevel(1337); // Modify it if you like
			api.setStatus(copyStatus); // Put it as your own status

		}
	}

}
