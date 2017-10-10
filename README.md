League of Legends XMPP Chat Library
===================================
[![Build Status](https://travis-ci.org/TheHolyWaffle/League-of-Legends-XMPP-Chat-Library.svg)](https://travis-ci.org/TheHolyWaffle/League-of-Legends-XMPP-Chat-Library) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.theholywaffle/lolchatapi/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.theholywaffle/lolchatapi) [![Dependency Status](http://www.versioneye.com/user/projects/5401aa7aeab62acbcb000056/badge.png)](http://www.versioneye.com/user/projects/5401aa7aeab62acbcb000056)

A Java 7 XMPP library to chat and interact with the League of Legends chatservers. Built upon [Smack](http://www.igniterealtime.org/projects/smack/) and [JDOM](http://www.jdom.org/).

<a target='_blank' rel='nofollow' href='https://app.codesponsor.io/link/sCywAjA9UJZXFdDf3QeuU2Sp/TheHolyWaffle/League-of-Legends-XMPP-Chat-Library'>
  <img alt='Sponsor' width='888' height='68' src='https://app.codesponsor.io/embed/sCywAjA9UJZXFdDf3QeuU2Sp/TheHolyWaffle/League-of-Legends-XMPP-Chat-Library.svg' />
</a>

## Features

- [Documented source](http://www.javadoc.io/doc/com.github.theholywaffle/lolchatapi)
- Event-based chat system
- Automatic reconnecting
- Managing of Friends and FriendGroups
- Fetch metadata of friends (level, current division, ...)
- Riot API supported (https://developer.riotgames.com)

## Getting Started

### Download

- **Option 1 (Standalone Jar)**: 

   Download the <a href="https://search.maven.org/remote_content?g=com.github.theholywaffle&a=lolchatapi&v=LATEST&c=with-dependencies" target="_blank">latest release</a> and add it to the buildpath of your project. 

- **Option 2 (Maven)**: 
```xml
<dependency>
  <groupId>com.github.theholywaffle</groupId>
  <artifactId>lolchatapi</artifactId>
  <version><!--latest version--></version>
</dependency>
 ```


### Usage

1. Create a [LolChat](src/main/java/com/github/theholywaffle/lolchatapi/LolChat.java) object with the correct [ChatServer](src/main/java/com/github/theholywaffle/lolchatapi/ChatServer.java) of your region.
2. Add any listeners.
3. Login.
4. ...

### Example

```java
final LolChat api = new LolChat(ChatServer.EUW, FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY", RateLimit.DEFAULT));
if (api.login("myusername", "mypassword")) {

	// Example 1: Send Chat Message to all your friends
	for (Friend f : api.getFriends()) {
		f.sendMessage("Hello " + f.getName());
	}

	// Example 2: Send Chat Message to all your friends and wait for an
	// response
	for (Friend f : api.getFriends()) {
		f.sendMessage("Hello " + f.getName(), new ChatListener() {

			@Override
			public void onMessage(Friend friend, String message) {
				System.out.println("Friend " + friend.getName()
						+ " responded to my Hello World!");
			}
		});
	}

	// Example3: Send Chat Message to an specific friend
	Friend f = api.getFriendByName("Dyrus");
	if (f != null && f.isOnline()) {
		f.sendMessage("Hi, I'm your biggest fan!");
	}
}
```

[more examples](example)

### Javadocs

[here](http://www.javadoc.io/doc/com.github.theholywaffle/lolchatapi)

## Questions or bugs?

Please let me know them [here](../../issues). I'll help you out as soon as I can.

___
*“League of Legends XMPP Chat Library isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or anyone officially involved in producing or managing League of Legends. League of Legends and Riot Games are trademarks or registered trademarks of Riot Games, Inc. League of Legends © Riot Games, Inc.”*
