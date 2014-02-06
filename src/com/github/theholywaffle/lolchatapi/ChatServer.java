package com.github.theholywaffle.lolchatapi;

public enum ChatServer {
	EUW("chat.eu.lol.riotgames.com"),
	NA("chat.na1.lol.riotgames.com"),
	EUNE("chat.eun1.lol.riotgames.com"),
	TW("chattw.lol.garenanow.com"),
	TH("chatth.lol.garenanow.com"),
	PH("chatph.lol.garenanow.com"),
	VN("chatvn.lol.garenanow.com");
	String host;

	ChatServer(String host) {
		this.host = host;
	}

}
