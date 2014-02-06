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

First of all, you can always download the latest release [here](../../releases/latest) and add it to the buildpath of your project, this includes the binaries of Smack and JDOM. Or you can also just download the sourcecode and put it in your project's source folder, remember to manually add Smack and JDOM binaries as well.

To start of you have to first create a [LolChat](src/com/github/theholywaffle/lolchatapi/LolChat.java) object with the correct [ChatServer](src/com/github/theholywaffle/lolchatapi/ChatServer.java) of your region. From there you can do anything you want with it.

**Example:**

```java
LolChat api = new LolChat(ChatServer.EUW, false);
if (api.login("myusername", "mypassword")) {

	try {
		Thread.sleep(1000); // Give server some time to send us all the data
	} catch (InterruptedException e) {
		e.printStackTrace();
	}

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

Examples such as using listeners, fetching game data and more can be found here: [com.github.theholywaffle.lolchatapi.example](src/com/github/theholywaffle/lolchatapi/example)

___
*“This product is not endorsed, certified or otherwise approved in any way by Riot Games, Inc. or any of its affiliates.”*