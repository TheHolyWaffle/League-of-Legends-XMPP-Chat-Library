League of Legends XMPP Chat Library
===================================

A Java 7 XMPP library to chat and interact with the League of Legends chatservers. Built upon [Smack](http://www.igniterealtime.org/projects/smack/) and [JDOM](http://www.jdom.org/)

## Features

- Documented source
- Event-based chat system
- Automatic reconnecting
- Managing of Friends and FriendGroups
- Fetch extra info of your friends such as level, amount of wins, current division ...

## Getting Started

Maven is required because of the dependencies this library uses.

To start of you have to create a [LolChat](src/com/github/theholywaffle/lolchatapi/LolChat.java) object first with the correct [ChatServer](src/com/github/theholywaffle/lolchatapi/ChatServer.java) of your region. From there on you can do anything you want with it.

**Example:**

```java
LolChat api = new LolChat(ChatServer.EUW, false);
if (api.login("myusername", "mypassword")) {

	// Example 1: Send Chat Message to all your friends
	for (Friend f : api.getFriends()) {
		f.sendMessage("Hello " + f.getName());
	}

	// Example 2: Send Chat Message to all your friends and wait for an response
	for (Friend f : api.getFriends()) {
		f.sendMessage("Hello " + f.getName(), new ChatListener() {

			@Override
			public void onMessage(Friend friend, String message) {
				System.out.println("Friend " + friend.getName() + " responded back to me!");
			}
		});
	}
}
```

**More examples**

Examples such as using listeners, fetching game data and more can be found [here.](src/com/github/theholywaffle/lolchatapi/example)

## Questions or bugs?

Please let me know them [here](../../issues). I'll help you out as soon as I can.

___
*“This product is not endorsed, certified or otherwise approved in any way by Riot Games, Inc. or any of its affiliates.”*