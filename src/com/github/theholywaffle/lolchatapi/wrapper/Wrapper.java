package com.github.theholywaffle.lolchatapi.wrapper;

import org.jivesoftware.smack.XMPPConnection;

import com.github.theholywaffle.lolchatapi.LolChat;

public class Wrapper<E> {

	protected XMPPConnection con;
	private E object;
	protected LolChat api;

	protected Wrapper(LolChat api, XMPPConnection con, E object) {
		this.con = con;
		this.object = object;
		this.api = api;
		if (object == null) {
			System.err.println("Object " + object + " is null");
		}
	}

	protected E get() {
		return object;
	}

}
