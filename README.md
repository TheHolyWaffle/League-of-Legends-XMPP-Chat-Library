League of Legends XMPP Chat Library
===================================

A Java 7 XMPP library to chat and interact with the League of Legends chatservers. Built upon [Smack](http://www.igniterealtime.org/projects/smack/) and [JDOM](http://www.jdom.org/).

## Features

- Documented source
- Event-based chat system
- Automatic reconnecting
- Managing of Friends and FriendGroups
- Fetch metadata of friends (level, current division, ...)

## Getting Started

* Create a maven project.
* Add the following to your pom.xml
```
	<repositories>
		<repository>
			<id>League-of-Legends-XMPP-Chat-Library-mvn-repo</id>
			<url>https://raw.githubusercontent.com/TheHolyWaffle/League-of-Legends-XMPP-Chat-Library/mvn-repo/</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>com.github.theholywaffle</groupId>
			<artifactId>lolchatapi</artifactId>
			<version>LATEST</version>
		</dependency>
	</dependencies>
```
* To use this api you have to create a [LolChat](src/com/github/theholywaffle/lolchatapi/LolChat.java) object first with the correct [ChatServer](src/com/github/theholywaffle/lolchatapi/ChatServer.java) of your region.
* Do what you want with this [LolChat](src/com/github/theholywaffle/lolchatapi/LolChat.java) object (see examples).

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

[look here](src/com/github/theholywaffle/lolchatapi/example)

## Questions or bugs?

Please let me know them [here](../../issues). I'll help you out as soon as I can.

___
*“This product is not endorsed, certified or otherwise approved in any way by Riot Games, Inc. or any of its affiliates.”*