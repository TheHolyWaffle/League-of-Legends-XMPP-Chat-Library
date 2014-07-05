package com.github.theholywaffle.lolchatapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.github.theholywaffle.lolchatapi.listeners.FriendRequestListener;

public class LeaguePacketListener implements PacketListener {

	private XMPPConnection connection;
	private FriendRequestListener requestListener;
	private LolChat api;

	public LeaguePacketListener(LolChat api, XMPPConnection connection) {
		this.api = api;
		this.connection = connection;
	}

	public void processPacket(Packet packet) throws NotConnectedException {
		Presence presence = (Presence) packet;
		if (presence.getType().equals(Presence.Type.subscribe)) {

			switch (api.getFriendRequestPolicy()) {

			case ACCEPT_ALL:
				accept(presence.getFrom());
				break;

			case MANUAL:
				if (requestListener != null) {
					if (requestListener.onFriendRequest(presence.getFrom())) {
						accept(presence.getFrom());
					} else {
						decline(presence.getFrom());
					}
				} else {
					System.err
							.println("FriendRequestListener is null when FriendRequestPolicy is MANUAL!");
				}
				break;

			case REJECT_ALL:
				decline(presence.getFrom());
				break;
			}

		}
	}

	public void setFriendRequestListener(FriendRequestListener requestListener) {
		this.requestListener = requestListener;
	}

	private void accept(String from) throws NotConnectedException {
		Presence newp = new Presence(Presence.Type.subscribed);
		newp.setTo(from);
		connection.sendPacket(newp);
		Presence subscription = new Presence(Presence.Type.subscribe);
		subscription.setTo(from);
		connection.sendPacket(subscription);
		if (api.isOnline()) {
			api.setOnline();
		}		
	}

	private void decline(String from) throws NotConnectedException {
		Presence newp = new Presence(Presence.Type.unsubscribed);
		newp.setTo(from);
		connection.sendPacket(newp);
	}

}
