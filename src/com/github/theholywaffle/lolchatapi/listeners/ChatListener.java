package com.github.theholywaffle.lolchatapi.listeners;

import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public interface ChatListener {

	public void onMessage(Friend friend, String message);

}
